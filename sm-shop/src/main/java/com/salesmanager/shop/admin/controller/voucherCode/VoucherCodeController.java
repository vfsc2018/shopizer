package com.salesmanager.shop.admin.controller.voucherCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

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
import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.services.order.OrderService;
import com.salesmanager.core.business.services.system.ModuleConfigurationService;
import com.salesmanager.core.business.services.voucher.VoucherService;
import com.salesmanager.core.business.services.voucherCode.VoucherCodeService;
import com.salesmanager.core.business.utils.ajax.AjaxPageableResponse;
import com.salesmanager.core.business.utils.ajax.AjaxResponse;
import com.salesmanager.core.model.common.CriteriaOrderBy;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.BillMaster;
import com.salesmanager.core.model.voucher.Voucher;
import com.salesmanager.core.model.voucherCode.VoucherCode;
import com.salesmanager.core.model.voucherCode.VoucherCodeCriteria;
import com.salesmanager.core.model.voucherCode.VoucherCodeList;
import com.salesmanager.shop.admin.controller.ControllerConstants;
import com.salesmanager.shop.admin.model.web.Menu;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.utils.DateUtil;
import com.salesmanager.shop.utils.LabelUtils;
import com.salesmanager.shop.utils.VoucherUtils;

@Controller
@Scope("session")
public class VoucherCodeController {

	@Inject
	private VoucherCodeService voucherCodeService;
	@Inject
	private VoucherService voucherService;
	
	
	@Inject
	private CustomerService customerService;
	
	@Inject
	private OrderService orderService;
	
	@Inject
	LabelUtils messages;
	
	@Inject
	PricingService pricingService;

	@Inject
	protected ModuleConfigurationService moduleConfigurationService;

	private static final Logger LOGGER = LoggerFactory.getLogger(VoucherCodeController.class);


	@RequestMapping(value = "/admin/voucherCodes/list.html", method = RequestMethod.GET)
	public String displayOrders(Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		setMenu(model, request);
		String voucherId = request.getParameter("voucherId");
		
		if(voucherId!=null && !voucherId.equals("")){
			request.getSession().setAttribute("voucherId",voucherId);
			
			model.addAttribute("voucher",voucherService.getById(Long.parseLong(voucherId)));
			model.addAttribute("intAmount",voucherCodeService.getVoucherCodeByVoucherId(Long.parseLong(voucherId)));
		}else{
			request.getSession().removeAttribute("voucherId");
		}
		
		// the list of orders is from page method

		return ControllerConstants.Tiles.VoucherCode.vouchers;

	}

