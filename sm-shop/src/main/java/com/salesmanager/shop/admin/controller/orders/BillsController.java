package com.salesmanager.shop.admin.controller.orders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import com.salesmanager.core.business.utils.ProductPriceUtils;
import com.salesmanager.core.business.utils.ajax.AjaxPageableResponse;
import com.salesmanager.core.business.utils.ajax.AjaxResponse;
import com.salesmanager.core.model.catalog.product.BillMaster;
import com.salesmanager.core.model.catalog.product.relationship.BillItem;
import com.salesmanager.core.model.common.CriteriaOrderBy;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.BillMasterCriteria;
import com.salesmanager.core.model.order.BillMasterList;
import com.salesmanager.core.model.order.CollectBill;
import com.salesmanager.core.model.order.Order;
import com.salesmanager.core.model.order.orderproduct.OrderProductEx;
import com.salesmanager.core.model.order.orderstatus.OrderStatus;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.admin.controller.ControllerConstants;
import com.salesmanager.shop.admin.model.web.Menu;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.utils.DateUtil;
import com.salesmanager.shop.utils.LabelUtils;

/**
 * Manage order list Manage search order
 * 
 * @author csamson
 *
 */
@Controller
@Scope("session")
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

	private static final Logger LOGGER = LoggerFactory
			.getLogger(OrderControler.class);

	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value = "/admin/bills/list.html", method = RequestMethod.GET)
	public String displayOrders(Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		setMenu(model, request);

		// the list of orders is from page method

		return ControllerConstants.Tiles.Bill.bills;

	}

	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value = "/admin/bills/paging.html", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> pageBills(
			HttpServletRequest request, HttpServletResponse response,
			Locale locale) {

		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		AjaxPageableResponse resp = new AjaxPageableResponse();

		try {

			int startRow = Integer.parseInt(request.getParameter("_startRow"));
			int endRow = Integer.parseInt(request.getParameter("_endRow"));
			String paymentModule = request.getParameter("paymentModule");
			String sku = request.getParameter("sku");
			String productName = request.getParameter("productName");
			String customerName = request.getParameter("customer");
			String billIdRq = request.getParameter("id");
			String orderIdRq = request.getParameter("orderId");
			String statusRq = request.getParameter("status");
			String phone = request.getParameter("phone");
			String date = request.getParameter("date");
			String address = request.getParameter("address");
			
			if(date!=null && date.length()!=10){
				return new ResponseEntity<>("{}",httpHeaders,HttpStatus.OK);
			}
			
			BillMasterCriteria criteria = new BillMasterCriteria();
			criteria.setOrderBy(CriteriaOrderBy.DESC);
			criteria.setStartIndex(startRow);
			criteria.setMaxCount(endRow);

			if(!StringUtils.isBlank(date)) {
				criteria.setDate(date);
			}
			if(!StringUtils.isBlank(customerName)) {
				criteria.setCustomerName(customerName);
			}
			if(!StringUtils.isBlank(address)){
				criteria.setAddress(address);
			}
			if(!StringUtils.isBlank(phone)) {
				criteria.setPhone(phone);
			}

			if (!StringUtils.isBlank(sku)) {
				criteria.setSku(sku);
			}
			if (!StringUtils.isBlank(productName)) {
				criteria.setProductName(productName);
			}

			if (!StringUtils.isBlank(billIdRq)) {
				criteria.setId(Long.parseLong(billIdRq));
			}

			if (!StringUtils.isBlank(orderIdRq)) {
				criteria.setOrderId(Long.parseLong(orderIdRq));
			}

			if (!StringUtils.isBlank(statusRq)) {
				criteria.setStatus(statusRq.toUpperCase());
			}

			// Language language = (Language) request.getAttribute("LANGUAGE");
			MerchantStore store = (MerchantStore) request.getAttribute(Constants.ADMIN_STORE);
			// List<IntegrationModule> paymentModules = moduleConfigurationService.getIntegrationModules("PAYMENT");

			BillMasterList billList = billService.getListByStore2(store, criteria);
			request.getSession().setAttribute("STORE_BILLDATA",billList.getBillMasters());
			
			if (billList.getBillMasters() != null) {

				resp.setTotalRow(billList.getTotalCount());

				BigDecimal totalBill = null;
				
				for (BillMaster bill : billList.getBillMasters()) {
					totalBill = BigDecimal.valueOf(0);
					@SuppressWarnings("rawtypes")
					Map entry = new HashMap();
					entry.put("id", bill.getId());
					entry.put("orderId", bill.getOrder().getId());

					// BigDecimal total = BigDecimal.valueOf(0);
					if (bill.getItems() != null) {
						for (BillItem item : bill.getItems()) {
							if (item.getParentId()!=null && item.getParentId() > 0) {
								BigDecimal total = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
								totalBill = totalBill.add(total);
							}
						}
						ProductPriceUtils price = new ProductPriceUtils();
						entry.put("total", price.getAdminFormatedAmount(store, totalBill));
					} else {
						entry.put("total", 0);
					}
					entry.put("customer", bill.getOrder().getBilling().getFirstName()); // + " " +
												// bill.getOrder().getBilling().getLastName());
					if (StringUtils.isBlank(bill.getPhone())) {
						entry.put("phone", bill.getOrder().getBilling().getTelephone());
					} else {
						entry.put("phone", bill.getPhone());
					}

					if (StringUtils.isBlank(bill.getAddress())) {
						entry.put("address", bill.getOrder().getBilling().getAddress());
					} else {
						entry.put("address", bill.getAddress());
					}
					entry.put("date", DateUtil.formatDate(bill.getDateExported()));
					entry.put("status", bill.getStatus());

					entry.put("paymentModule", paymentModule);
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
	

	

	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value = "/admin/bills/viewBill.html", method = RequestMethod.GET)
	public String viewBill(@RequestParam("id") Long billId, Model model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// display menu
		setMenu(model, request);

		com.salesmanager.shop.admin.model.orders.Order order = new com.salesmanager.shop.admin.model.orders.Order();

		BillMaster bill = billService.getById(billId);
		long orderId = bill.getOrder().getId();

		if (orderId > 0) {

			Order dbOrder = orderService.getById(orderId);

			order.setId(dbOrder.getId());
			if (dbOrder.getDatePurchased() != null) {
				order.setDatePurchased(DateUtil.formatDate(dbOrder
						.getDatePurchased()));
			}
			order.setOrder(dbOrder);
			order.setBilling(dbOrder.getBilling());
			order.setDelivery(dbOrder.getDelivery());

			Double totalMoney = 0.0;

			OrderProductEx ordernew = new OrderProductEx();
			ordernew.setId(bill.getId());
			ordernew.setCurrency(dbOrder.getCurrency());
			ordernew.setDateExported(DateUtil.formatDate(bill.getDateExported()));
			ordernew.setStatus(bill.getStatus());
			ordernew.setDescription(bill.getDescription());
		
			ordernew.setPhone(bill.getPhone());
			ordernew.setAddress(bill.getAddress());
			
			
			List<OrderProductEx> proRelaList = new ArrayList<>();
			OrderProductEx proRela = null;

			for (BillItem sBean : bill.getItems()) {
				proRela = new OrderProductEx();
				proRela.setId(sBean.getId());
				proRela.setParentId(sBean.getParentId());
				proRela.setSku(sBean.getCode());
				proRela.setProductName(sBean.getName());
				proRela.setCurrency(dbOrder.getCurrency());
				proRela.setProductQuantity(sBean.getQuantity());
				proRela.setOneTimeCharge(sBean.getPrice().intValue());
				proRela.setTotal(proRela.getOneTimeCharge().intValue()
						* proRela.getProductQuantity());
				totalMoney = totalMoney + proRela.getTotal();
				proRelaList.add(proRela);
			}
			ordernew.setRelationships(proRelaList);

			model.addAttribute("orderStatusList", Arrays.asList(OrderStatus.values()));
			model.addAttribute("dataEx", ordernew);
			model.addAttribute("order", order);
			model.addAttribute("totalMoney", totalMoney);

		}

		return "admin-bill-view";

	}

	@RequestMapping(value = "/admin/bills/buildBill.html", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> buildBill(
			@RequestParam("id") Long id, @RequestParam("orderId") Long orderId,
			@RequestParam("itemId") Long[] itemIds,
			@RequestParam("code") String[] code,
			@RequestParam("quantity") Double[] quantity,
			@RequestParam("oneTimeCharge") BigDecimal[] oneTimeCharge,
			@RequestParam("description") String description,
			@RequestParam("dateExported") String dateExported,
			@RequestParam("phone") String phone,
			@RequestParam("address") String address,
			@RequestParam("status") String status,
			@RequestParam("typeSave") int typeSave, HttpServletRequest request,
			HttpServletResponse response) {

		Language language = (Language) request.getAttribute("LANGUAGE");

		AjaxResponse resp = new AjaxResponse();
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		Date date = null;
		if (StringUtils.isNotBlank(dateExported)) {
			try {
				date = DateUtil.getDate(dateExported);
			} catch (Exception e) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorMessage(e);
			}
		}
		if(date!=null){
			try {
				Order order = orderService.getById(orderId);
				// Call API
				BillMaster bill = billService.getById(id);
				bill.setOrder(order);
				bill.setStatus(status);
				bill.setDescription(description);
				bill.setPhone(phone);
				bill.setAddress(address);
				bill.setDateExported(date);

				bill = billService.saveAnnouncement(bill);
				
				if(itemIds!=null){
					int j = 0;
					for (Long itemId : itemIds) {
						BillItem sub = billItemService.getById(itemId);
						sub.setCode(code[j]);
						sub.setName(productService.getByCode(sub.getCode(), language).getProductDescription().getName());
						sub.setQuantity(quantity[j]);
						sub.setPrice(oneTimeCharge[j]);
						sub.setBillMaster(bill);
						billItemService.saveBillItem(sub);
						j++;
					}
				}

			} catch (Exception e) {
				LOGGER.error("Error while paging products", e);
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorMessage(e);
			}
		}

		String returnString = resp.toJSONString();
		return new ResponseEntity<>(returnString, httpHeaders,HttpStatus.OK);

	}

	

	@RequestMapping(value = "/admin/bills/reportBill.html", method = RequestMethod.GET)
	public String reportBill(@RequestParam("id") Integer billId,Model model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// display menu
		setMenu(model, request);
		List<BillMaster> dataStore = new ArrayList<>();
		try {
			dataStore = (List<BillMaster>)request.getSession().getAttribute("STORE_BILLDATA");
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("data",dataStore);
		
		MerchantStore sessionStore = (MerchantStore) request.getAttribute(Constants.ADMIN_STORE);
		model.addAttribute("currency",sessionStore.getCurrency());
		
		return "admin-orders-report-bill";
	}
	
	
	@RequestMapping(value = "/admin/bills/collectBill.html", method = RequestMethod.GET)
	public String collectBill(@RequestParam("id") Integer billId,Model model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// display menu
		setMenu(model, request);
		List<BillMaster> dataStore = new ArrayList<>();
		List<CollectBill> datas = new ArrayList<>();
		try {
			dataStore = (List<BillMaster>)(request.getSession().getAttribute("STORE_BILLDATA"));
			String billIds ="";
			for(BillMaster billMaster:dataStore){
				if(billIds.equals("")) {
					billIds = billMaster.getId() +"";
				}else{
					billIds +=","+billMaster.getId();
				}
			}
			
			datas = billService.collectBill(billIds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("data",datas);
		
		MerchantStore sessionStore = (MerchantStore) request.getAttribute(Constants.ADMIN_STORE);
		model.addAttribute("currency",sessionStore.getCurrency());
		
		
		return "admin-orders-collect-bill";
	}	
	
	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value = "/admin/bills/printBill.html", method = RequestMethod.GET)
	public String printBill(@RequestParam("id") Long billId, Model model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// display menu
		setMenu(model, request);

		com.salesmanager.shop.admin.model.orders.Order order = new com.salesmanager.shop.admin.model.orders.Order();

		BillMaster billMaster = billService.getById(billId);
		if (billMaster.getOrder().getId() > 0) {
			Order dbOrder = orderService.getById(billMaster.getOrder().getId());
			if (dbOrder.getDatePurchased() != null) {
				order.setDatePurchased(DateUtil.formatDate(dbOrder
						.getDatePurchased()));
			}
			order.setOrder(dbOrder);
			order.setBilling(dbOrder.getBilling());
			order.setDelivery(dbOrder.getDelivery());

			// List<BillItem> items = billItemService.getItemByBillId(billId);
			// System.out.println("Data:::::::::::::"+items.size());
			// Set<BillItem> targetSet = new HashSet<BillItem>(items);
			// billMaster.setItems(targetSet);
			model.addAttribute("dataEx", billMaster);
			System.out.println("Data:::::::::::::"
					+ billMaster.getItems().size());
			model.addAttribute("order", order);

		}

		return "admin-orders-print-bill";

	}

	private void setMenu(Model model, HttpServletRequest request) {

		// display menu
		Map<String, String> activeMenus = new HashMap<>();
		activeMenus.put("bills", "bills");
		activeMenus.put("order-list-bill", "order-list-bill");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request
				.getAttribute("MENUMAP");

		Menu currentMenu = (Menu) menus.get("bills");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
		//

	}

}
