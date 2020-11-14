package com.salesmanager.shop.admin.controller.orders;

import java.math.BigDecimal;
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

import com.salesmanager.core.business.services.catalog.product.ProductService;
import com.salesmanager.core.business.services.order.OrderService;
import com.salesmanager.core.business.services.reference.country.BillItemService;
import com.salesmanager.core.business.services.reference.country.BillMasterService;
import com.salesmanager.core.business.services.system.ModuleConfigurationService;
import com.salesmanager.core.business.utils.ajax.AjaxPageableResponse;
import com.salesmanager.core.business.utils.ajax.AjaxResponse;
import com.salesmanager.core.model.catalog.product.BillMaster;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.relationship.BillItem;
import com.salesmanager.core.model.common.CriteriaOrderBy;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.BillMasterCriteria;
import com.salesmanager.core.model.order.BillMasterList;
import com.salesmanager.core.model.order.Order;
import com.salesmanager.core.model.order.orderproduct.OrderProductEx;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.system.IntegrationModule;
import com.salesmanager.shop.admin.controller.ControllerConstants;
import com.salesmanager.shop.admin.model.web.Menu;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.utils.DateUtil;
import com.salesmanager.shop.utils.LabelUtils;


/**
 * Manage order list
 * Manage search order
 * @author csamson 
 *
 */
@Controller
public class BillsController {
	
	@Inject
	BillMasterService billService;
	
	@Inject
	private OrderService orderService;
	
	@Inject
	private BillItemService billItemService;
	
	@Inject
	LabelUtils messages;

	@Inject
	private ProductService productService;
	
	@Inject
	protected ModuleConfigurationService moduleConfigurationService;
	 
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderControler.class);

	
	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value="/admin/bills/list.html", method=RequestMethod.GET)
	public String displayOrders(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		setMenu(model,request);

		//the list of orders is from page method
		
		return ControllerConstants.Tiles.Bill.bills;
		
		
	}


	@PreAuthorize("hasRole('ORDER')")
	@SuppressWarnings({ "unchecked", "unused"})
	@RequestMapping(value="/admin/bills/paging.html", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> pageOrders(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		

		AjaxPageableResponse resp = new AjaxPageableResponse();

		try {
			
			int startRow = Integer.parseInt(request.getParameter("_startRow"));
			int endRow = Integer.parseInt(request.getParameter("_endRow"));
			String	paymentModule = request.getParameter("paymentModule");
			String sku = request.getParameter("sku");
			String productName = request.getParameter("productName");
			
			String billIdRq = request.getParameter("id");
			String orderIdRq = request.getParameter("orderId");
			String statusRq = request.getParameter("status");
			
			
			
			BillMasterCriteria criteria = new BillMasterCriteria();
			criteria.setOrderBy(CriteriaOrderBy.DESC);
			criteria.setStartIndex(startRow);
			criteria.setMaxCount(endRow);
			if(!StringUtils.isBlank(sku)) {
				criteria.setSku(sku);
			}
			if(!StringUtils.isBlank(productName)) {
				criteria.setProductName(productName);
			}			
			
			if(!StringUtils.isBlank(billIdRq)) {
				criteria.setId(Integer.parseInt(billIdRq));
			}
			
			if(!StringUtils.isBlank(orderIdRq)) {
				criteria.setOrderId(Long.parseLong(orderIdRq));
			}
			
			if(!StringUtils.isBlank(statusRq)) {
				criteria.setStatus(statusRq);
			}			
			
			
			Language language = (Language)request.getAttribute("LANGUAGE");
			MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
			List<IntegrationModule> paymentModules = moduleConfigurationService.getIntegrationModules( "PAYMENT" );

			BillMasterList billList = billService.getListByStore2(store, criteria);
		
			if(billList.getBillMasters()!=null) {	
			
				for(BillMaster order : billList.getBillMasters()) {
					
					@SuppressWarnings("rawtypes")
					Map entry = new HashMap();
					entry.put("id", order.getId());
					entry.put("orderId", order.getOrder().getId());
					entry.put("sku", order.getSku());
					entry.put("productName", order.getProductName());
					//entry.put("customer", order.getBilling().getFirstName() + " " + order.getBilling().getLastName());
					//entry.put("amount", priceUtil.getAdminFormatedAmountWithCurrency(store,order.getTotal()));//todo format total
					entry.put("date", DateUtil.formatDate(order.getCreateAt()));
					entry.put("status", order.getStatus());
	
					entry.put("paymentModule", paymentModule );
					resp.addDataEntry(entry);				
					
				}
			}
			
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			

		
		} catch (Exception e) {
			LOGGER.error("Error while paging orders", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		String returnString = resp.toJSONString();

		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
	}
	

	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value="/admin/bills/viewBill.html", method=RequestMethod.GET)
	public String viewBill(@RequestParam("id") int billId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


			//display menu
			setMenu(model,request);
			
			com.salesmanager.shop.admin.model.orders.Order order = new com.salesmanager.shop.admin.model.orders.Order();
			Language language = (Language)request.getAttribute("LANGUAGE");
			
			BillMaster bill = billService.getById(billId);
			long orderId = bill.getOrder().getId();
			

			if(orderId>0) {	

				Order dbOrder = orderService.getById(orderId);	
				
				
				if( dbOrder.getDatePurchased() !=null ){
					order.setDatePurchased(DateUtil.formatDate(dbOrder.getDatePurchased()));
				}
				order.setOrder( dbOrder );
				order.setBilling( dbOrder.getBilling());
				order.setDelivery(dbOrder.getDelivery());	
	
					BigDecimal totalMoney = new BigDecimal("0");

					OrderProductEx ordernew = new OrderProductEx();
						Product dbProduct = productService.getByCode(bill.getSku(), language);
						ordernew.setProductName(bill.getProductName());
						ordernew.setSku(bill.getSku());
						ordernew.setCurrency(dbOrder.getCurrency());
						//ordernew.setProductQuantity(bean.getProductQuantity());
						//ordernew.setOneTimeCharge(bean.getOneTimeCharge());
						//ordernew.setTotal(bean.getOneTimeCharge().multiply(new BigDecimal(bean.getProductQuantity())));
						ordernew.setStatus(bill.getStatus());
						ordernew.setDescription(bill.getDescription());
						
						if(dbProduct!=null){
							List<OrderProductEx> proRelaList =new ArrayList<OrderProductEx>();
							OrderProductEx proRela = null;
							for(BillItem sBean : bill.getItems()){
								proRela =  new OrderProductEx();
								proRela.setSku(sBean.getCode());
								proRela.setProductName(sBean.getName());
								proRela.setCurrency(dbOrder.getCurrency());
								proRela.setProductQuantity(sBean.getQuantity()!=null?sBean.getQuantity().intValue():0);
								proRela.setOneTimeCharge(sBean.getPrice());
								proRela.setTotal(proRela.getOneTimeCharge().multiply(new BigDecimal(proRela.getProductQuantity())));
								totalMoney = totalMoney.add(proRela.getTotal()); 
								proRelaList.add(proRela);
							}
							ordernew.setRelationships(proRelaList);
						}

					
					model.addAttribute("dataEx",ordernew);
					model.addAttribute("order",order);
					model.addAttribute("totalMoney",totalMoney);				


			}	
			
			return "admin-bill-view";

		}
	
	
	private void setMenu(Model model, HttpServletRequest request) throws Exception {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("bills", "bills");
		activeMenus.put("order-list-bill", "order-list-bill");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");
		
		Menu currentMenu = (Menu)menus.get("bills");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}

}
