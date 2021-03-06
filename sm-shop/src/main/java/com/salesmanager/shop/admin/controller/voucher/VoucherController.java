package com.salesmanager.shop.admin.controller.voucher;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.services.catalog.product.PricingService;
import com.salesmanager.core.business.services.system.ModuleConfigurationService;
import com.salesmanager.core.business.services.voucher.VoucherService;
import com.salesmanager.core.business.utils.ajax.AjaxPageableResponse;
import com.salesmanager.core.business.utils.ajax.AjaxResponse;
import com.salesmanager.core.model.common.CriteriaOrderBy;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.voucher.Voucher;
import com.salesmanager.core.model.voucher.VoucherCriteria;
import com.salesmanager.core.model.voucher.VoucherList;
import com.salesmanager.shop.admin.controller.ControllerConstants;
import com.salesmanager.shop.admin.model.web.Menu;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.utils.DateUtil;
import com.salesmanager.shop.utils.LabelUtils;

@Controller
@Scope("session")
public class VoucherController {

	@Inject
	private VoucherService voucherService;


	@Inject
	LabelUtils messages;
	
	@Inject
	PricingService pricingService;

	@Inject
	protected ModuleConfigurationService moduleConfigurationService;

	private static final Logger LOGGER = LoggerFactory.getLogger(VoucherController.class);


	@RequestMapping(value = "/admin/vouchers/list.html", method = RequestMethod.GET)
	public String displayOrders(Model model, HttpServletRequest request, HttpServletResponse response) {

		setMenu(model, request);

		// the list of orders is from page method

		return ControllerConstants.Tiles.Voucher.vouchers;

	}


