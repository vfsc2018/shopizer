package com.salesmanager.shop.admin.controller.vouchercode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

import com.salesmanager.core.business.services.voucher.VoucherService;
import com.salesmanager.core.business.services.vouchercode.VoucherCodeService;
import com.salesmanager.core.business.utils.ajax.AjaxPageableResponse;
import com.salesmanager.core.business.utils.ajax.AjaxResponse;
import com.salesmanager.core.model.common.CriteriaOrderBy;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.voucher.Voucher;
import com.salesmanager.core.model.vouchercode.VoucherCode;
import com.salesmanager.core.model.vouchercode.VoucherCodeCriteria;
import com.salesmanager.core.model.vouchercode.VoucherCodeList;
import com.salesmanager.shop.admin.controller.ControllerConstants;
import com.salesmanager.shop.admin.model.web.Menu;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.utils.LabelUtils;

@Controller
@Scope("session")
public class VoucherCodeController {

	@Inject
	private VoucherCodeService voucherCodeService;
	@Inject
	private VoucherService voucherService;
	
	@Inject
	LabelUtils messages;

	private static final Logger LOGGER = LoggerFactory.getLogger(VoucherCodeController.class);


	@RequestMapping(value = "/admin/vouchercodes/list.html", method = RequestMethod.GET)
	public String displayOrders(Model model, HttpServletRequest request, HttpServletResponse response) {
		
		setMenu(model, request);
		String voucherId = request.getParameter("voucherId");
		
		if(StringUtils.isNotEmpty(voucherId)){
			request.getSession().setAttribute("voucherId",voucherId);
			
			model.addAttribute("voucher",voucherService.getById(Long.parseLong(voucherId)));
			model.addAttribute("intAmount",voucherCodeService.countCodeByVoucherId(Long.parseLong(voucherId)));
		}else{
			request.getSession().removeAttribute("voucherId");
		}
		
		// the list of orders is from page method

		return ControllerConstants.Tiles.VoucherCode.vouchers;

	}

	@RequestMapping(value = "/admin/vouchercodes/reportCode.html", method = RequestMethod.GET)
	public String reportVoucherCode(Model model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// display menu
		setMenu(model, request);
		try {
			model.addAttribute("data",request.getSession().getAttribute("STORE_VOUCHERCODEDATA"));
		} catch (Exception e) {
			model.addAttribute("data",new ArrayList<>());
		}
		
		// MerchantStore sessionStore = (MerchantStore) request.getAttribute(Constants.ADMIN_STORE);
		// model.addAttribute("currency",sessionStore.getCurrency());
		
		return "admin-orders-report-voucherCode";
	}	

	@RequestMapping(value = "/admin/vouchercodes/printCode.html", method = RequestMethod.GET)
	public String printVoucherCode(Model model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// display menu
		setMenu(model, request);
		try {
			model.addAttribute("data",request.getSession().getAttribute("STORE_VOUCHERCODEDATA"));
		} catch (Exception e) {
			model.addAttribute("data",new ArrayList<>());
		}
		
		// MerchantStore sessionStore = (MerchantStore) request.getAttribute(Constants.ADMIN_STORE);
		// model.addAttribute("currency",sessionStore.getCurrency());
		
		return "admin-orders-print-voucherCode";
	}	
	
	
	@RequestMapping(value="/admin/vouchercodes/createVoucherCode.html", method=RequestMethod.GET)
	public String displayVoucherCodeCreate(Model model, HttpServletRequest request, HttpServletResponse response) {
		//display menu
		setMenu(model,request);
		
		VoucherCodeForm temp = new VoucherCodeForm();
		String voucherId = request.getParameter("voucherId");
		// temp.setCode(genCode());
		if(StringUtils.isNotEmpty(voucherId)){
			try{
				temp.setVoucherId(Long.parseLong(voucherId));
			}catch(Exception e){}
		}
		model.addAttribute("voucherCode", temp);
		
		model.addAttribute("lstVoucher", voucherService.getActiveVoucher());
		
		return "admin-voucherCodes-create";
	}
	
	
	@RequestMapping(value = "/admin/vouchercodes/paging.html", method = RequestMethod.POST)
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
			if(StringUtils.isEmpty(voucherId) && request.getSession().getAttribute("voucherId")!=null){
				voucherId = (String)(request.getSession().getAttribute("voucherId"));
			}

			String blocked = request.getParameter("blocked");
			
			String	code = request.getParameter("code");
			String	index = request.getParameter("index");
			String	customerId  = request.getParameter("customerId");
			
			String	used = request.getParameter("used");
			String orderId = request.getParameter("orderId");
			String batch = request.getParameter("batch"); 
			String available = request.getParameter("available");
			
			
			if(used!=null && used.length()!=10){
				return new ResponseEntity<>("{}",httpHeaders,HttpStatus.OK);
			}
			
