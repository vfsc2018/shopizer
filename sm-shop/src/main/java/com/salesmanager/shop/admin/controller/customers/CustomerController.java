package com.salesmanager.shop.admin.controller.customers;

import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.services.customer.attribute.CustomerAttributeService;
import com.salesmanager.core.business.services.customer.attribute.CustomerOptionService;
import com.salesmanager.core.business.services.customer.attribute.CustomerOptionSetService;
import com.salesmanager.core.business.services.customer.attribute.CustomerOptionValueService;
import com.salesmanager.core.business.services.reference.country.CountryService;
import com.salesmanager.core.business.services.reference.language.LanguageService;
import com.salesmanager.core.business.services.reference.zone.ZoneService;
// import com.salesmanager.core.business.services.system.EmailService;
import com.salesmanager.core.business.services.user.GroupService;
import com.salesmanager.core.business.utils.CoreConfiguration;
import com.salesmanager.core.business.utils.ProductPriceUtils;
import com.salesmanager.core.business.utils.ajax.AjaxPageableResponse;
import com.salesmanager.core.business.utils.ajax.AjaxResponse;
import com.salesmanager.core.model.common.Billing;
import com.salesmanager.core.model.common.CriteriaOrderBy;
import com.salesmanager.core.model.common.Delivery;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.customer.CustomerCriteria;
import com.salesmanager.core.model.customer.CustomerList;
import com.salesmanager.core.model.customer.Wallet;
import com.salesmanager.core.model.customer.attribute.CustomerAttribute;
import com.salesmanager.core.model.customer.attribute.CustomerOptionSet;
import com.salesmanager.core.model.customer.attribute.CustomerOptionType;
import com.salesmanager.core.model.customer.attribute.CustomerOptionValueDescription;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.country.Country;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.core.model.reference.zone.Zone;
import com.salesmanager.core.model.user.Group;
import com.salesmanager.core.model.user.GroupType;
import com.salesmanager.shop.admin.model.customer.attribute.CustomerOption;
import com.salesmanager.shop.admin.model.customer.attribute.CustomerOptionValue;
import com.salesmanager.shop.admin.model.web.Menu;
import com.salesmanager.shop.constants.Constants;
import com.salesmanager.shop.populator.customer.ReadableCustomerOptionPopulator;
import com.salesmanager.shop.store.controller.customer.facade.CustomerFacade;
import com.salesmanager.shop.utils.DateUtil;
// import com.salesmanager.shop.utils.EmailUtils;
import com.salesmanager.shop.utils.LabelUtils;
// import com.salesmanager.shop.utils.SessionUtil;
import com.salesmanager.shop.utils.SmsUtils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
// import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

