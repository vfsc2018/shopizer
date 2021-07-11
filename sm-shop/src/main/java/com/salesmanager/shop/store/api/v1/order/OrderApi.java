package com.salesmanager.shop.store.api.v1.order;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.salesmanager.core.business.repositories.vouchercode.VoucherCodeRepository;
import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.services.order.OrderService;
import com.salesmanager.core.business.services.order.bill.BillMasterService;
import com.salesmanager.core.business.services.shoppingcart.ShoppingCartService;
import com.salesmanager.core.business.services.voucher.VoucherService;
import com.salesmanager.core.business.utils.CacheUtils;
import com.salesmanager.core.model.order.BillMaster;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.Order;
import com.salesmanager.core.model.order.orderstatus.OrderStatus;
import com.salesmanager.core.model.order.orderstatus.OrderStatusHistory;
import com.salesmanager.core.model.payments.TransactionType;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.shoppingcart.ShoppingCart;
import com.salesmanager.core.model.vouchercode.VoucherCode;
import com.salesmanager.shop.model.customer.ReadableCustomer;
import com.salesmanager.shop.model.order.v0.ReadableOrder;
import com.salesmanager.shop.model.order.v0.ReadableOrderList;
import com.salesmanager.shop.model.order.v1.PersistableAnonymousOrder;
import com.salesmanager.shop.model.order.v1.PersistableOrder;
import com.salesmanager.shop.populator.customer.ReadableCustomerPopulator;
import com.salesmanager.shop.store.api.exception.ResourceNotFoundException;
import com.salesmanager.shop.store.api.exception.ServiceRuntimeException;
import com.salesmanager.shop.store.controller.customer.facade.CustomerFacade;
import com.salesmanager.shop.store.controller.order.facade.OrderFacade;
import com.salesmanager.shop.store.security.user.JWTUser;
import com.salesmanager.shop.utils.LocaleUtils;
import com.salesmanager.shop.utils.NotificationUtils;

