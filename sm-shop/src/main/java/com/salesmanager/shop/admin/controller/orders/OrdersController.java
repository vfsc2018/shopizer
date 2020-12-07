package com.salesmanager.shop.admin.controller.orders;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.salesmanager.core.business.services.order.OrderService;
import com.salesmanager.core.business.services.reference.country.BillMasterService;
import com.salesmanager.core.business.services.system.ModuleConfigurationService;
import com.salesmanager.core.business.utils.ProductPriceUtils;
import com.salesmanager.core.business.utils.ajax.AjaxPageableResponse;
import com.salesmanager.core.business.utils.ajax.AjaxResponse;
import com.salesmanager.core.model.common.CriteriaOrderBy;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.CollectBill;
import com.salesmanager.core.model.order.Order;
import com.salesmanager.core.model.order.OrderCriteria;
import com.salesmanager.core.model.order.OrderList;
import com.salesmanager.shop.admin.controller.ControllerConstants;
import com.salesmanager.shop.admin.model.web.Menu;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.utils.DateUtil;
import com.salesmanager.shop.utils.LabelUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Manage order list
 * Manage search order
 * @author csamson 
 *
 */
@Controller
@JsonAutoDetect(getterVisibility=com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE)
public class OrdersController {
	
	@Inject
	OrderService orderService;
	
	
	@Inject
	BillMasterService billService;
	@Inject
	LabelUtils messages;
	
	@Inject
	private ProductPriceUtils priceUtil;
	