@Controller
public class CustomerController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
	
	private static final String CUSTOMER_ID_PARAMETER = "customer";
	
	@Inject
	private CoreConfiguration configuration;
	
	@Inject
	private LabelUtils messages;
	
	@Inject
	private GroupService groupService;
	
	@Inject
	private CustomerService customerService;

	@Inject
	private CustomerFacade customerFacade;
	
	@Inject
	private CustomerOptionService customerOptionService;
	
	@Inject
	private CustomerOptionValueService customerOptionValueService;
	
	@Inject
	private CustomerOptionSetService customerOptionSetService;
	
	@Inject
	private CountryService countryService;
	
	@Inject
	private ZoneService zoneService;
	
	@Inject
	private LanguageService languageService;
	
	@Inject
	private CustomerAttributeService customerAttributeService;

	@Inject
	private ProductPriceUtils priceUtil;
	
	// @Inject
	// private EmailService emailService;
	
	// @Inject
	// private EmailUtils emailUtils;
	
	private String getGroupShare(Long id){
		List<Customer> groupShare = customerService.getByShare(id);
		if(groupShare!=null && !groupShare.isEmpty()){
			String ids = "";
			for(Customer c: groupShare){
				ids = "#" + c.getId() + " ";
			}
			return ids;
		}
		return null;
	}
	/**
	 * Customer details
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value="/admin/customers/customer.html", method=RequestMethod.GET)
	public String displayCustomer(Long id, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
			
		//display menu
		this.setMenu(model, request);
		
		//get groups
		List<Group> groups = new ArrayList<>();
		List<Group> userGroups = groupService.listGroup(GroupType.CUSTOMER);
		for(Group group : userGroups) {
			groups.add(group);
		}
		
		model.addAttribute("groups",groups);
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		List<Language> languages = languageService.getLanguages();

		model.addAttribute("languages",languages);
		
		Customer customer = null;
		
		//if request.attribute contains id then get this customer from customerService
		if(id!=null && id!=0) {//edit mode
			
			//get from DB
			customer = customerService.getById(id);
			if(customer==null) {
				return "redirect:/admin/customers/list.html";
			}
			
			if(customer.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
				return "redirect:/admin/customers/list.html";
			}
			String groupShare = getGroupShare(id);
			Wallet wallet = customer.getWallet();
			if(wallet==null) wallet = new Wallet();
			if(groupShare != null){
				wallet.setGroupShare(groupShare);
			}
			if(wallet.getShare()!=null && wallet.getShare().length()>0){
				wallet.setShare(wallet.getShare().replace("##", " #"));
			}
			if(wallet.getMoney()!=null){
				String amount = priceUtil.getAdminFormatedAmountWithCurrency(store, new BigDecimal(wallet.getMoney()));
				wallet.setAmount(amount);
			}
			if(wallet.getTopup()!=null){
				String amount = priceUtil.getAdminFormatedAmountWithCurrency(store, new BigDecimal(wallet.getTopup()));
				wallet.setTopupAmount(amount);
			}
			customer.setWallet(wallet);
			// customer.getWallet().setName(customer.getBilling().getFirstName());
			// customer.getWallet().setPoint(customer.getLoyalty().getVPoint());
			
		} else {
			customer = new Customer();
			Billing billing = new Billing();
			Delivery delivery = new Delivery();
			Country country = new Country();
			country.setIsoCode(Constants.DEFAULT_COUNTRY);
			billing.setCountry(country);
			delivery.setCountry(country);
			customer.setBilling(billing);
			customer.setDelivery(delivery);
		}
		//get list of countries (see merchant controller)
		Language language = (Language)request.getAttribute("LANGUAGE");				
		//get countries
		List<Country> countries = countryService.getCountries(language);

		//get list of zones
		List<Zone> zones = zoneService.list();
		
		this.getCustomerOptions(model, customer, store, language);

		model.addAttribute("zones", zones);
		model.addAttribute("countries", countries);
		model.addAttribute("customer", customer);
		return "admin-customer";	
		
	}
	
	private void getCustomerOptions(Model model, Customer customer, MerchantStore store, Language language) throws Exception {

		Map<Long,CustomerOption> options = new HashMap<>();
		//get options
		List<CustomerOptionSet> optionSet = customerOptionSetService.listByStore(store, language);
		if(!CollectionUtils.isEmpty(optionSet)) {
			
			
			ReadableCustomerOptionPopulator optionPopulator = new ReadableCustomerOptionPopulator();
			
			Set<CustomerAttribute> customerAttributes = customer.getAttributes();
			
			for(CustomerOptionSet optSet : optionSet) {
				
				com.salesmanager.core.model.customer.attribute.CustomerOption custOption = optSet.getCustomerOption();
				if(!custOption.isActive()) {
					continue;
				}
				CustomerOption customerOption = options.get(custOption.getId());
				
				optionPopulator.setOptionSet(optSet);
				
				
				
				if(customerOption==null) {
					customerOption = new CustomerOption();
					customerOption.setId(custOption.getId());
					customerOption.setType(custOption.getCustomerOptionType());
					customerOption.setName(custOption.getDescriptionsSettoList().get(0).getName());
					
				} 
				
				optionPopulator.populate(custOption, customerOption, store, language);
				options.put(customerOption.getId(), customerOption);

				if(!CollectionUtils.isEmpty(customerAttributes)) {
					for(CustomerAttribute customerAttribute : customerAttributes) {
						if(customerAttribute.getCustomerOption().getId().longValue()==customerOption.getId()){
							CustomerOptionValue selectedValue = new CustomerOptionValue();
							com.salesmanager.core.model.customer.attribute.CustomerOptionValue attributeValue = customerAttribute.getCustomerOptionValue();
							selectedValue.setId(attributeValue.getId());
							CustomerOptionValueDescription optValue = attributeValue.getDescriptionsSettoList().get(0);
							selectedValue.setName(optValue.getName());
							customerOption.setDefaultValue(selectedValue);
							if(customerOption.getType().equalsIgnoreCase(CustomerOptionType.Text.name())) {
								selectedValue.setName(customerAttribute.getTextValue());
							} 
						}
					}
				}
			}
		}
		
		
		model.addAttribute("options", options.values());

		
	}
	
	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value="/admin/customers/save.html", method=RequestMethod.POST)
	public String saveCustomer(@Valid @ModelAttribute("customer") Customer customer, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception{
	
		this.setMenu(model, request);

		Language language = (Language)request.getAttribute("LANGUAGE");
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		List<Language> languages = languageService.getLanguages();
		
		model.addAttribute("languages",languages);
		List<Group> groups = new ArrayList<>();
		List<Group> userGroups = groupService.listGroup(GroupType.CUSTOMER);
		for(Group group : userGroups) {
			groups.add(group);
		}
		model.addAttribute("groups",groups);
		List<Country> countries = countryService.getCountries(language);
		model.addAttribute("countries", countries);

		if(request.getParameter("walletTopup")!=null){
			Customer newCustomer = customerService.getById(customer.getId());
			Wallet wallet = newCustomer.getWallet();
			if(wallet!=null && wallet.getTopup()>0){
				Integer topup = wallet.getTopup();
				wallet.changeMoney(topup);
				wallet.setTopup(0);
				customerService.saveOrUpdate(newCustomer);
			}else{
				wallet = new Wallet();
				wallet.setMoney(0);
				wallet.setTopup(0);
				wallet.setCustomer(newCustomer);
				newCustomer.setWallet(wallet);
				customerService.saveOrUpdate(newCustomer);
			}
			newCustomer.getWallet().setGroupShare(getGroupShare(customer.getId()));
			model.addAttribute("customer", newCustomer);
			model.addAttribute("success","success");
			return "admin-customer";
		}
		
		String emailRegEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
		Pattern pattern = Pattern.compile(emailRegEx);
		
		this.getCustomerOptions(model, customer, store, language);
		
		
		if(!StringUtils.isBlank(customer.getEmailAddress() ) ){
			 java.util.regex.Matcher matcher = pattern.matcher(customer.getEmailAddress());
			 
			 if(!matcher.find()) {
				ObjectError error = new ObjectError("customerEmailAddress",messages.getMessage("Email.customer.EmailAddress", locale));
				result.addError(error);
			 }
		}else{
			ObjectError error = new ObjectError("customerEmailAddress",messages.getMessage("NotEmpty.customer.EmailAddress", locale));
			result.addError(error);
		}
		

		 
		if( StringUtils.isBlank(customer.getBilling().getFirstName() ) ){
			 ObjectError error = new ObjectError("billingFirstName", messages.getMessage("NotEmpty.customer.billingFirstName", locale));
			 result.addError(error);
		}
		
		if( StringUtils.isBlank(customer.getBilling().getLastName() ) ){
			 ObjectError error = new ObjectError("billingLastName", messages.getMessage("NotEmpty.customer.billingLastName", locale));
			 result.addError(error);
		}
		
		if( StringUtils.isBlank(customer.getBilling().getAddress() ) ){
			 ObjectError error = new ObjectError("billingAddress", messages.getMessage("NotEmpty.customer.billingStreetAddress", locale));
			 result.addError(error);
		}
		 
		if( StringUtils.isBlank(customer.getBilling().getCity() ) ){
			 ObjectError error = new ObjectError("billingCity",messages.getMessage("NotEmpty.customer.billingCity", locale));
			 result.addError(error);
		}
		 
		// if( customer.getShowBillingStateList().equalsIgnoreCase("yes" ) && customer.getBilling().getZone().getCode() == null ){
		// 	 ObjectError error = new ObjectError("billingState",messages.getMessage("NotEmpty.customer.billingState", locale));
		// 	 result.addError(error);
			 
		// }else if( customer.getShowBillingStateList().equalsIgnoreCase("no" ) && customer.getBilling().getState() == null ){
		// 		 ObjectError error = new ObjectError("billingState",messages.getMessage("NotEmpty.customer.billingState", locale));
		// 		 result.addError(error);
			
		// }
		 
		// if( StringUtils.isBlank(customer.getBilling().getPostalCode() ) ){
		// 	 ObjectError error = new ObjectError("billing.FirstName", messages.getMessage("NotEmpty.customer.billingPostCode", locale));
		// 	 result.addError(error);
		// }
		
		//check if error from the @valid
		if (result.hasErrors()) {
			return "admin-customer";
		}
				
		Customer newCustomer = new Customer();

		if( customer.getId()!=null && customer.getId().longValue()>0 ) {
			newCustomer = customerService.getById( customer.getId() );
			
			if(newCustomer==null) {
				return "redirect:/admin/customers/list.html";
			}
			if(newCustomer.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
				return "redirect:/admin/customers/list.html";
			}
		}else{
			//  new customer set marchant_Id
			MerchantStore merchantStore = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
			newCustomer.setMerchantStore(merchantStore);
		}
		
		List<Group> submitedGroups = customer.getGroups();
		Set<Integer> ids = new HashSet<>();
		for(Group group : submitedGroups) {
			ids.add(group.getId());
		}
		
		List<Group> newGroups = groupService.listGroupByIds(ids);
		newCustomer.setGroups(newGroups);

		newCustomer.setEmailAddress(customer.getEmailAddress() );		
		
		//get Customer country/zone 		
		Country deliveryCountry = countryService.getByCode( customer.getDelivery().getCountry().getIsoCode()); 
		Country billingCountry  = countryService.getByCode( customer.getBilling().getCountry().getIsoCode()) ;

		Zone deliveryZone = customer.getDelivery().getZone();
		Zone billingZone  = customer.getBilling().getZone();
		

		if ("yes".equalsIgnoreCase(customer.getShowDeliveryStateList())) {
			if(customer.getDelivery().getZone()!=null) {
				deliveryZone = zoneService.getByCode(customer.getDelivery().getZone().getCode());
				customer.getDelivery().setState( null );
			}
			
		}else if ("no".equalsIgnoreCase(customer.getShowDeliveryStateList())){
			if(customer.getDelivery().getState()!=null) {
				deliveryZone = null ;
				customer.getDelivery().setState( customer.getDelivery().getState() );
			}
		}
	
		if ("yes".equalsIgnoreCase(customer.getShowBillingStateList())) {
			if(customer.getBilling().getZone()!=null) {
				billingZone = zoneService.getByCode(customer.getBilling().getZone().getCode());
				customer.getBilling().setState( null );
			}
			
		}else if ("no".equalsIgnoreCase(customer.getShowBillingStateList())){
			if(customer.getBilling().getState()!=null) {
				billingZone = null ;
				customer.getBilling().setState( customer.getBilling().getState() );
			}
		}
		newCustomer.setDefaultLanguage(customer.getDefaultLanguage() );
		
		customer.getDelivery().setZone(  deliveryZone);
		customer.getDelivery().setCountry(deliveryCountry );
		newCustomer.setDelivery( customer.getDelivery() );
		
		customer.getBilling().setZone(  billingZone);
		customer.getBilling().setCountry(billingCountry );
		newCustomer.setBilling( customer.getBilling()  );
		newCustomer.setWallet(customer.getWallet());
		try{
			customerService.saveOrUpdate(newCustomer);
			newCustomer.getWallet().setGroupShare(getGroupShare(customer.getId()));
			model.addAttribute("customer", newCustomer);
			model.addAttribute("success","success");
		} catch(Exception e){
			model.addAttribute("countries", countries);
			ObjectError error = new ObjectError("customerFirstName", e.getMessage());
			result.addError(error);
		}
		
		return "admin-customer";
		
	}
	
	/**
	 * Deserves shop and admin
	 * @param request
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value={"/admin/customers/attributes/save.html"}, method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> saveCustomerAttributes(HttpServletRequest request, Locale locale) throws Exception {
		

		AjaxResponse resp = new AjaxResponse();
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		//1=1&2=on&3=eeee&4=on&customer=1

		@SuppressWarnings("rawtypes")
		Enumeration parameterNames = request.getParameterNames();
		
		Customer customer = null;
		
		while(parameterNames.hasMoreElements()) {

			String parameterName = (String)parameterNames.nextElement();
			String parameterValue = request.getParameter(parameterName);
			if(CUSTOMER_ID_PARAMETER.equals(parameterName)) {
				customer = customerService.getById(Long.parseLong(parameterValue));
				break;
			}
		}
		
		if(customer==null) {
			LOGGER.error("Customer id [customer] is not defined in the parameters");
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			String returnString = resp.toJSONString();
			return new ResponseEntity<>(returnString,httpHeaders,HttpStatus.OK);
		}
		
		if(customer.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
			LOGGER.error("Customer id does not belong to current store");
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			String returnString = resp.toJSONString();
			return new ResponseEntity<>(returnString,httpHeaders,HttpStatus.OK);
		}
		
		List<CustomerAttribute> customerAttributes = customerAttributeService.getByCustomer(store, customer);
		Map<Long,CustomerAttribute> customerAttributesMap = new HashMap<>();
		
		for(CustomerAttribute attr : customerAttributes) {
			customerAttributesMap.put(attr.getCustomerOption().getId(), attr);
		}

		parameterNames = request.getParameterNames();
		
		while(parameterNames.hasMoreElements()) {
			
			String parameterName = (String)parameterNames.nextElement();
			String parameterValue = request.getParameter(parameterName);
			try {
				
				String[] parameterKey = parameterName.split("-");
				com.salesmanager.core.model.customer.attribute.CustomerOption customerOption = null;
				com.salesmanager.core.model.customer.attribute.CustomerOptionValue customerOptionValue = null;

				
				if(CUSTOMER_ID_PARAMETER.equals(parameterName)) {
					continue;
				}
				
					if(parameterKey.length>1) {
						//parse key - value
						String key = parameterKey[0];
						String value = parameterKey[1];
						//should be on
						customerOption = customerOptionService.getById(Long.parseLong(key));
						customerOptionValue = customerOptionValueService.getById(Long.parseLong(value));
						

						
					} else {
						customerOption = customerOptionService.getById(Long.parseLong(parameterName));
						customerOptionValue = customerOptionValueService.getById(Long.parseLong(parameterValue));

					}
					
					//get the attribute
					//CustomerAttribute attribute = customerAttributeService.getByCustomerOptionId(store, customer.getId(), customerOption.getId());
					CustomerAttribute attribute = customerAttributesMap.get(customerOption.getId());
					if(attribute==null) {
						attribute = new CustomerAttribute();
						attribute.setCustomer(customer);
						attribute.setCustomerOption(customerOption);
					} else {
						customerAttributes.remove(attribute);
					}
					
					if(customerOption.getCustomerOptionType().equals(CustomerOptionType.Text.name())) {
						if(!StringUtils.isBlank(parameterValue)) {
							attribute.setCustomerOptionValue(customerOptionValue);
							attribute.setTextValue(parameterValue);
						} else {
							attribute.setTextValue(null);
						}
					} else {
						attribute.setCustomerOptionValue(customerOptionValue);
					}
					
					
					if(attribute.getId()!=null && attribute.getId().longValue()>0) {
						if(attribute.getCustomerOptionValue()==null){
							customerAttributeService.delete(attribute);
						} else {
							customerAttributeService.update(attribute);
						}
					} else {
						customerAttributeService.save(attribute);
					}
					


			} catch (Exception e) {
				LOGGER.error("Cannot get parameter information " + parameterName,e);
			}
			
		}
		
		//and now the remaining to be removed
		for(CustomerAttribute attr : customerAttributes) {
			customerAttributeService.delete(attr);
		}
		
		resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		String returnString = resp.toJSONString();
		return new ResponseEntity<>(returnString,httpHeaders,HttpStatus.OK);
		

	}


	
	/**
	 * List of customers
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/admin/customers/list.html", method=RequestMethod.GET)
	public String displayCustomers(Model model,HttpServletRequest request) {
		
		this.setMenu(model, request);
		return "admin-customers";
	}
	
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/admin/customers/page.html", method=RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String>  pageCustomers(HttpServletRequest request,HttpServletResponse response) {

		final HttpHeaders httpHeaders= new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		AjaxPageableResponse resp = new AjaxPageableResponse();
		
		//Language language = (Language)request.getAttribute("LANGUAGE");
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		
		
		try {
			
			// Enumeration<String> parameterNames = request.getParameterNames();
 
			// while (parameterNames.hasMoreElements()) {
	 
			// 	String paramName = parameterNames.nextElement();
			// 	String[] paramValues = request.getParameterValues(paramName);
			// 	for (int i = 0; i < paramValues.length; i++) {
			// 		String paramValue = paramValues[i];
			// 		System.out.println(paramName + ":" + paramValue);
			// 	}
	 
			// }
	 
			
			//Map<String,Country> countriesMap = countryService.getCountriesMap(language);
			
			
			int startRow = Integer.parseInt(request.getParameter("_startRow"));
			int endRow = Integer.parseInt(request.getParameter("_endRow"));
			String	email = request.getParameter("email");
			String name = request.getParameter("name");
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String	country = request.getParameter("country");
			String	phone = request.getParameter("phone");
			String	cid = request.getParameter("id");
			String	date = request.getParameter("date");
			String	address = request.getParameter("address");
			if(date!=null && date.length()!=10){
				return new ResponseEntity<>("{}",httpHeaders,HttpStatus.OK);
			}
			
			CustomerCriteria criteria = new CustomerCriteria();
			criteria.setStartIndex(startRow);
			criteria.setMaxCount(endRow);
			criteria.setOrderBy(CriteriaOrderBy.DESC);
			criteria.setCriteriaOrderByField("id");

			if(!StringUtils.isBlank(address)) {
				criteria.setAddress(address);
			}
			if(!StringUtils.isBlank(date)) {
				criteria.setDate(date);
			}

			if(!StringUtils.isBlank(phone)) {
				criteria.setPhone(phone);
			}

			if(!StringUtils.isBlank(cid)) {
				criteria.setId(Long.valueOf(cid));
			}

			if(!StringUtils.isBlank(phone)) {
				criteria.setPhone(phone);
			}

			if(!StringUtils.isBlank(email)) {
				criteria.setEmail(email);
			}
			
			if(!StringUtils.isBlank(name)) {
				criteria.setName(name);
			}
			
			if(!StringUtils.isBlank(country)) {
				criteria.setCountry(country);
			}
			
			if(!StringUtils.isBlank(firstName)) {
				criteria.setFirstName(firstName);
			}
			
			if(!StringUtils.isBlank(lastName)) {
				criteria.setLastName(lastName);
			}
			

			CustomerList customerList = customerService.getListByStore(store,criteria);
			
			if(customerList.getCustomers()!=null) {
			
				resp.setTotalRow(customerList.getTotalCount());
				
				for(Customer customer : customerList.getCustomers()) {
					@SuppressWarnings("rawtypes")
					Map entry = new HashMap();
					entry.put("id", customer.getId());
					entry.put("firstName", customer.getBilling().getFirstName());
					entry.put("lastName", customer.getBilling().getLastName());
					entry.put("email", customer.getEmailAddress());
					entry.put("country", customer.getBilling().getCountry().getIsoCode());
					entry.put("phone", customer.getBilling().getTelephone());
					entry.put("address", customer.getBilling().getAddress());
					Integer point = null;
					if(customer.getLoyalty()!=null){
						point = customer.getLoyalty().getVPoint();
					}
					entry.put("point", point);
					if(customer.getAuditSection()==null){
						entry.put("date", null);
					}else{
						entry.put("date", DateUtil.formatDate(customer.getAuditSection().getDateCreated()));
					}
					resp.addDataEntry(entry);
					
				}
			
			}
			
		} catch (Exception e) {
			LOGGER.error("Error while paging orders", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		String returnString = resp.toJSONString();
		
		return new ResponseEntity<>(returnString,httpHeaders,HttpStatus.OK);
		
	
	}
	
	public boolean sendPassword(String pwd, String phone) {
		
		String text = configuration.getProperty("SMS_RESET_PASSWORD") + pwd;
		return SmsUtils.sendTextMessage(phone, text);
		// Sms sms = new Sms();
		// sms.setPhone(phone);
		// sms.setText(text);
	    // RestTemplate restTemplate = new RestTemplate();
        // final HttpEntity<Sms> entity = new HttpEntity<>(sms, SessionUtil.getBasicHeader("a42d4482-d5e6-40fc-bc5b-3ea7ec89b66b"));

		// ResponseEntity<?> response = restTemplate.postForEntity(uri, entity, String.class);
    
	    // return (response.getStatusCode()==HttpStatus.OK);
		
	}	
	
	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value="/admin/customers/resetPassword.html", method=RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> resetPassword(HttpServletRequest request,HttpServletResponse response) {
		
		String customerId = request.getParameter("customerId");
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		AjaxResponse resp = new AjaxResponse();
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		try {
			
			Long id = Long.parseLong(customerId);
			
			Customer customer = customerService.getById(id);
			
			if(customer==null) {
				resp.setErrorString("Customer does not exist");
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<>(returnString,httpHeaders,HttpStatus.OK);
			}
			
			if(customer.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
				resp.setErrorString("Invalid customer id");
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<>(returnString,httpHeaders,HttpStatus.OK);
			}
			
			Language userLanguage = customer.getDefaultLanguage();
			
			String pwd = customerFacade.resetPassword(customer, store, userLanguage);
			String phone = customer.getBilling().getTelephone();
			sendPassword(pwd, phone);
			
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			
		} catch (Exception e) {
			LOGGER.error("An exception occured while changing password",e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		
		String returnString = resp.toJSONString();
		return new ResponseEntity<>(returnString,httpHeaders,HttpStatus.OK);
		
		
	}
	
	
	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value="/admin/customers/setCredentials.html", method=RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> setCredentials(HttpServletRequest request,HttpServletResponse response) {
		
		String customerId = request.getParameter("customerId");
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		
		MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
		AjaxResponse resp = new AjaxResponse();
		final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		
		
		
		try {
			
			Long id = Long.parseLong(customerId);
			
			Customer customer = customerService.getById(id);
			
			if(customer==null) {
				resp.setErrorString("Customer does not exist");
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<>(returnString,httpHeaders,HttpStatus.OK);
			}
			
			if(customer.getMerchantStore().getId().intValue()!=store.getId().intValue()) {
				resp.setErrorString("Invalid Merchant Store id");
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<>(returnString,httpHeaders,HttpStatus.OK);
			}
			
			if(StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
				resp.setErrorString("Invalid username or password");
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<>(returnString,httpHeaders,HttpStatus.OK);
			}
			
			// Language userLanguage = customer.getDefaultLanguage();
			
			// Locale customerLocale = LocaleUtils.getLocale(userLanguage);

			// String encodedPassword = passwordEncoder.encode(password);
			
			// customer.setPassword(encodedPassword);
			if(userName.contains("@")){
				customer.setNick(userName);
			}else{
				customer.setNick(userName + "@vfsc.vn");
			}
			customerFacade.changePassword(customer, password);
			
			// customerService.saveOrUpdate(customer);
			
			//send email
			
/*			try {

				//creation of a user, send an email
				String[] storeEmail = {store.getStoreEmailAddress()};
				
				
				Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(request.getContextPath(), store, messages, customerLocale);
				templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", customerLocale));
		        templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, customer.getBilling().getFirstName());
		        templateTokens.put(EmailConstants.EMAIL_CUSTOMER_LASTNAME, customer.getBilling().getLastName());
				templateTokens.put(EmailConstants.EMAIL_RESET_PASSWORD_TXT, messages.getMessage("email.customer.resetpassword.text", customerLocale));
				templateTokens.put(EmailConstants.EMAIL_CONTACT_OWNER, messages.getMessage("email.contactowner", storeEmail, customerLocale));
				templateTokens.put(EmailConstants.EMAIL_PASSWORD_LABEL, messages.getMessage("label.generic.password",customerLocale));
				templateTokens.put(EmailConstants.EMAIL_CUSTOMER_PASSWORD, password);


				Email email = new Email();
				email.setFrom(store.getStorename());
				email.setFromEmail(store.getStoreEmailAddress());
				email.setSubject(messages.getMessage("label.generic.changepassword",customerLocale));
				email.setTo(customer.getEmailAddress());
				email.setTemplateName(RESET_PASSWORD_TPL);
				email.setTemplateTokens(templateTokens);
	
	
				
				emailService.sendHtmlEmail(store, email);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			
			} catch (Exception e) {
				LOGGER.error("Cannot send email to user",e);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}*/
			
			
			
			
		} catch (Exception e) {
			LOGGER.error("An exception occured while changing password",e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}
		
		
		String returnString = resp.toJSONString();
		return new ResponseEntity<>(returnString,httpHeaders,HttpStatus.OK);
		
		
	}
	
	private void setMenu(Model model, HttpServletRequest request) {
		
		//display menu
		Map<String,String> activeMenus = new HashMap<>();
		activeMenus.put("customer", "customer");
		activeMenus.put("customer-list", "customer-list");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");

		Menu currentMenu = menus.get("customer");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
		
	}
	
	

}