	@RequestMapping(value = "/admin/vouchers/paging.html", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> pageBills(
			HttpServletRequest request, HttpServletResponse response,
			Locale locale) {

		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		AjaxPageableResponse resp = new AjaxPageableResponse();

		try {

			int startRow = Integer.parseInt(request.getParameter("_startRow"));
			int endRow = Integer.parseInt(request.getParameter("_endRow"));

			String id = request.getParameter("id");
			String blocked = request.getParameter("blocked");
			
			String	approved = request.getParameter("approved");
			String	startDate = request.getParameter("startDate");
			String	endDate = request.getParameter("endDate");
			String manager = request.getParameter("manager");
			String product = request.getParameter("product");
			String code = request.getParameter("code");
			
			if(startDate!=null && startDate.length()!=10){
				return new ResponseEntity<>("{}",httpHeaders,HttpStatus.OK);
			}			
			if(endDate!=null && endDate.length()!=10){
				return new ResponseEntity<>("{}",httpHeaders,HttpStatus.OK);
			}

			VoucherCriteria criteria = new VoucherCriteria();
			criteria.setOrderBy(CriteriaOrderBy.DESC);
			criteria.setCriteriaOrderByField("id");
			criteria.setStartIndex(startRow);
			criteria.setMaxCount(endRow);

			criteria.setApproved(approved!=null && approved.equals("true"));
			criteria.setBlocked(blocked!=null && blocked.equals("true"));
			if(!StringUtils.isBlank(id)) {
				try {
						criteria.setId(Long.parseLong(id));
				} catch (Exception e) {
					return new ResponseEntity<>("{}",httpHeaders,HttpStatus.OK);
				}
			}
			
			if(!StringUtils.isBlank(startDate)) {
				try{
					criteria.setStartDate(DateUtil.getDate(startDate));
				}catch(Exception e){}
			}
			if(!StringUtils.isBlank(endDate)) {
				try{
					criteria.setEndDate(DateUtil.getDate(endDate));
				}catch(Exception e){}
			}
			
			if(!StringUtils.isBlank(manager)) {
				criteria.setManager(manager);
			}

			if(!StringUtils.isBlank(product)) {
				criteria.setProduct(product);
			}

			if(!StringUtils.isBlank(code)) {
				criteria.setCode(code);
			}
			
			MerchantStore store = (MerchantStore) request.getAttribute(Constants.ADMIN_STORE);

			VoucherList list = voucherService.getListByStore(store, criteria);

			
			if (list.getVouchers() != null) {

				resp.setTotalRow(list.getTotalCount());

				for (Voucher transaction : list.getVouchers()) {
					
					@SuppressWarnings("rawtypes")
					Map entry = new HashMap();
					entry.put("id", transaction.getId());
					entry.put("manager", transaction.getManager());
					entry.put("product", transaction.getProductSku());
					entry.put("blocked", transaction.getBlocked()!=null && transaction.getBlocked()>0);
					entry.put("code", transaction.getCode());
					
					entry.put("approved", transaction.getApproved()!=null);
					entry.put("startDate", DateUtil.formatDate(transaction.getStartDate()));
					entry.put("endDate", DateUtil.formatDate(transaction.getEndDate()));
					entry.put("voucher", transaction.getPoint() + "/" + transaction.getDiscount() + "/" + transaction.getPercent());
					
					// entry.put("partnerId", transaction.getPartnerId());
					
					resp.addDataEntry(entry);

				}
			}
			
			
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);

		} catch (Exception e) {
			LOGGER.error("Error while paging orders", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}

		String returnString = resp.toJSONString();

		return new ResponseEntity<>(returnString, httpHeaders, HttpStatus.OK);
	}
	
	@RequestMapping(value="/admin/vouchers/createVoucher.html", method=RequestMethod.GET)
	public String displayProductCreate(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//display menu
		setMenu(model,request);
		
		VoucherForm temp = new VoucherForm();
		model.addAttribute("voucher", temp);
		return "admin-vouchers-create";

	}
	
	


	@RequestMapping(value = "/admin/vouchers/view.html", method = RequestMethod.GET)
	public String viewNotificaiton(@RequestParam("id") Long id, Model model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// display menu
		setMenu(model, request);

		com.salesmanager.core.model.voucher.Voucher bean = voucherService.getById(id);
		VoucherForm temp = new VoucherForm();
		temp.setId(bean.getId());
		temp.setCode(bean.getCode());
		temp.setDescription(bean.getDescription());
		temp.setPoint(bean.getPoint());
		temp.setOrderIndex(bean.getOrderIndex());



		temp.setPercent(bean.getPercent());
		temp.setDiscount(bean.getDiscount());
		temp.setStatus(bean.getStatus());
		temp.setBlocked(bean.getBlocked());
		temp.setBlockMessage(bean.getBlockMessage());
		temp.setStartDate(DateUtil.formatDate(bean.getStartDate()));
		temp.setEndDate(DateUtil.formatDate(bean.getEndDate()));
		temp.setWeekDays(bean.getWeekDays());
		temp.setDayOfMonth(bean.getDayOfMonth());
		temp.setStartTime(bean.getStartTime());
		temp.setEndTime(bean.getEndTime());
		temp.setApproved(DateUtil.formatDate(bean.getApproved())); 
		temp.setPartnerId(bean.getPartnerId());
		temp.setExpire(DateUtil.formatDate(bean.getExpire()));
		temp.setManager(bean.getManager());
		temp.setProductSku(bean.getProductSku());
		model.addAttribute("voucher", temp);

		return ControllerConstants.Tiles.Voucher.Edit;
	}
	
	

	@RequestMapping(value = "/admin/vouchers/save.html", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> buildBill(@RequestBody VoucherForm bean) {

		AjaxResponse resp = new AjaxResponse();
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
			try {
				Voucher temp = new Voucher();
				if(bean.getId()!=null && bean.getId()>0){
					temp = voucherService.getById(bean.getId());
				}
				temp.setId(bean.getId());
				temp.setCode(bean.getCode());
				temp.setDescription(bean.getDescription());
				temp.setPoint(bean.getPoint());
				temp.setOrderIndex(bean.getOrderIndex());



				temp.setDiscount(bean.getDiscount());
				temp.setPercent(bean.getPercent());
				temp.setStatus(bean.getStatus());
				temp.setBlocked(bean.getBlocked());
				temp.setBlockMessage(bean.getBlockMessage());
				temp.setStartDate(DateUtil.getDate(bean.getStartDate()));
				temp.setEndDate(DateUtil.getDate(bean.getEndDate()));
				temp.setWeekDays(bean.getWeekDays());
				temp.setDayOfMonth(bean.getDayOfMonth());
				temp.setStartTime(bean.getStartTime());
				temp.setEndTime(bean.getEndTime());
				temp.setApproved(DateUtil.getDate(bean.getApproved())); 
				temp.setPartnerId(bean.getPartnerId());
				// temp.setExpire(DateUtil.getDate(bean.getExpire()));
				temp.setManager(bean.getManager());
				temp.setProductSku(bean.getProductSku());
				voucherService.saveVoucher(temp);
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
				
				
			} catch (Exception e) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorMessage(e);
			}
		
		String returnString = resp.toJSONString();
		return new ResponseEntity<>(returnString, httpHeaders,HttpStatus.OK);

	}
	
    @RequestMapping(value="/admin/vouchers/remove.html", method=RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> deleteVoucher(HttpServletRequest request, Locale locale) {
        String sid = request.getParameter("id");
        AjaxResponse resp = new AjaxResponse();
        try {
            Long id = Long.parseLong(sid);
            if(voucherService.deleteVoucher(id)){
                resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
            }else{
                resp.setStatusMessage(messages.getMessage("message.product.not.empty", locale));
                resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
            }
        } catch (Exception e) {
            LOGGER.error("Error while deleting voucher", e);
            resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorMessage(e);
        }
        String returnString = resp.toJSONString();
        final HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(returnString, httpHeaders, HttpStatus.OK);
    }

	private void setMenu(Model model, HttpServletRequest request) {

		// display menu
		Map<String, String> activeMenus = new HashMap<>();

		activeMenus.put("voucher", "voucher");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");

		Menu currentMenu = menus.get("voucher");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
		//

	}

}
