package com.salesmanager.shop.admin.controller.notifications;

import java.math.BigDecimal;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.services.notifications.NotificationsService;
import com.salesmanager.core.business.services.system.ModuleConfigurationService;
import com.salesmanager.core.business.utils.ajax.AjaxPageableResponse;
import com.salesmanager.core.business.utils.ajax.AjaxResponse;
import com.salesmanager.core.model.catalog.product.Notifications;
import com.salesmanager.core.model.common.CriteriaOrderBy;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.NotificationsCriteria;
import com.salesmanager.core.model.order.NotificationsList;
import com.salesmanager.shop.admin.controller.ControllerConstants;
import com.salesmanager.shop.admin.model.web.Menu;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.utils.LabelUtils;

/**
 * Manage order list Manage search order
 * 
 * @author csamson
 *
 */
@Controller
@Scope("session")
public class NotificationsController {

	@Inject
	NotificationsService billService;


	@Inject
	LabelUtils messages;


	@Inject
	protected ModuleConfigurationService moduleConfigurationService;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(NotificationsController.class);

	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value = "/admin/notifications/list.html", method = RequestMethod.GET)
	public String displayOrders(Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		setMenu(model, request);

		// the list of orders is from page method

		return ControllerConstants.Tiles.Notification.notifications;

	}

	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value = "/admin/notifications/paging.html", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> pageBills(
			HttpServletRequest request, HttpServletResponse response,
			Locale locale) {

		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		AjaxPageableResponse resp = new AjaxPageableResponse();

		try {

			int startRow = Integer.parseInt(request.getParameter("_startRow"));
			int endRow = Integer.parseInt(request.getParameter("_endRow"));
			
			String messager = request.getParameter("messager");
			String topic = request.getParameter("topic");
			
			String customerName = request.getParameter("customer");
			String read = request.getParameter("read");
			
			
			NotificationsCriteria criteria = new NotificationsCriteria();
			criteria.setOrderBy(CriteriaOrderBy.DESC);
			criteria.setStartIndex(startRow);
			criteria.setMaxCount(endRow);

		
			if(!StringUtils.isBlank(customerName)) {
				criteria.setCustomerName(customerName);
			}
			if(!StringUtils.isBlank(messager)){
				criteria.setMessager(messager);
			}
			if(!StringUtils.isBlank(topic)) {
				criteria.setTopic(topic);
			}

			if (!StringUtils.isBlank(topic)) {
				criteria.setTopic(topic);
			}
			if (!StringUtils.isBlank(read)) {
				criteria.setRead(read);
			}

			MerchantStore store = (MerchantStore) request.getAttribute(Constants.ADMIN_STORE);

			NotificationsList billList = billService.getListByStore2(store, criteria);

			
			if (billList.getNotificationss() != null) {

				resp.setTotalRow(billList.getTotalCount());

				BigDecimal totalBill = null;
				
				for (Notifications bill : billList.getNotificationss()) {
					totalBill = BigDecimal.valueOf(0);
					@SuppressWarnings("rawtypes")
					Map entry = new HashMap();
					entry.put("id", bill.getId());
					
					entry.put("customer", bill.getCustomer().getBilling().getFirstName()); 
							
					if (StringUtils.isBlank(bill.getMessage())) {
						entry.put("messager", bill.getMessage());
					} else {
						entry.put("topic", bill.getTopic());
					}

					if (bill.getRead()>0) {
						entry.put("read", bill.getRead());
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
	

	

	@PreAuthorize("hasRole('ORDER')")
	@RequestMapping(value = "/admin/notifications/viewBill.html", method = RequestMethod.GET)
	public String viewBill(@RequestParam("id") Long billId, Model model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// display menu
		setMenu(model, request);

		return "admin-bill-view";

	}


	

	

	private void setMenu(Model model, HttpServletRequest request)
			throws Exception {

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