			VoucherCodeCriteria criteria = new VoucherCodeCriteria();
			criteria.setOrderBy(CriteriaOrderBy.DESC);
			criteria.setCriteriaOrderByField("id");
			criteria.setStartIndex(startRow);
			criteria.setMaxCount(endRow);
			
			if(StringUtils.isNotBlank(blocked)) {
				criteria.setBlocked(blocked.equals("true"));
			}
			if(StringUtils.isNotBlank(available)) {
				criteria.setAvailable(available.equals("true"));
			}

			if(!StringUtils.isBlank(id)) {
				try {
					criteria.setId(Long.parseLong(id));
				} catch (Exception e) {
					return new ResponseEntity<>("{}",httpHeaders,HttpStatus.OK);
				}
			}
			if(!StringUtils.isBlank(voucherId)) {
				try {
					criteria.setVoucherId(Long.parseLong(voucherId));
				} catch (Exception e) {
					return new ResponseEntity<>("{}",httpHeaders,HttpStatus.OK);
				}
			}
			if(!StringUtils.isBlank(customerId)) {
				try {
					criteria.setCustomerId(Long.parseLong(customerId));
				} catch (Exception e) {
					return new ResponseEntity<>("{}",httpHeaders,HttpStatus.OK);
				}
			}

			if(!StringUtils.isBlank(orderId)) {
				try {
					criteria.setOrderId(Long.parseLong(orderId));
				} catch (Exception e) {
					return new ResponseEntity<>("{}",httpHeaders,HttpStatus.OK);
				}
			}

			if(!StringUtils.isBlank(index)) {
				try {
					criteria.setIndex(Integer.parseInt(index));
				} catch (Exception e) {
					return new ResponseEntity<>("{}",httpHeaders,HttpStatus.OK);
				}
			}
			
			if(!StringUtils.isBlank(used)) {
				try{
					criteria.setUsed(used);
				}catch(Exception e){}
			}

			if(!StringUtils.isBlank(code)) {
				criteria.setCode(code.toUpperCase());
			}

			if(!StringUtils.isBlank(batch)) {
				criteria.setBatch(batch);
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
						entry.put("voucherId", transaction.getVoucherId());
					}
					
					entry.put("code", transaction.getCode());
					entry.put("index", transaction.getIndex());
					entry.put("batch", transaction.getBatch());
					entry.put("blocked", transaction.getBlocked()>0);
					
					if(transaction.getCustomer()!=null){
						entry.put("customerId", transaction.getCustomerId());	
					}
					entry.put("available", transaction.getUsed()==null);
					if(transaction.getUsed()!=null){
						entry.put("used", com.salesmanager.shop.utils.DateUtil.formatTimeDate(transaction.getUsed()));	
					}
					
					if(transaction.getOrder()!=null){
						entry.put("orderId", transaction.getOrderId());	
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



	@RequestMapping(value = "/admin/vouchercodes/view.html", method = RequestMethod.GET)
	public String viewVoucherCode(@RequestParam("id") Long id, Model model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// display menu
		setMenu(model, request);

		VoucherCode bean = voucherCodeService.getById(id);
		VoucherCodeForm temp = new VoucherCodeForm();
		temp.setId(bean.getId());
		if(bean.getVoucher()!=null){
			temp.setVoucherId(bean.getVoucherId());
		}
		
		temp.setCode(bean.getCode());
		temp.setBatch(bean.getBatch());
		temp.setSecurecode(bean.getSecurecode());
		temp.setBlocked(bean.getBlocked());
		temp.setBlockMessage(bean.getBlockMessage());
		temp.setUsed(com.salesmanager.shop.utils.DateUtil.formatDate(bean.getUsed()));
		temp.setIndex(bean.getIndex());
		if(bean.getOrder()!=null){
			temp.setOrderId(bean.getOrderId());	
		}
		
		
		model.addAttribute("voucherCode", temp);

		return ControllerConstants.Tiles.VoucherCode.Edit;
	}


	@RequestMapping(value = "/admin/vouchercodes/save.html", method = RequestMethod.POST)
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
	

	@RequestMapping(value = "/admin/vouchercodes/genCode.html", method = RequestMethod.POST)
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
				Integer nextIndex = voucherCodeService.getMaxIndexByVoucherId(bean.getVoucherId());
				Long next = nextIndex.longValue();
				Voucher voucher = voucherService.getById(bean.getVoucherId());
				for(int i = 1;i<=bean.getAmtCode();i++){
					VoucherCode code = new VoucherCode();
					code.setVoucher(voucher);
					code.setIndex(nextIndex + i);
					code.setCode(voucherCodeService.encode(VoucherCodeService.TYPE_ORDER_PAYMENT, voucher.getId(), next + i));
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
	
    @RequestMapping(value="/admin/vouchercodes/remove.html", method=RequestMethod.POST)
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
