package com.salesmanager.shop.admin.controller.orders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesmanager.core.business.modules.email.Email;
import com.salesmanager.core.business.services.catalog.product.PricingService;
import com.salesmanager.core.business.services.catalog.product.ProductService;
import com.salesmanager.core.business.services.catalog.product.price.ProductPriceService;
import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.services.order.OrderService;
import com.salesmanager.core.business.services.order.orderproduct.OrderProductDownloadService;
import com.salesmanager.core.business.services.payments.PaymentService;
import com.salesmanager.core.business.services.payments.TransactionService;
import com.salesmanager.core.business.services.reference.country.BillItemService;
import com.salesmanager.core.business.services.reference.country.BillMasterService;
import com.salesmanager.core.business.services.reference.country.CountryService;
import com.salesmanager.core.business.services.reference.zone.ZoneService;
import com.salesmanager.core.business.services.system.EmailService;
import com.salesmanager.core.business.utils.CoreConfiguration;
import com.salesmanager.core.business.utils.ajax.AjaxPageableResponse;
import com.salesmanager.core.business.utils.ajax.AjaxResponse;
import com.salesmanager.core.model.catalog.product.BillMaster;
import com.salesmanager.core.model.catalog.product.Product;
import com.salesmanager.core.model.catalog.product.description.ProductDescription;
import com.salesmanager.core.model.catalog.product.price.ProductPrice;
import com.salesmanager.core.model.catalog.product.relationship.BillItem;
import com.salesmanager.core.model.catalog.product.relationship.ProductRelationship;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.Order;
import com.salesmanager.core.model.order.OrderTotal;
import com.salesmanager.core.model.order.orderproduct.BillDetailToSend;
import com.salesmanager.core.model.order.orderproduct.BillToSend;
import com.salesmanager.core.model.order.orderproduct.OrderProduct;
import com.salesmanager.core.model.order.orderproduct.OrderProductDownload;
import com.salesmanager.core.model.order.orderproduct.OrderProductEx;
import com.salesmanager.core.model.order.orderstatus.OrderStatusHistory;
import com.salesmanager.core.model.payments.PaymentType;
import com.salesmanager.core.model.payments.Transaction;
import com.salesmanager.core.model.reference.country.Country;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.reference.zone.Zone;
import com.salesmanager.shop.admin.controller.ControllerConstants;
import com.salesmanager.shop.admin.model.web.Menu;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.constants.EmailConstants;
import com.salesmanager.shop.utils.DateUtil;
import com.salesmanager.shop.utils.EmailTemplatesUtils;
import com.salesmanager.shop.utils.EmailUtils;
import com.salesmanager.shop.utils.LabelUtils;
import com.salesmanager.shop.utils.LocaleUtils;

/**
 * Manage order details
 * @author Carl Samson
 *
 */
@Controller
public class OrderControler {
	
private static final Logger LOGGER = LoggerFactory.getLogger(OrderControler.class);
	
	@Inject
	private LabelUtils messages;
	
	@Inject
	private OrderService orderService;
	
	@Inject
	CountryService countryService;
	
	@Inject
	ZoneService zoneService;
	
	@Inject
	PaymentService paymentService;
	
	@Inject
	CustomerService customerService;
	
	@Inject
	PricingService pricingService;
	
	@Inject
	TransactionService transactionService;
	
	@Inject
	EmailService emailService;
	
	@Inject
	private CoreConfiguration configuration;
	
	@Inject
	EmailTemplatesUtils emailTemplatesUtils;
	
	@Inject
	private EmailUtils emailUtils;
	
	@Inject
	private ProductService productService;
	
	@Inject
	private BillMasterService billMasterService;
	
	@Inject
	private BillItemService billItemService;
	
	
	@Inject
	private ProductPriceService productPriceService;
//	
//	@Inject
//	private com.salesmanager.core.business.services.catalog.product.ProductService productServiceAdmin;
	
	@Inject
	OrderProductDownloadService orderProdctDownloadService;
	