	@Inject
	protected ModuleConfigurationService moduleConfigurationService;
	 
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderControler.class);

	
	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value="/admin/orders/list.html", method=RequestMethod.GET)
	public String displayOrders(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request);

		//the list of orders is from page method
		
		return ControllerConstants.Tiles.Order.orders;
		
		
	}


	@PreAuthorize("hasRole('ORDER')")
	@SuppressWarnings({ "unchecked"})
	@RequestMapping(value="/admin/orders/paging.html", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> pageOrders(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		AjaxPageableResponse resp = new AjaxPageableResponse();

		try {
			
			int startRow = Integer.parseInt(request.getParameter("_startRow"));
			int endRow = Integer.parseInt(request.getParameter("_endRow"));
			String	paymentModule = request.getParameter("paymentModule");
			String customerName = request.getParameter("customer");
			String status = request.getParameter("status");
			String	pId = request.getParameter("orderId");
			String	phone = request.getParameter("phone");
			String	startDate = request.getParameter("startDate");
			String	endDate = request.getParameter("endDate");
			String	date = request.getParameter("date");
			String	address = request.getParameter("address");
			String purchased = request.getParameter("purchased");

			if(date!=null && date.length()!=10){
				return new ResponseEntity<>("{}",httpHeaders,HttpStatus.OK);
			}
			if(startDate!=null && startDate.length()!=10){
				return new ResponseEntity<>("{}",httpHeaders,HttpStatus.OK);
			}
			if(endDate!=null && endDate.length()!=10){
				return new ResponseEntity<>("{}",httpHeaders,HttpStatus.OK);
			}

			OrderCriteria criteria = new OrderCriteria();
			criteria.setOrderBy(CriteriaOrderBy.DESC);
			criteria.setStartIndex(startRow);
			criteria.setMaxCount(endRow);

			criteria.setPurchased(purchased!=null && purchased.equals("true"));

			if(!StringUtils.isBlank(date)){
				criteria.setDate(date);
			}
			if(!StringUtils.isBlank(address)){
				criteria.setAddress(address);
			}
			if(startDate!=null){
				criteria.setStartDate(DateUtil.getDate(startDate));
			}
			if(endDate!=null){
				criteria.setEndDate(DateUtil.getDate(endDate));
			}

			if(!StringUtils.isBlank(phone)) {
				criteria.setPhone(phone);
			}

			if(!StringUtils.isBlank(paymentModule)) {
				criteria.setPaymentMethod(paymentModule);
			}
			
			if(!StringUtils.isBlank(customerName)) {
				criteria.setCustomerName(customerName);
			}
			
			if(!StringUtils.isBlank(status)) {
				criteria.setStatus(status.toUpperCase());
			}
			if(!StringUtils.isBlank(pId)) {
				criteria.setId(Long.parseLong(pId));
			}
			
			// Language language = (Language)request.getAttribute("LANGUAGE");
			MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
			// List<IntegrationModule> paymentModules = moduleConfigurationService.getIntegrationModules( "PAYMENT" );


			OrderList orderList = orderService.listByStore(store, criteria);
			request.getSession().setAttribute("STORE_ORDERDATA",orderList.getOrders());
			
			
			if(orderList.getOrders()!=null) {	
				resp.setTotalRow(orderList.getTotalCount());

				for(Order order : orderList.getOrders()) {
					
					@SuppressWarnings("rawtypes")
					Map entry = new HashMap();
					entry.put("orderId", order.getId());
					entry.put("customer", order.getBilling().getFirstName()); // + " " + order.getBilling().getLastName());
					entry.put("amount", priceUtil.getAdminFormatedAmountWithCurrency(store,order.getTotal()));//todo format total
					entry.put("date", DateUtil.formatDate(order.getDatePurchased()));
					entry.put("purchased", order.getPaymentTime()!=null);
					entry.put("startDate", DateUtil.formatDate(order.getFromDate()));
					entry.put("endDate", DateUtil.formatDate(order.getToDate()));
					entry.put("phone", order.getBilling().getTelephone());
					entry.put("address", order.getBilling().getAddress());
					entry.put("status", order.getStatus().name());
					
//					if ( paymentModules!= null && paymentModules.size() > 0 ) 
//					{	
//						for ( int index = 0; index < paymentModules.size(); index++ )
//						{
//							if ( paymentModules.get(index).getCode().equalsIgnoreCase( order.getPaymentModuleCode() ) )
//							{
//								 paymentModule = paymentModules.get(index).getCode();
//								 break;
//							}
//						}
//	
//					}
//	
//					entry.put("paymentModule", paymentModule );
					resp.addDataEntry(entry);				
					
				}
				
			}
			
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			

		
		} catch (Exception e) {
			LOGGER.error("Error while paging orders", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		String returnString = resp.toJSONString();

		return new ResponseEntity<>(returnString,httpHeaders,HttpStatus.OK);
	}
	
	

	@RequestMapping(value = "/admin/orders/reportOrder.html", method = RequestMethod.GET)
	public String reportBill(@RequestParam("id") Integer billId,Model model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// display menu
		setMenu(model, request);
		List<Order> dataStore = new ArrayList<>();
		try {
			dataStore = (List<Order>)request.getSession().getAttribute("STORE_ORDERDATA");
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("data",dataStore);
		
		MerchantStore sessionStore = (MerchantStore) request.getAttribute(Constants.ADMIN_STORE);
		model.addAttribute("currency",sessionStore.getCurrency());
		
		return "admin-orders-report-order";
	}
	
	
	@RequestMapping(value = "/admin/orders/collectOrder.html", method = RequestMethod.GET)
	public String collectBill(@RequestParam("id") Integer billId,Model model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// display menu
		setMenu(model, request);
		List<Order> dataStore = new ArrayList<>();
		List<CollectBill> datas = new ArrayList<>();
		try {
			dataStore = (List<Order>)request.getSession().getAttribute("STORE_ORDERDATA");
			String billIds ="";
			for(Order billMaster:dataStore){
				if(billIds.equals("")) {
					billIds = billMaster.getId() +"";
				}else{
					billIds +=","+billMaster.getId();
				}
			}
			
			datas = billService.collectOrder(billIds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("data",datas);
		
		MerchantStore sessionStore = (MerchantStore) request.getAttribute(Constants.ADMIN_STORE);
		model.addAttribute("currency",sessionStore.getCurrency());
		
		
		return "admin-orders-collect-order";
	}		
	
	
	
	
	private void setMenu(Model model, HttpServletRequest request) {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<>();
		activeMenus.put("order", "order");
		activeMenus.put("order-list", "order-list");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = menus.get("order");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}

}
