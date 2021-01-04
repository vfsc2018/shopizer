package com.salesmanager.shop.admin.controller.transactions;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.salesmanager.core.business.services.catalog.product.PricingService;
import com.salesmanager.core.business.services.payments.TransactionService;
import com.salesmanager.core.business.services.system.ModuleConfigurationService;
import com.salesmanager.core.business.utils.ajax.AjaxPageableResponse;
import com.salesmanager.core.business.utils.ajax.AjaxResponse;
import com.salesmanager.core.model.common.CriteriaOrderBy;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.payments.Transaction;
import com.salesmanager.core.model.payments.TransactionsCriteria;
import com.salesmanager.core.model.payments.TransactionsList;
import com.salesmanager.shop.admin.controller.ControllerConstants;
import com.salesmanager.shop.admin.model.web.Menu;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.utils.DateUtil;
import com.salesmanager.shop.utils.LabelUtils;

@Controller
@Scope("session")
public class TransactionsController {

	@Inject
	TransactionService transactionService;


	@Inject
	LabelUtils messages;
	
	@Inject
	PricingService pricingService;

	@Inject
	protected ModuleConfigurationService moduleConfigurationService;

	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionsController.class);


	@RequestMapping(value = "/admin/transactions/list.html", method = RequestMethod.GET)
	public String displayOrders(Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		setMenu(model, request);

		// the list of orders is from page method

		return ControllerConstants.Tiles.Transaction.Transactions;

	}


	@RequestMapping(value = "/admin/transactions/paging.html", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> pageBills(
			HttpServletRequest request, HttpServletResponse response,
			Locale locale) {

		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		AjaxPageableResponse resp = new AjaxPageableResponse();

		try {

			int startRow = Integer.parseInt(request.getParameter("_startRow"));
			int endRow = Integer.parseInt(request.getParameter("_endRow"));
			
//			String message = request.getParameter("message");
//			String topic = request.getParameter("topic");
			
			String transactionId = request.getParameter("transactionId");
			String	date = request.getParameter("transactionDate");
			String	detail = request.getParameter("transactionDetails");

			if(date!=null && date.length()!=10){
				return new ResponseEntity<>("{}",httpHeaders,HttpStatus.OK);
			}


			
			
			TransactionsCriteria criteria = new TransactionsCriteria();
			criteria.setOrderBy(CriteriaOrderBy.DESC);
			criteria.setStartIndex(startRow);
			criteria.setMaxCount(endRow);

			
			try {
				if(!StringUtils.isBlank(transactionId)) {
					criteria.setTransactionId(Long.parseLong(transactionId));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if(!StringUtils.isBlank(date)) {
				criteria.setDate(date);
			}

			if(!StringUtils.isBlank(detail)) {
				criteria.setDetail(detail);
			}
			
			MerchantStore store = (MerchantStore) request.getAttribute(Constants.ADMIN_STORE);

			TransactionsList list = transactionService.getListByStore2(store, criteria);

			
			if (list.getTransactions() != null) {

				resp.setTotalRow(list.getTotalCount());

				for (Transaction transaction : list.getTransactions()) {
					
					@SuppressWarnings("rawtypes")
					Map entry = new HashMap();
					entry.put("transactionId", transaction.getId());
					entry.put("transactionDate", DateUtil.formatTimeDate(transaction.getTransactionDate()));
					entry.put("transactionType", transaction.getTransactionType().name());
					entry.put("paymentType", transaction.getPaymentType().name());
					entry.put("transactionAmount", pricingService.getStringAmount(transaction.getAmount(), store));
					String details = transaction.getDetails();
					if(details==null) details = "{}";
					entry.put("transactionDetails", details);
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



	@RequestMapping(value = "/admin/transactions/viewTransactions.html", method = RequestMethod.GET)
	public String viewNotificaiton(@RequestParam("id") Long id, Model model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// display menu
		setMenu(model, request);

		Transaction bill = transactionService.getById(id);
	
		model.addAttribute("notification", bill);

		return "admin-transactions-view";
	}
	

	private void setMenu(Model model, HttpServletRequest request) {

		// display menu
		Map<String, String> activeMenus = new HashMap<>();
		//activeMenus.put("payment", "payment");
		activeMenus.put("transactions", "transactions");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");

		Menu currentMenu = menus.get("payment");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
		//

	}

}