	private final static String ORDER_STATUS_TMPL = "email_template_order_status.ftl";
	
	
	
//	@RequestMapping(value = "/getCountry")
//    public ResponseEntity<Country> getCountry() {
//        
//        var c = new Country();
//        c.setName("France");
//        c.setPopulation(66984000);
//        
//        var headers = new HttpHeaders();
//        headers.add("Responded", "MyController");
//        
//        return ResponseEntity.accepted().headers(headers).body(c);
//    }
	
	
	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value="/admin/orders/sendBill.html", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> sendBill(
			@RequestParam("order.id") Long orderId,
			@RequestParam("order.customerId") Long customerId, 
			@RequestParam("sku") String[] skus,
			@RequestParam("productName") String[] productNames,
			@RequestParam("code") String[] code,
			@RequestParam("quantity") Double[] quantity,
			@RequestParam("oneTimeCharge") BigDecimal[] oneTimeCharge,
			@RequestParam("orderHistoryComment") String orderHistoryComment,
			@RequestParam("typeSave") int typeSave,
			HttpServletRequest request, 
			HttpServletResponse response) {
		
		
		
		
		AjaxResponse resp = new AjaxResponse();
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
	    
	    int i = 0 ;
		try {
			Order order = orderService.getById(orderId);
			//Call API
			
			
			if(typeSave==0){
			        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
					
			        String urlString = configuration.getProperty("config_shop_api_build_bill_to_vfsc");
			        RestTemplate restTemplate = new RestTemplate();
		           // create request body
			        
			       // check response
		
			        BillToSend billToSend = new BillToSend();
			        billToSend.setCode(orderId.toString());
			        billToSend.setDate(new Date());
			        billToSend.setDescription(orderHistoryComment);
		
			        Language language = (Language)request.getAttribute("LANGUAGE");
					List<BillDetailToSend> details = new ArrayList<BillDetailToSend>();
					BillDetailToSend sub1 = null;
					i = 0;
					for(String sku1:skus){
							sub1 = new BillDetailToSend();
							
							Product bean1111 = productService.getByCode(sku1, language);
							sub1.setProductName(productNames[i]);
							sub1.setProductId(bean1111.getId().toString());
							sub1.setProductCode(bean1111.getSku());
							sub1.setQuantity(quantity[i]+"");
							sub1.setSku(sku1);
							sub1.setUnit("");
							details.add(sub1);
						i++;
					}
					billToSend.setDetail(details);
		
					
			        ObjectMapper objectMapper = new ObjectMapper();
			        
			        String carAsString = objectMapper.writeValueAsString(billToSend);
			        System.out.println(carAsString);
			        ResponseEntity<String> res = restTemplate.postForEntity(urlString, carAsString, String.class);
			        if (res.getStatusCode() == HttpStatus.OK) {
			            System.out.println("Request Successful");
			            resp.setStatus(AjaxPageableResponse.RESPONSE_OPERATION_COMPLETED);
			        } else {
			            System.out.println("Request Failed");
			            resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			        }	        
			}
			
		} catch (Exception e) {
			LOGGER.error("Error while paging products", e);
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
		
	}
	
	
	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value="/admin/orders/buildBill.html", method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> buildBill(
			@RequestParam("order.id") Long orderId,
			@RequestParam("order.customerId") Long customerId, 
			@RequestParam("sku") String[] skus,
			@RequestParam("productName") String[] productNames,
			@RequestParam("code") String[] code,
			@RequestParam("quantity") Double[] quantity,
			@RequestParam("oneTimeCharge") BigDecimal[] oneTimeCharge,
			@RequestParam("orderHistoryComment") String orderHistoryComment,
			@RequestParam("order.status") String status,
			@RequestParam("typeSave") int typeSave,
			HttpServletRequest request, 
			HttpServletResponse response) {
		
		
		
		
		AjaxResponse resp = new AjaxResponse();
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
	    
	    int i = 0 ;
		try {
			Order order = orderService.getById(orderId);
			//Call API
			
			
			if(typeSave==0){
			        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
					
			        String urlString = configuration.getProperty("config_shop_api_build_bill_to_vfsc");
			        RestTemplate restTemplate = new RestTemplate();
		           // create request body
			        
			       // check response
		
			        BillToSend billToSend = new BillToSend();
			        billToSend.setCode(orderId.toString());
			        billToSend.setDate(new Date());
			        billToSend.setDescription(orderHistoryComment);
		
			        Language language = (Language)request.getAttribute("LANGUAGE");
					List<BillDetailToSend> details = new ArrayList<BillDetailToSend>();
					BillDetailToSend sub1 = null;
					i = 0;
					for(String sku1:skus){
							sub1 = new BillDetailToSend();
							
							Product bean1111 = productService.getByCode(sku1, language);
							sub1.setProductName(productNames[i]);
							sub1.setProductId(bean1111.getId().toString());
							sub1.setProductCode(bean1111.getSku());
							sub1.setQuantity(quantity[i]+"");
							sub1.setSku(sku1);
							sub1.setUnit("");
							details.add(sub1);
						i++;
					}
					billToSend.setDetail(details);
		
					
			        ObjectMapper objectMapper = new ObjectMapper();
			        
			        String carAsString = objectMapper.writeValueAsString(billToSend);
			        System.out.println(carAsString);
			        ResponseEntity<String> res = restTemplate.postForEntity(urlString, carAsString, String.class);
			        if (res.getStatusCode() == HttpStatus.OK) {
			            System.out.println("Request Successful");
			        } else {
			            System.out.println("Request Failed");
			        }	        
			        
			        if (res.getStatusCode() == HttpStatus.OK) {
			        		//click bt SaveBill    
			        		
							//insert BillMaster
							String tem="";
							BillMaster bill=null;
							i=0;
										for(String sku:skus){
											if(!tem.equals(sku)){
												bill = new BillMaster();
												//Insert
												bill.setSku(sku);
												bill.setProductName(productNames[i]);
												bill.setOrder(order);
												bill = billMasterService.saveAnnouncement(bill);
												//Update 
												BillItem sub = null;
												int j = 0;
												for(String sku1:skus){
													if(sku1.equals(bill.getSku())){
														sub = new BillItem();
														sub.setCode(sku1);
														sub.setName(productNames[j]);
														sub.setQuantity(quantity[j]);
														sub.setPrice(oneTimeCharge[j]);
														sub.setBillMaster(bill);
														billItemService.saveBillItem(sub);
													}
													j++;
												}
											}
											//Save
											tem = sku;
											i++;
										}
							
			        } else {
			            System.out.println("Request Failed");
						LOGGER.error("Pls check "+ urlString);
						resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			        }
			}else{
			  if(billMasterService.countByOrderId(orderId)==0){
		        BillToSend billToSend = new BillToSend();
		        billToSend.setCode(orderId.toString());
		        billToSend.setDate(new Date());
		        billToSend.setDescription(orderHistoryComment);
				String tem="";
				BillMaster bill=null;
				i=0;
				for(String sku:skus){
					if(!tem.equals(sku)){
						bill = new BillMaster();
						bill.setSku(sku);
						bill.setProductName(productNames[i]);
						bill.setOrder(order);
						bill.setStatus(status);
						bill.setDescription(orderHistoryComment);
						bill = billMasterService.saveAnnouncement(bill);
						BillItem sub = null;
						int j = 0;
						for(String sku1:skus){
							if(sku1.equals(bill.getSku())){
								sub = new BillItem();
								sub.setCode(sku1);
								sub.setName(productNames[j]);
								sub.setQuantity(quantity[j]);
								sub.setPrice(oneTimeCharge[j]);
								sub.setBillMaster(bill);
								billItemService.saveBillItem(sub);
							}
							j++;
						}
					}
					tem = sku;
					i++;
				}
			  }
			}
			resp.setStatus(AjaxPageableResponse.RESPONSE_OPERATION_COMPLETED);
	        
		} catch (Exception e) {
			LOGGER.error("Error while paging products", e);
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}
		
		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString,httpHeaders,HttpStatus.OK);
		
	}
	
	
	@PreAuthorize("hasRole('ORDER')")
	@ResponseBody
	@RequestMapping(value="/admin/orders/validationCode.html", method=RequestMethod.POST)
	public OrderProductEx getData(@RequestParam("orderId") long orderId,
			@RequestParam("quantity") int quantity,
			HttpServletRequest request, 
			HttpServletResponse response) {
		
		String code = request.getParameter("code");
		
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
	    OrderProductEx beantemp = null;
	    Order dbOrder = null;
	    try {
	    	Language language = (Language)request.getAttribute("LANGUAGE");
	    	if(orderId>0){
	    		dbOrder = orderService.getById(orderId);	
	    	}
	    	
	    	Product bean = productService.getByCode(code, language);
	    	
	    	if(bean!=null){
	    		beantemp = new OrderProductEx();
		    	beantemp.setSku(bean.getSku());
		    	beantemp.setCurrency(dbOrder.getCurrency());
		    	
		    	
		    	beantemp.setProductQuantity(quantity);//default
				
				ProductPrice price = productPriceService.getProductPriceByid(bean.getId());
				
				beantemp.setOneTimeCharge(price!=null?price.getProductPriceAmount():new BigDecimal(0));
				beantemp.setTotal(beantemp.getOneTimeCharge().multiply(new BigDecimal(beantemp.getProductQuantity())));
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return beantemp;
		
	}
	/*ducdv5*/
	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value="/admin/orders/prepareBill.html", method=RequestMethod.GET)
	public String displayOrderBill(@RequestParam("id") long orderId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		
		
		long vcheck = 0 ;

		//display menu
		setMenu(model,request);
		List<OrderProductEx> listOrderNew= new ArrayList<OrderProductEx>();   
		com.salesmanager.shop.admin.model.orders.Order order = new com.salesmanager.shop.admin.model.orders.Order();
		Language language = (Language)request.getAttribute("LANGUAGE");
		List<Country> countries = countryService.getCountries(language);
		if(orderId>0) {	
			
			
			MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
			
			
			
		
		
			Order dbOrder = orderService.getById(orderId);

			if(dbOrder==null) {
				return "redirect:/admin/orders/list.html";
			}
			
			
			if(dbOrder.getMerchant().getId().intValue()!=store.getId().intValue()) {
				return "redirect:/admin/orders/list.html";
			}
			
			
			order.setId( orderId );
		
			if( dbOrder.getDatePurchased() !=null ){
				order.setDatePurchased(DateUtil.formatDate(dbOrder.getDatePurchased()));
			}
			
			Long customerId = dbOrder.getCustomerId();
			
			if(customerId!=null && customerId>0) {
			
				try {
					
					Customer customer = customerService.getById(customerId);
					if(customer!=null) {
						model.addAttribute("customer",customer);
					}
					
					
				} catch(Exception e) {
					LOGGER.error("Error while getting customer for customerId " + customerId, e);
				}
			
			}
			
			order.setOrder( dbOrder );
			order.setBilling( dbOrder.getBilling() );
			order.setDelivery(dbOrder.getDelivery() );
			
			
			//Check exits order in BillMaster
			vcheck = billMasterService.countByOrderId(orderId);
			if(vcheck==0){
					OrderProductEx ordernew=null;
					for(OrderProduct bean : dbOrder.getOrderProducts()){
						
						ordernew = new OrderProductEx();
						Product dbProduct = productService.getByCode(bean.getSku(), language);
						ordernew.setProductName(bean.getProductName());
						ordernew.setSku(bean.getSku());
						ordernew.setCurrency(dbOrder.getCurrency());
						ordernew.setProductQuantity(bean.getProductQuantity());
						ordernew.setOneTimeCharge(bean.getOneTimeCharge());
						ordernew.setTotal(bean.getOneTimeCharge().multiply(new BigDecimal(bean.getProductQuantity())));
						
		
						if(dbProduct!=null){
							List<OrderProductEx> proRelaList =new ArrayList<OrderProductEx>();
							OrderProductEx proRela = null;
							for(ProductRelationship sBean : dbProduct.getRelationships()){
								
								proRela =  new OrderProductEx();
								
								proRela.setSku(sBean.getRelatedProduct().getSku());
		
								for(ProductDescription bean1 : sBean.getRelatedProduct().getDescriptions()){
									if(bean1.getLanguage().getCode().equals(language.getCode())) proRela.setProductName(bean1.getName());
								}
								proRela.setCurrency(dbOrder.getCurrency());
								
								proRela.setProductQuantity(sBean.getQuantity()!=null?sBean.getQuantity().intValue():0);
								
								ProductPrice price = productPriceService.getProductPriceByid(sBean.getRelatedProduct().getId());
								
								proRela.setOneTimeCharge(price!=null?price.getProductPriceAmount():new BigDecimal(0));
								
								proRela.setTotal(proRela.getOneTimeCharge().multiply(new BigDecimal(proRela.getProductQuantity())));
								
								proRelaList.add(proRela);
								
							}
							ordernew.setRelationships(proRelaList);
						}
						//add to list
						listOrderNew.add(ordernew);
						model.addAttribute("dataEx",listOrderNew);
					}
			}else{
				//view form edit bill
				List<BillMaster> listdata = billMasterService.findByOrderId(orderId);
				
				
				
				model.addAttribute("dataEx",listdata);
			}
			//get capturable
			if(dbOrder.getPaymentType().name() != PaymentType.MONEYORDER.name()) {
				Transaction capturableTransaction = transactionService.getCapturableTransaction(dbOrder);
				if(capturableTransaction!=null) {
					model.addAttribute("capturableTransaction",capturableTransaction);
				}
			}
			
			//get refundable
			if(dbOrder.getPaymentType().name() != PaymentType.MONEYORDER.name()) {
				Transaction refundableTransaction = transactionService.getRefundableTransaction(dbOrder);
				if(refundableTransaction!=null) {
						model.addAttribute("capturableTransaction",null);//remove capturable
						model.addAttribute("refundableTransaction",refundableTransaction);
				}
			}
			
			List<OrderProductDownload> orderProductDownloads = orderProdctDownloadService.getByOrderId(order.getId());
			if(CollectionUtils.isNotEmpty(orderProductDownloads)) {
				model.addAttribute("downloads",orderProductDownloads);
			}
			
		}	
		
		model.addAttribute("countries", countries);
		model.addAttribute("order",order);
		if(vcheck >0 ){
			return  ControllerConstants.Tiles.Order.ordersBillEdit;	
		}else{
			return  ControllerConstants.Tiles.Order.ordersBill;
		}
		
		
	}

	
	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value="/admin/orders/printBill.html", method=RequestMethod.GET)
	public String printBill(@RequestParam("id") long orderId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		//display menu
		setMenu(model,request);
		List<OrderProductEx> listOrderNew= new ArrayList<OrderProductEx>();   
		com.salesmanager.shop.admin.model.orders.Order order = new com.salesmanager.shop.admin.model.orders.Order();
		Language language = (Language)request.getAttribute("LANGUAGE");

		if(orderId>0) {	
			
			
			
			MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
			
			

		
		
			Order dbOrder = orderService.getById(orderId);

			if(dbOrder==null) {
				return "redirect:/admin/orders/list.html";
			}
			
			
			if(dbOrder.getMerchant().getId().intValue()!=store.getId().intValue()) {
				return "redirect:/admin/orders/list.html";
			}
			Long customerId = dbOrder.getCustomerId();
			
			if(customerId!=null && customerId>0) {
			
				try {
					
					Customer customer = customerService.getById(customerId);
					if(customer!=null) {
						model.addAttribute("customer",customer);
//						dbOrder.getBilling().getFirstName()
					}
					
					
				} catch(Exception e) {
					LOGGER.error("Error while getting customer for customerId " + customerId, e);
				}
			
			}
			
			
			order.setId( orderId );
		
			if( dbOrder.getDatePurchased() !=null ){
				order.setDatePurchased(DateUtil.formatDate(dbOrder.getDatePurchased()));
			}

			order.setOrder( dbOrder );
			order.setBilling( dbOrder.getBilling() );
			order.setDelivery(dbOrder.getDelivery() );
			BigDecimal totalMoney = new BigDecimal("0");

					OrderProductEx ordernew=null;
					for(OrderProduct bean : dbOrder.getOrderProducts()){
						
						ordernew = new OrderProductEx();
						Product dbProduct = productService.getByCode(bean.getSku(), language);
						ordernew.setProductName(bean.getProductName());
						ordernew.setSku(bean.getSku());
						ordernew.setCurrency(dbOrder.getCurrency());
						ordernew.setProductQuantity(bean.getProductQuantity());
						ordernew.setOneTimeCharge(bean.getOneTimeCharge());
						ordernew.setTotal(bean.getOneTimeCharge().multiply(new BigDecimal(bean.getProductQuantity())));
						
		
						if(dbProduct!=null){
							List<OrderProductEx> proRelaList =new ArrayList<OrderProductEx>();
							OrderProductEx proRela = null;
							for(ProductRelationship sBean : dbProduct.getRelationships()){
								
								proRela =  new OrderProductEx();
								
								proRela.setSku(sBean.getRelatedProduct().getSku());
		
								for(ProductDescription bean1 : sBean.getRelatedProduct().getDescriptions()){
									if(bean1.getLanguage().getCode().equals(language.getCode())) proRela.setProductName(bean1.getName());
								}
								proRela.setCurrency(dbOrder.getCurrency());
								
								proRela.setProductQuantity(sBean.getQuantity()!=null?sBean.getQuantity().intValue():0);
								
								ProductPrice price = productPriceService.getProductPriceByid(sBean.getRelatedProduct().getId());
								
								proRela.setOneTimeCharge(price!=null?price.getProductPriceAmount():new BigDecimal(0));
								
								proRela.setTotal(proRela.getOneTimeCharge().multiply(new BigDecimal(proRela.getProductQuantity())));
								totalMoney = totalMoney.add(proRela.getTotal()); 
								proRelaList.add(proRela);
								
							}
							ordernew.setRelationships(proRelaList);
						}
						//add to list
						listOrderNew.add(ordernew);
						model.addAttribute("dataEx",listOrderNew);
						model.addAttribute("order",order);
						model.addAttribute("totalMoney",totalMoney);
					}
			
			
		}	
		
		return "admin-orders-print-bill";

	}
	
	
	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value="/admin/orders/editOrder.html", method=RequestMethod.GET)
	public String displayOrderEdit(@RequestParam("id") long orderId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		return displayOrder(orderId,model,request,response);

	}
	
	
	@PreAuthorize("hasRole('ORDER')")
	private String displayOrder(Long orderId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		//display menu
		setMenu(model,request);
		   
		com.salesmanager.shop.admin.model.orders.Order order = new com.salesmanager.shop.admin.model.orders.Order();
		Language language = (Language)request.getAttribute("LANGUAGE");
		List<Country> countries = countryService.getCountries(language);
		if(orderId!=null && orderId!=0) {		//edit mode		
			
			
			MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
			
			
			
			Set<OrderProduct> orderProducts = null;
			Set<OrderTotal> orderTotal = null;
			Set<OrderStatusHistory> orderHistory = null;
		
			Order dbOrder = orderService.getById(orderId);

			if(dbOrder==null) {
				return "redirect:/admin/orders/list.html";
			}
			
			
			if(dbOrder.getMerchant().getId().intValue()!=store.getId().intValue()) {
				return "redirect:/admin/orders/list.html";
			}
			
			
			order.setId( orderId );
		
			if( dbOrder.getDatePurchased() !=null ){
				order.setDatePurchased(DateUtil.formatDate(dbOrder.getDatePurchased()));
			}
			
			Long customerId = dbOrder.getCustomerId();
			
			if(customerId!=null && customerId>0) {
			
				try {
					
					Customer customer = customerService.getById(customerId);
					if(customer!=null) {
						model.addAttribute("customer",customer);
					}
					
					
				} catch(Exception e) {
					LOGGER.error("Error while getting customer for customerId " + customerId, e);
				}
			
			}
			
			order.setOrder( dbOrder );
			order.setBilling( dbOrder.getBilling() );
			order.setDelivery(dbOrder.getDelivery() );
			

			orderProducts = dbOrder.getOrderProducts();
			orderTotal = dbOrder.getOrderTotal();
			orderHistory = dbOrder.getOrderHistory();
			
			//get capturable
			if(dbOrder.getPaymentType().name() != PaymentType.MONEYORDER.name()) {
				Transaction capturableTransaction = transactionService.getCapturableTransaction(dbOrder);
				if(capturableTransaction!=null) {
					model.addAttribute("capturableTransaction",capturableTransaction);
				}
			}
			
			
			//get refundable
			if(dbOrder.getPaymentType().name() != PaymentType.MONEYORDER.name()) {
				Transaction refundableTransaction = transactionService.getRefundableTransaction(dbOrder);
				if(refundableTransaction!=null) {
						model.addAttribute("capturableTransaction",null);//remove capturable
						model.addAttribute("refundableTransaction",refundableTransaction);
				}
			}

			
			List<OrderProductDownload> orderProductDownloads = orderProdctDownloadService.getByOrderId(order.getId());
			if(CollectionUtils.isNotEmpty(orderProductDownloads)) {
				model.addAttribute("downloads",orderProductDownloads);
			}
			
		}	
		
		model.addAttribute("countries", countries);
		model.addAttribute("order",order);
		return  ControllerConstants.Tiles.Order.ordersEdit;
	}
	

	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value="/admin/orders/save.html", method=RequestMethod.POST)
	public String saveOrder(@Valid @ModelAttribute("order") com.salesmanager.shop.admin.model.orders.Order entityOrder, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {
		
		String email_regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
		Pattern pattern = Pattern.compile(email_regEx);
		
		Language language = (Language)request.getAttribute("LANGUAGE");
		List<Country> countries = countryService.getCountries(language);
		model.addAttribute("countries", countries);
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		//set the id if fails
		entityOrder.setId(entityOrder.getOrder().getId());
		
		model.addAttribute("order", entityOrder);
		
		Set<OrderProduct> orderProducts = new HashSet<OrderProduct>();
		Set<OrderTotal> orderTotal = new HashSet<OrderTotal>();
		Set<OrderStatusHistory> orderHistory = new HashSet<OrderStatusHistory>();
		
		Date date = new Date();
		if(!StringUtils.isBlank(entityOrder.getDatePurchased() ) ){
			try {
				date = DateUtil.getDate(entityOrder.getDatePurchased());
			} catch (Exception e) {
				ObjectError error = new ObjectError("datePurchased",messages.getMessage("message.invalid.date", locale));
				result.addError(error);
			}
			
		} else{
			date = null;
		}
		 

		if(!StringUtils.isBlank(entityOrder.getOrder().getCustomerEmailAddress() ) ){
			 java.util.regex.Matcher matcher = pattern.matcher(entityOrder.getOrder().getCustomerEmailAddress());
			 
			 if(!matcher.find()) {
				ObjectError error = new ObjectError("customerEmailAddress",messages.getMessage("Email.order.customerEmailAddress", locale));
				result.addError(error);
			 }
		}else{
			ObjectError error = new ObjectError("customerEmailAddress",messages.getMessage("NotEmpty.order.customerEmailAddress", locale));
			result.addError(error);
		}

		 
		if( StringUtils.isBlank(entityOrder.getOrder().getBilling().getFirstName() ) ){
			 ObjectError error = new ObjectError("billingFirstName", messages.getMessage("NotEmpty.order.billingFirstName", locale));
			 result.addError(error);
		}
		
		if( StringUtils.isBlank(entityOrder.getOrder().getBilling().getFirstName() ) ){
			 ObjectError error = new ObjectError("billingLastName", messages.getMessage("NotEmpty.order.billingLastName", locale));
			 result.addError(error);
		}
		 
		if( StringUtils.isBlank(entityOrder.getOrder().getBilling().getAddress() ) ){
			 ObjectError error = new ObjectError("billingAddress", messages.getMessage("NotEmpty.order.billingStreetAddress", locale));
			 result.addError(error);
		}
		 
		if( StringUtils.isBlank(entityOrder.getOrder().getBilling().getCity() ) ){
			 ObjectError error = new ObjectError("billingCity",messages.getMessage("NotEmpty.order.billingCity", locale));
			 result.addError(error);
		}
		 
		if( entityOrder.getOrder().getBilling().getZone()==null){
			if( StringUtils.isBlank(entityOrder.getOrder().getBilling().getState())){
				 ObjectError error = new ObjectError("billingState",messages.getMessage("NotEmpty.order.billingState", locale));
				 result.addError(error);
			}
		}
		 
		if( StringUtils.isBlank(entityOrder.getOrder().getBilling().getPostalCode() ) ){
			 ObjectError error = new ObjectError("billingPostalCode", messages.getMessage("NotEmpty.order.billingPostCode", locale));
			 result.addError(error);
		}
		
		com.salesmanager.core.model.order.Order newOrder = orderService.getById(entityOrder.getOrder().getId() );
		
		
		//get capturable
		if(newOrder.getPaymentType().name() != PaymentType.MONEYORDER.name()) {
			Transaction capturableTransaction = transactionService.getCapturableTransaction(newOrder);
			if(capturableTransaction!=null) {
				model.addAttribute("capturableTransaction",capturableTransaction);
			}
		}
		
		
		//get refundable
		if(newOrder.getPaymentType().name() != PaymentType.MONEYORDER.name()) {
			Transaction refundableTransaction = transactionService.getRefundableTransaction(newOrder);
			if(refundableTransaction!=null) {
					model.addAttribute("capturableTransaction",null);//remove capturable
					model.addAttribute("refundableTransaction",refundableTransaction);
			}
		}
	
	
		if (result.hasErrors()) {
			//  somehow we lose data, so reset Order detail info.
			entityOrder.getOrder().setOrderProducts( orderProducts);
			entityOrder.getOrder().setOrderTotal(orderTotal);
			entityOrder.getOrder().setOrderHistory(orderHistory);
			
			return ControllerConstants.Tiles.Order.ordersEdit;
		/*	"admin-orders-edit";  */
		}
		
		OrderStatusHistory orderStatusHistory = new OrderStatusHistory();		



		
		Country deliveryCountry = countryService.getByCode( entityOrder.getOrder().getDelivery().getCountry().getIsoCode()); 
		Country billingCountry  = countryService.getByCode( entityOrder.getOrder().getBilling().getCountry().getIsoCode()) ;
		Zone billingZone = null;
		Zone deliveryZone = null;
		if(entityOrder.getOrder().getBilling().getZone()!=null) {
			billingZone = zoneService.getByCode(entityOrder.getOrder().getBilling().getZone().getCode());
		}
		
		if(entityOrder.getOrder().getDelivery().getZone()!=null) {
			deliveryZone = zoneService.getByCode(entityOrder.getOrder().getDelivery().getZone().getCode());
		}

		newOrder.setCustomerEmailAddress(entityOrder.getOrder().getCustomerEmailAddress() );
		newOrder.setStatus(entityOrder.getOrder().getStatus() );		
		
		newOrder.setDatePurchased(date);
		newOrder.setLastModified( new Date() );
		
		if(!StringUtils.isBlank(entityOrder.getOrderHistoryComment() ) ) {
			orderStatusHistory.setComments( entityOrder.getOrderHistoryComment() );
			orderStatusHistory.setCustomerNotified(1);
			orderStatusHistory.setStatus(entityOrder.getOrder().getStatus());
			orderStatusHistory.setDateAdded(new Date() );
			orderStatusHistory.setOrder(newOrder);
			newOrder.getOrderHistory().add( orderStatusHistory );
			entityOrder.setOrderHistoryComment( "" );
		}		
		
		newOrder.setDelivery( entityOrder.getOrder().getDelivery() );
		newOrder.setBilling( entityOrder.getOrder().getBilling() );
		newOrder.setCustomerAgreement(entityOrder.getOrder().getCustomerAgreement());
		
		newOrder.getDelivery().setCountry(deliveryCountry );
		newOrder.getBilling().setCountry(billingCountry );	
		
		if(billingZone!=null) {
			newOrder.getBilling().setZone(billingZone);
		}
		
		if(deliveryZone!=null) {
			newOrder.getDelivery().setZone(deliveryZone);
		}
		
		orderService.saveOrUpdate(newOrder);
		entityOrder.setOrder(newOrder);
		entityOrder.setBilling(newOrder.getBilling());
		entityOrder.setDelivery(newOrder.getDelivery());
		model.addAttribute("order", entityOrder);
		
		Long customerId = newOrder.getCustomerId();
		
		if(customerId!=null && customerId>0) {
		
			try {
				
				Customer customer = customerService.getById(customerId);
				if(customer!=null) {
					model.addAttribute("customer",customer);
				}
				
				
			} catch(Exception e) {
				LOGGER.error("Error while getting customer for customerId " + customerId, e);
			}
		
		}

		List<OrderProductDownload> orderProductDownloads = orderProdctDownloadService.getByOrderId(newOrder.getId());
		if(CollectionUtils.isNotEmpty(orderProductDownloads)) {
			model.addAttribute("downloads",orderProductDownloads);
		}
		
		
		/** 
		 * send email if admin posted orderHistoryComment
		 * 
		 * **/
		
		if(StringUtils.isBlank(entityOrder.getOrderHistoryComment())) {
		
			try {
				
				// Customer customer = customerService.getById(newOrder.getCustomerId());
				// Language lang = store.getDefaultLanguage();
				// if(customer!=null) {
				// 	lang = customer.getDefaultLanguage();
				// }
				
				// Locale customerLocale = LocaleUtils.getLocale(lang);

				// StringBuilder customerName = new StringBuilder();
				// customerName.append(newOrder.getBilling().getFirstName()).append(" ").append(newOrder.getBilling().getLastName());
				
				
				// Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(request.getContextPath(), store, messages, customerLocale);
				// templateTokens.put(EmailConstants.EMAIL_CUSTOMER_NAME, customerName.toString());
				// templateTokens.put(EmailConstants.EMAIL_TEXT_ORDER_NUMBER, messages.getMessage("email.order.confirmation", new String[]{String.valueOf(newOrder.getId())}, customerLocale));
				// templateTokens.put(EmailConstants.EMAIL_TEXT_DATE_ORDERED, messages.getMessage("email.order.ordered", new String[]{entityOrder.getDatePurchased()}, customerLocale));
				// templateTokens.put(EmailConstants.EMAIL_TEXT_STATUS_COMMENTS, messages.getMessage("email.order.comments", new String[]{entityOrder.getOrderHistoryComment()}, customerLocale));
				// templateTokens.put(EmailConstants.EMAIL_TEXT_DATE_UPDATED, messages.getMessage("email.order.updated", new String[]{DateUtil.formatDate(new Date())}, customerLocale));

				
				// Email email = new Email();
				// email.setFrom(store.getStorename());
				// email.setFromEmail(store.getStoreEmailAddress());
				// email.setSubject(messages.getMessage("email.order.status.title",new String[]{String.valueOf(newOrder.getId())},customerLocale));
				// email.setTo(entityOrder.getOrder().getCustomerEmailAddress());
				// email.setTemplateName(ORDER_STATUS_TMPL);
				// email.setTemplateTokens(templateTokens);
	
	
				
				// emailService.sendHtmlEmail(store, email);
			
			} catch (Exception e) {
				LOGGER.error("Cannot send email to customer",e);
			}
			
		}
		
		model.addAttribute("success","success");

		
		return  ControllerConstants.Tiles.Order.ordersEdit;
	    /*	"admin-orders-edit";  */
	}

	private void setMenu(Model model, HttpServletRequest request) throws Exception {
	
		//display menu
		Map<String,String> activeMenus = new HashMap<String,String>();
		activeMenus.put("order", "order");
		activeMenus.put("order-list", "order-list");
		
		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>)request.getAttribute("MENUMAP");

		model.addAttribute("activeMenus",activeMenus);
		
		Menu currentMenu = (Menu)menus.get("order");
		model.addAttribute("currentMenu",currentMenu);
		model.addAttribute("activeMenus",activeMenus);
		//
		
	}

}