	@RequestMapping(value = "/admin/voucherCodes/reportCode.html", method = RequestMethod.GET)
	public String reportBill(@RequestParam("id") Integer billId,Model model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// display menu
		setMenu(model, request);
		List<BillMaster> dataStore = new ArrayList<>();
		try {
			dataStore = (List<BillMaster>)request.getSession().getAttribute("STORE_VOUCHERCODEDATA");
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("data",dataStore);
		
		MerchantStore sessionStore = (MerchantStore) request.getAttribute(Constants.ADMIN_STORE);
		model.addAttribute("currency",sessionStore.getCurrency());
		
		return "admin-orders-report-voucherCode";
	}	
	
	
	@RequestMapping(value="/admin/voucherCodes/createVoucherCode.html", method=RequestMethod.GET)
	public String displayProductCreate(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//display menu
		setMenu(model,request);
		
		VoucherCodeForm temp = new VoucherCodeForm();
		temp.setCode(genCode());
		model.addAttribute("voucherCode", temp);
		
		model.addAttribute("lstVoucher", voucherService.getVoucherEndDate());
		
		
		return "admin-voucherCodes-create";

	}
	
	private String genCode(){
	    int leftLimit = 97; // letter 'a'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = 10;
	    Random random = new Random();
	    StringBuilder buffer = new StringBuilder(targetStringLength);
	    for (int i = 0; i < targetStringLength; i++) {
	        int randomLimitedInt = leftLimit + (int) 
	          (random.nextFloat() * (rightLimit - leftLimit + 1));
	        buffer.append((char) randomLimitedInt);
	    }
	    String generatedString = buffer.toString();
	    
		return generatedString;
	}
	
	@RequestMapping(value = "/admin/voucherCodes/paging.html", method = RequestMethod.POST)
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
			String voucherId = request.getParameter("voucherId");
			
			
			String	code = request.getParameter("code");
			String	index = request.getParameter("index");
			String	customerId  = request.getParameter("customerId ");
			
			
			String	used = request.getParameter("used");
			String	redeem = request.getParameter("redeem");
			
			String orderId = request.getParameter("orderId");
			
			
			if(used!=null && used.length()!=10){
				return new ResponseEntity<>("{}",httpHeaders,HttpStatus.OK);
			}
			if(redeem!=null && redeem.length()!=10){
				return new ResponseEntity<>("{}",httpHeaders,HttpStatus.OK);
			}	
			
			VoucherCodeCriteria criteria = new VoucherCodeCriteria();
			criteria.setOrderBy(CriteriaOrderBy.DESC);
			criteria.setStartIndex(startRow);
			criteria.setMaxCount(endRow);
			
			
			try {
				if(!StringUtils.isBlank(id)) {
					criteria.setId(Long.parseLong(id));
				}
				if(!StringUtils.isBlank(voucherId)) {
					criteria.setVoucherId(Long.parseLong(voucherId));
				}
				if(request.getSession().getAttribute("voucherId")!=null){
					criteria.setVoucherId(Long.parseLong((String)request.getSession().getAttribute("voucherId")));
				}
				
				if(!StringUtils.isBlank(code)) {
					criteria.setCode(code);
				}
				
				if(!StringUtils.isBlank(index)) {
					criteria.setIndex(Integer.parseInt(index));
				}
				
				if(!StringUtils.isBlank(orderId)) {
					criteria.setOrderId(Long.parseLong(orderId));
				}
				
				if(!StringUtils.isBlank(customerId )) {
					criteria.setCustomerId(Long.parseLong(customerId) );
				}
				
				if(!StringUtils.isBlank(used)) {
					criteria.setUsed(used);
				}
				
				if(!StringUtils.isBlank(redeem)) {
					criteria.setRedeem(redeem);
				}
			
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}


			
			MerchantStore store = (MerchantStore) request.getAttribute(Constants.ADMIN_STORE);

			VoucherCodeList list = voucherCodeService.getListByStore(store, criteria);
			
			request.getSession().setAttribute("STORE_VOUCHERCODEDATA",list.getVoucherCodes());
			
			if (list.getVoucherCodes() != null) {

				resp.setTotalRow(list.getTotalCount());

				for (VoucherCode transaction : list.getVoucherCodes()) {
					
					@SuppressWarnings("rawtypes")
					Map entry = new HashMap();
					
					entry.put("id", transaction.getId());
					if(transaction.getVoucher()!=null){
						entry.put("voucherId", transaction.getVoucher().getId());
					}
					
					entry.put("code", transaction.getCode());
					entry.put("index", transaction.getIndex());
					entry.put("batch", transaction.getBatch());
					entry.put("blocked", transaction.getBlocked()>0);
					
					if(transaction.getCustomer()!=null){
						entry.put("customerId ", transaction.getCustomer().getId());	
					}
					
					if(transaction.getUsed()!=null){
						entry.put("used", DateUtil.formatTimeDate(transaction.getUsed()));	
					}
					if(transaction.getRedeem()!=null){
						entry.put("redeem", DateUtil.formatTimeDate(transaction.getRedeem()));	
					}
					
					if(transaction.getOrder()!=null){
						entry.put("orderId ", transaction.getOrder().getId());	
					}
					
					
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



	@RequestMapping(value = "/admin/voucherCodes/view.html", method = RequestMethod.GET)
	public String viewVoucherCode(@RequestParam("id") Long id, Model model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// display menu
		setMenu(model, request);

		VoucherCode bean = voucherCodeService.getById(id);
		VoucherCodeForm temp = new VoucherCodeForm();
		temp.setId(bean.getId());
		temp.setVoucherId(bean.getVoucher().getId());
		temp.setCode(bean.getCode());
		temp.setBatch(bean.getBatch());
		temp.setSecurecode(bean.getSecurecode());
		temp.setBlocked(bean.getBlocked());
		temp.setBlockMessage(bean.getBlockMessage());
		temp.setUsed(DateUtil.formatDate(bean.getUsed()));
		temp.setIndex(bean.getIndex());
		temp.setRedeem(DateUtil.formatDate(bean.getRedeem()));
		if(bean.getOrder()!=null){
			temp.setOrderId(bean.getOrder().getId());	
		}
		
		
		model.addAttribute("voucherCode", temp);

		return ControllerConstants.Tiles.VoucherCode.Edit;
	}


	@RequestMapping(value = "/admin/voucherCodes/save.html", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> buildBill(@RequestBody VoucherCodeEditForm bean) {

		

		AjaxResponse resp = new AjaxResponse();
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
			try {

				VoucherCode temp = new VoucherCode();
				if(bean.getId()!=null && bean.getId()>0){
					temp = voucherCodeService.getById(bean.getId());
				}
				
				temp.setBlocked(bean.getBlocked());
				temp.setBlockMessage(bean.getBlockMessage());
				
				voucherCodeService.saveVoucher(temp);	
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
				
				
			} catch (Exception e) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorMessage(e);
			}
		
		String returnString = resp.toJSONString();
		return new ResponseEntity<>(returnString, httpHeaders,HttpStatus.OK);

	}
	

	@RequestMapping(value = "/admin/voucherCodes/genCode.html", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> genCode(@RequestBody VoucherCodeCreateForm bean) {

		AjaxResponse resp = new AjaxResponse();
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		if(bean.getAmtCode()==null || bean.getAmtCode()<=0){
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			String returnString = resp.toJSONString();
			return new ResponseEntity<>(returnString, httpHeaders,HttpStatus.OK);
		}
			try {
				List<VoucherCode> ls = new ArrayList<>();
				Integer nextIndex = voucherCodeService.countGrByVoucherId(bean.getVoucherId());
				Long next = nextIndex.longValue();
				Voucher voucher = voucherService.getById(bean.getVoucherId());
				for(int i = 1;i<=bean.getAmtCode();i++){
					VoucherCode code = new VoucherCode();
					code.setVoucher(voucher);
					code.setIndex(nextIndex + i);
					code.setCode(VoucherUtils.encode(VoucherUtils.TYPE_ORDER_PAYMENT, voucher.getId(), next + i));
					code.setId(0L);
					code.setBatch(bean.getBatch());
					ls.add(code);
				}
				voucherCodeService.saveVoucher(ls);	
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
			} catch (Exception e) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorMessage(e);
			}
		
		String returnString = resp.toJSONString();
		return new ResponseEntity<>(returnString, httpHeaders,HttpStatus.OK);

	}
	
    @RequestMapping(value="/admin/voucherCodes/remove.html", method=RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> deleteVoucher(HttpServletRequest request, Locale locale) {
        String sid = request.getParameter("id");
        AjaxResponse resp = new AjaxResponse();
        try {
            Long id = Long.parseLong(sid);
            if(voucherCodeService.deleteVoucher(id)){
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

		activeMenus.put("voucherCode", "voucherCode");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");

		Menu currentMenu = menus.get("voucher");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
		//

	}

}