import com.salesmanager.shop.model.shop.CacheNamesImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1")
@Api(tags = { "Order management resource (Order Management Api)" })
@SwaggerDefinition(tags = { @Tag(name = "Order management resource", description = "Manage orders") })
public class OrderApi {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderApi.class);

	@Inject
	private CustomerService customerService;

	@Inject
	private NotificationUtils notificationUtils;

	@Inject
	private OrderFacade orderFacade;

	@Inject
	private OrderService orderService;

	@Inject
	private BillMasterService billMasterService;

	@Inject
	private ShoppingCartService shoppingCartService;

	@Autowired
	private CustomerFacade customerFacade;

	@Inject
	private VoucherCodeRepository voucherCodeRepository;

	@Inject
	private VoucherService voucherService;

	@Inject
	private CacheUtils cache;
	
	// private static final String DEFAULT_ORDER_LIST_COUNT = "25";

	/**
	 * Get a list of orders for a given customer accept request parameter
	 * 'start' start index for count accept request parameter 'max' maximum
	 * number count, otherwise returns all Used for administrators
	 *
	 * @param response
	 * @return
	 * @throws Exception
	 */
	// @RequestMapping(value = { "/private/orders/customers/{id}" }, method = RequestMethod.GET)
	// @ResponseStatus(HttpStatus.OK)
	// @ResponseBody
	// @ApiImplicitParams({ @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT"),
	// 		@ApiImplicitParam(name = "lang", dataType = "string", defaultValue = "vi") })
	// public ReadableOrderList list(@PathVariable final Long id,
	// 		@RequestParam(value = "start", required = false) Integer start,
	// 		@RequestParam(value = "count", required = false) Integer count, @ApiIgnore MerchantStore merchantStore,
	// 		@ApiIgnore Language language, HttpServletResponse response) throws Exception {

	// 	Customer customer = customerService.getById(id);

	// 	if (customer == null) {
	// 		LOGGER.error("Customer is null for id " + id);
	// 		response.sendError(404, "Customer is null for id " + id);
	// 		return null;
	// 	}

	// 	if (start == null) {
	// 		start = 0;
	// 	}
	// 	if (count == null) {
	// 		count = 100;
	// 	}

	// 	ReadableCustomer readableCustomer = new ReadableCustomer();
	// 	ReadableCustomerPopulator customerPopulator = new ReadableCustomerPopulator();
	// 	customerPopulator.populate(customer, readableCustomer, merchantStore, language);

	// 	ReadableOrderList returnList = orderFacade.getReadableOrderList(merchantStore, customer, start, count,
	// 			language);

	// 	List<ReadableOrder> orders = returnList.getOrders();

	// 	if (!CollectionUtils.isEmpty(orders)) {
	// 		for (ReadableOrder order : orders) {
	// 			order.setCustomer(readableCustomer);
	// 		}
	// 	}

	// 	return returnList;
	// }

	/**
	 * List orders for authenticated customers
	 *
	 * @param start
	 * @param count
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = { "/private/customer/orders" }, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT"),
		@ApiImplicitParam(name = "lang", dataType = "string", defaultValue = "vi") })
	@Cacheable(value="CACHE_CUSTOMER_ORDER", key = "'orders_' + #request.userPrincipal.principal.id + '_' + #page")
	public ReadableOrderList list(@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "5") Integer count, @ApiIgnore MerchantStore merchantStore,
			@ApiIgnore Language language, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Principal principal = request.getUserPrincipal();
		String userName = principal.getName();

		System.out.println("who os the username ? " + userName);

		Customer customer = customerService.getByNick(userName);

		if (customer == null) {
			response.sendError(401, "Error while listing orders, customer not authorized");
			return null;
		}

		ReadableCustomer readableCustomer = new ReadableCustomer();
		ReadableCustomerPopulator customerPopulator = new ReadableCustomerPopulator();
		customerPopulator.populate(customer, readableCustomer, merchantStore, language);

		ReadableOrderList returnList = orderFacade.getReadableOrderList(merchantStore, customer, page, count, language);

		if (returnList == null) {
			returnList = new ReadableOrderList();
		}

		List<ReadableOrder> orders = returnList.getOrders();
		if (!CollectionUtils.isEmpty(orders)) {
			for (ReadableOrder order : orders) {
				if(order.getTotal()!=null && order.getTotal().getValue().doubleValue()<0){
					order.getTotal().setValue(new BigDecimal(0));
				}
				order.setCustomer(readableCustomer);
			}
		}
		return returnList;
	}

	/**
	 * This method returns list of all the orders for a store.This is not
	 * bound to any specific stores and will get list of all the orders
	 * available for this instance
	 *
	 * @param start
	 * @param count
	 * @return List of orders
	 * @throws Exception
	 */
	// @RequestMapping(value = { "/private/orders" }, method = RequestMethod.GET)
	// @ResponseStatus(HttpStatus.OK)
	// @ResponseBody
	// public ReadableOrderList list(
	// 		@RequestParam(value = "count", required = false, defaultValue = DEFAULT_ORDER_LIST_COUNT) Integer count,
	// 		@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
	// 		@RequestParam(value = "name", required = false) String name,
	// 		@RequestParam(value = "id", required = false) Long id,
	// 		@RequestParam(value = "status", required = false) String status,
	// 		@RequestParam(value = "phone", required = false) String phone,
	// 		@RequestParam(value = "email", required = false) String email,
	// 		@ApiIgnore MerchantStore merchantStore,
	// 		@ApiIgnore Language language) {
		
		
	// 	//long startTime = System.nanoTime();


	// 	OrderCriteria orderCriteria = new OrderCriteria();
	// 	orderCriteria.setPageSize(count);
	// 	orderCriteria.setStartPage(page);

	// 	orderCriteria.setCustomerName(name);
	// 	orderCriteria.setCustomerPhone(phone);
	// 	orderCriteria.setStatus(status);
	// 	orderCriteria.setEmail(email);
	// 	orderCriteria.setId(id);


	// 	String user = authorizationUtils.authenticatedUser();
	// 	authorizationUtils.authorizeUser(user, Stream.of(Constants.GROUP_SUPERADMIN, Constants.GROUP_ADMIN,
	// 			Constants.GROUP_ADMIN_ORDER, Constants.GROUP_ADMIN_RETAIL).collect(Collectors.toList()), merchantStore);

	// 	ReadableOrderList orders = orderFacade.getReadableOrderList(orderCriteria, merchantStore);
		
	// 	/**
	// 	long endTime = System.nanoTime();
		
	// 	long timeElapsed = endTime - startTime;

	// 	System.out.println("Execution time in milliseconds : " +
	// 							timeElapsed / 1000000);
	// 							**/
		
	// 	return orders;

	// }

	/**
	 * Order details
	 * @param id
	 * @param merchantStore
	 * @param language
	 * @return
	 */
	// @RequestMapping(value = { "/private/orders/{id}" }, method = RequestMethod.GET)
	// @ResponseStatus(HttpStatus.OK)
	// @ResponseBody
	// @ApiImplicitParams({ @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT"),
	// 		@ApiImplicitParam(name = "lang", dataType = "string", defaultValue = "vi") })
	// public ReadableOrder get(
	// 		@PathVariable final Long id, 
	// 		@ApiIgnore MerchantStore merchantStore,
	// 		@ApiIgnore Language language) {
		
	// 	String user = authorizationUtils.authenticatedUser();
	// 	authorizationUtils.authorizeUser(user, Stream.of(Constants.GROUP_SUPERADMIN, Constants.GROUP_ADMIN,
	// 			Constants.GROUP_ADMIN_ORDER, Constants.GROUP_ADMIN_RETAIL).collect(Collectors.toList()), merchantStore);


	// 	ReadableOrder order = orderFacade.getReadableOrder(id, merchantStore, language);

	// 	return order;
	// }

	/**
	 * Get a given order by id
	 *
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = { "/private/customer/orders/{id}" }, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiImplicitParams({ @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT"),
			@ApiImplicitParam(name = "lang", dataType = "string", defaultValue = "vi") })
	@Cacheable(value="CACHE_CUSTOMER_ORDER", key = "'order_' + #id")
	public ReadableOrder getOrder(@PathVariable final Long id, @ApiIgnore MerchantStore merchantStore,
			@ApiIgnore Language language, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Principal principal = request.getUserPrincipal();
		String userName = principal.getName();

		Customer customer = customerService.getByNick(userName);

		if (customer == null) {
			response.sendError(401, "Error while performing checkout customer not authorized");
			return null;
		}

		ReadableOrder order = orderFacade.getReadableOrder(id, merchantStore, language);

		if (order == null) {
			response.sendError(404, "Order is null for id " + id);
			return null;
		}

		if (order.getCustomer() == null || order.getCustomer().getId() == null|| order.getCustomer().getId().longValue() != customer.getId().longValue()) {
			response.sendError(404, "Order is null for customer " + principal);
			return null;
		}
		if(order.getTotal()!=null && order.getTotal().getValue().doubleValue()<0){
			order.getTotal().setValue(new BigDecimal(0));
		}
		
		List<BillMaster> bills = billMasterService.findByOrderId(id);

		order.setBills(bills);

		return order;
	}

	@RequestMapping(value = { "/private/customer/order/confirm/bill" }, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT"),
		@ApiImplicitParam(name = "lang", dataType = "string", defaultValue = "vi") })
	
	public List<BillMaster> getOrderMustConfirm(@ApiIgnore MerchantStore merchantStore,
			@ApiIgnore Language language, HttpServletRequest request) throws Exception {
		
			UsernamePasswordAuthenticationToken principal = (UsernamePasswordAuthenticationToken)request.getUserPrincipal();
		
			JWTUser customer =  (JWTUser)principal.getPrincipal();
			Long id = customer.getId();

		if (id == null) {
			return Collections.emptyList();
		}

		String keyName = CacheNamesImpl.KEY_CUSTOMER_REMIND_ORDER + id;
		Long updateTime = (Long)cache.get(keyName);
		if(updateTime!=null){
			long diff = System.currentTimeMillis()-updateTime.longValue();
			long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diff);
			if(diffMinutes<100){
				return Collections.emptyList();
			}
		}
		cache.put(System.currentTimeMillis(), keyName);
		
		//List<OrderStatus> status = List.of(OrderStatus.PROCESSING, OrderStatus.PROCESSED, OrderStatus.DELIVERING, OrderStatus.DELIVERED);
		List<OrderStatus> status = new ArrayList<>();
		status.add(OrderStatus.PROCESSING);
		status.add(OrderStatus.PROCESSED);
		status.add(OrderStatus.DELIVERING);
		status.add(OrderStatus.DELIVERED);
		
		return billMasterService.findLast(id, status, PageRequest.of(0, 1));
	}

	@PatchMapping("/private/customer/order/{id}/DONE/{billId}")
	public ResponseEntity<?> setDone(@PathVariable final Long id, @PathVariable final Long billId, HttpServletRequest request) throws Exception {
		
		UsernamePasswordAuthenticationToken principal = (UsernamePasswordAuthenticationToken)request.getUserPrincipal();

		JWTUser customer =  (JWTUser)principal.getPrincipal();

		BillMaster bill = billMasterService.getById(billId);

		if (bill == null || !bill.getOrder().getId().equals(id) || !bill.getOrder().getCustomerId().equals(customer.getId())){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		//List<OrderStatus> status = List.of(OrderStatus.PROCESSING, OrderStatus.PROCESSED, OrderStatus.DELIVERING, OrderStatus.DELIVERED);
		List<OrderStatus> status = new ArrayList<>();
		status.add(OrderStatus.PROCESSING);
		status.add(OrderStatus.PROCESSED);
		status.add(OrderStatus.DELIVERING);
		status.add(OrderStatus.DELIVERED);
		if(status.contains(bill.getStatus())){
			bill.setStatus(OrderStatus.DONE);
			billMasterService.save(bill);
			return new ResponseEntity<>("{\"orderId\":" + id + ",\"billId\":" + billId + ",\"status\":\"" + OrderStatus.DONE.name() + "\"}", HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PatchMapping("/private/customer/order/{id}/CANCELED")
	public ResponseEntity<?> setOrderCanceled(@PathVariable final Long id, HttpServletRequest request) throws Exception {
		
		UsernamePasswordAuthenticationToken principal = (UsernamePasswordAuthenticationToken)request.getUserPrincipal();

		JWTUser customer =  (JWTUser)principal.getPrincipal();

		com.salesmanager.core.model.order.Order order = orderService.getById(id);

		if (order == null || !order.getCustomerId().equals(customer.getId())){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if(OrderStatus.ORDERED == order.getStatus()){
			OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
			orderStatusHistory.setComments("Customer decided to cancel this order");
			orderStatusHistory.setCustomerNotified(customer.getId());
			orderStatusHistory.setStatus(OrderStatus.ORDERED);
			orderStatusHistory.setDateAdded(new Date());
			orderStatusHistory.setOrder(order);
			order.setStatus(OrderStatus.CANCELED);
			order.getOrderHistory().add(orderStatusHistory);
			orderService.saveOrUpdate(order);
			return new ResponseEntity<>("{\"orderId\":" + id + ",\"status\":\"" + OrderStatus.CANCELED.name() + "\"}", HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	/**
	 * Action for performing a checkout on a given shopping cart
	 *
	 * @param id
	 * @param order
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = { "/private/cart/{code}/checkout" })
	@Transactional
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT"),
		@ApiImplicitParam(name = "lang", dataType = "string", defaultValue = "vi") })
	public PersistableOrder checkout(@PathVariable final String code, @Valid @RequestBody PersistableOrder order,
			@ApiIgnore MerchantStore merchantStore, @ApiIgnore Language language, HttpServletRequest request,
			HttpServletResponse response, Locale locale) throws Exception {

		try {

			Principal principal = request.getUserPrincipal();
			String userName = principal.getName();

			Customer customer = customerService.getByNick(userName);

			if (customer == null) {
				response.sendError(401, "Error while performing checkout customer not authorized");
				return null;
			}
			if(StringUtils.isEmpty(order.getCode())) order.setCode(null);
			if(StringUtils.isEmpty(order.getSecurecode())) order.setSecurecode(null);

			if ((order.getCode() == null && order.getSecurecode()!=null) || (order.getCode() != null && order.getSecurecode()==null) ) {
				response.sendError(401, "Error voucher code");
				return null;
			}

			ShoppingCart cart = shoppingCartService.getByCode(code, merchantStore);
			if (cart == null) {
				throw new ResourceNotFoundException("Cart code " + code + " does not exist");
			}
			
			order.setShoppingCartId(cart.getId());
			order.setCustomerId(customer.getId());
			order.setCurrency("VND");
			order.getPayment().setTransactionType(TransactionType.INIT.name());
			order.getPayment().setPaymentToken("***");

			VoucherCode voucherCode = null;
			if(order.getCode()!=null && order.getSecurecode()!=null){
				voucherCode =  voucherService.getVoucher(order.getCode(), order.getSecurecode());
				if(voucherCode==null){
					response.sendError(401, "Error voucher code with order");
					return null;
				}
				order.setVoucherCode(voucherCode);
				order.getPayment().setPaymentToken(order.getCode());
			}
			
			Order modelOrder = orderFacade.processOrder(order, customer, merchantStore, language, locale);
			Long orderId = modelOrder.getId();
			order.setId(orderId);

			if(voucherCode!=null){
				voucherCode.setCustomer(customer);
				voucherCode.setOrder(modelOrder);
				voucherCode.setUsed(new Date());
				voucherCode.setSecurecode(order.getSecurecode());
				voucherCodeRepository.save(voucherCode);
			}

			order.setVoucherCode(null);
			notificationUtils.createAfterOrder(customer, modelOrder);
			return order;

		} catch (Exception e) {
			LOGGER.error("Error while processing checkout", e);
			try {
				response.sendError(503, "Error while processing checkout " + e.getMessage());
			} catch (Exception ignore) {
			}
			return null;
		}
	}

	// @PatchMapping(value = { "/private/order/{id}/token" })
	// @ResponseStatus(HttpStatus.OK)
	// @ResponseBody
	// @ApiImplicitParams({ 
	// 	@ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT"),
	// 	@ApiImplicitParam(name = "lang", dataType = "string", defaultValue = "vi") })
	// public PersistableOrder setPaymentToken(@PathVariable final Long id,
	// 		@ApiIgnore MerchantStore merchantStore, @ApiIgnore Language language, HttpServletRequest request,
	// 		HttpServletResponse response, Locale locale) throws Exception {

	// 	try {
	// 		Principal principal = request.getUserPrincipal();
	// 		String userName = principal.getName();

	// 		Customer customer = customerService.getByNick(userName);

	// 		if (customer == null) {
	// 			response.sendError(401, "Error while performing checkout customer not authorized");
	// 			return null;
	// 		}

	// 		Order order = orderService.getById(id);
	// 		if (order == null) {
	// 			throw new ResourceNotFoundException("Order id: " + id + " does not exist");
	// 		}
	// 		order.getPay
	// 		order.getPayment().setPaymentToken("***");

	// 		return order;

	// 	} catch (Exception e) {
	// 		LOGGER.error("Error while processing checkout", e);
	// 		try {
	// 			response.sendError(503, "Error while processing checkout " + e.getMessage());
	// 		} catch (Exception ignore) {
	// 		}
	// 		return null;
	// 	}
	// }

	@RequestMapping(value = { "/cart/{code}/checkout" }, method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT"),
		@ApiImplicitParam(name = "lang", dataType = "string", defaultValue = "vi") })
	public PersistableOrder checkout(
			@PathVariable final String code,
			@Valid @RequestBody PersistableAnonymousOrder order, 
			@ApiIgnore MerchantStore merchantStore,
			@ApiIgnore Language language) {

		Validate.notNull(order.getCustomer(), "Customer must not be null");

		ShoppingCart cart;
		try {
			cart = shoppingCartService.getByCode(code, merchantStore);

			if (cart == null) {
				throw new ResourceNotFoundException("Cart code " + code + " does not exist");
			}

			Customer customer = new Customer();
			
			customer = customerFacade.populateCustomerModel(customer, order.getCustomer(), merchantStore, language);

			order.setShoppingCartId(cart.getId());

			Order modelOrder = orderFacade.processOrder(order, customer, merchantStore, language, LocaleUtils.getLocale(language));
			Long orderId = modelOrder.getId();
			order.setId(orderId);
			// set customer id
			order.getCustomer().setId(modelOrder.getCustomerId());

			// hash payment token
			order.getPayment().setPaymentToken("***");
			notificationUtils.createAfterOrder(customer, modelOrder);
			return order;

		} catch (Exception e) {
			throw new ServiceRuntimeException("Error during checkout " + e.getMessage(), e);
		}

	}
	
	// @RequestMapping(value = { "/private/orders/{id}/customer" }, method = RequestMethod.PATCH)
	// @ResponseStatus(HttpStatus.OK)
	// @ResponseBody
	// @ApiImplicitParams({ 
	// 		@ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT"),
	// 		@ApiImplicitParam(name = "lang", dataType = "string", defaultValue = "vi") })
	// public void updateOrderCustomer(
	// 		@PathVariable final Long id,
	// 		@Valid @RequestBody PersistableCustomer orderCustomer, 
	// 		@ApiIgnore MerchantStore merchantStore,
	// 		@ApiIgnore Language language) {
		
	// 	String user = authorizationUtils.authenticatedUser();
	// 	authorizationUtils.authorizeUser(user, Stream.of(Constants.GROUP_SUPERADMIN, Constants.GROUP_ADMIN,
	// 			Constants.GROUP_ADMIN_ORDER, Constants.GROUP_ADMIN_RETAIL).collect(Collectors.toList()), merchantStore);

		
	// 	orderFacade.updateOrderCustomre(id, orderCustomer, merchantStore);
	// 	return;
		
	// }
}
