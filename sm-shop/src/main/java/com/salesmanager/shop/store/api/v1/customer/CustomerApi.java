package com.salesmanager.shop.store.api.v1.customer;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.repositories.customer.CustomerRepository;
import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.utils.CacheUtils;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.model.customer.PersistableCustomer;
import com.salesmanager.shop.model.customer.ReadableCustomer;
import com.salesmanager.shop.model.references.ReadableWallet;
import com.salesmanager.shop.store.controller.customer.facade.CustomerFacade;
import com.salesmanager.shop.store.security.NotificationRequest;
import com.salesmanager.shop.store.security.user.JWTUser;
import com.salesmanager.shop.utils.PushUtils;
import com.salesmanager.shop.model.shop.CacheNamesImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import springfox.documentation.annotations.ApiIgnore;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.customer.Wallet;

@RestController
@RequestMapping(value = "/api/v1")
@Api(tags = {"Customer management resource (Customer Management Api)"})
@SwaggerDefinition(tags = {
    @Tag(name = "Customer management resource", description = "Manage customers")
})
public class CustomerApi {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerApi.class);

  @Inject
  private CustomerFacade customerFacade;

  @Inject
  private CustomerRepository customerRepository;

  @Inject
  private CustomerService customerService;

  @Inject
  private CacheUtils cache;


  /** Create new customer for a given MerchantStore */
  // @PostMapping("/private/customer")
  // @ApiOperation(
  //     httpMethod = "POST",
  //     value = "Creates a customer",
  //     notes = "Requires administration access",
  //     produces = "application/json",
  //     response = PersistableCustomer.class)
  // @ApiImplicitParams({
  //     @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT")
  // })
  // public PersistableCustomer create(
  //     @ApiIgnore MerchantStore merchantStore,
  //     @Valid @RequestBody PersistableCustomer customer) {
  //     return customerFacade.create(customer, merchantStore);

  // }


  
/*  *//**
   * Update authenticated customer adresses
   * @param userName
   * @param merchantStore
   * @param customer
   * @return
   *//*
  @PutMapping("/auth/customer/{id}")
  @ApiOperation(
      httpMethod = "PUT",
      value = "Updates a customer",
      produces = "application/json",
      response = PersistableCustomer.class)
  @ApiImplicitParams({
      @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT")
  })
  public PersistableCustomer update(
      @PathVariable String userName,
      @ApiIgnore MerchantStore merchantStore,
      @Valid @RequestBody PersistableCustomer customer) {
      // TODO customer.setUserName
      // TODO more validation
      return customerFacade.update(customer, merchantStore);
  }*/
  
  
  

//   @PutMapping("/private/customer/{id}")
//   @ApiOperation(
//       httpMethod = "PUT",
//       value = "Updates a customer",
//       notes = "Requires administration access",
//       produces = "application/json",
//       response = PersistableCustomer.class)
//   @ApiImplicitParams({
//       @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT")
//   })
//   public PersistableCustomer update(
//       @PathVariable Long id,
//       @ApiIgnore MerchantStore merchantStore,
//       @Valid @RequestBody PersistableCustomer customer) {

//       customer.setId(id);
//       return customerFacade.update(customer, merchantStore);
//   }
  
//   @PatchMapping("/private/customer/{id}/address")
//   @ApiOperation(
//       httpMethod = "PATCH",
//       value = "Updates a customer",
//       notes = "Requires administration access",
//       produces = "application/json",
//       response = Void.class)
//   @ApiImplicitParams({
//       @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT")
//   })
//   public void updateAddress(
//       @PathVariable Long id,
//       @ApiIgnore MerchantStore merchantStore,
//       @RequestBody PersistableCustomer customer) {

//       customer.setId(id);
//       customerFacade.updateAddress(customer, merchantStore);
//   }

//   @DeleteMapping("/private/customer/{id}")
//   @ApiOperation(
//       httpMethod = "DELETE",
//       value = "Deletes a customer",
//       notes = "Requires administration access")
//   @ApiImplicitParams({
//     @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT")
//   })
//   public void delete(
//       @PathVariable Long id,
//       @ApiIgnore MerchantStore merchantStore
//       ) {
//     customerFacade.deleteById(id);
//   }

  /**
   * Get all customers
   *
   * @param start
   * @param count
   * @param request
   * @return
   * @throws Exception
   */
//   @GetMapping("/private/customers")
//   @ApiImplicitParams({
//       @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT"),
//       @ApiImplicitParam(name = "lang", dataType = "string", defaultValue = "vi")
//   })
//   public ReadableCustomerList list(
//       @RequestParam(value = "page", required = false) Integer page,
//       @RequestParam(value = "count", required = false) Integer count,
//       @ApiIgnore MerchantStore merchantStore,
//       @ApiIgnore Language language) {
//     CustomerCriteria customerCriteria = createCustomerCriteria(page, count);
//     return customerFacade.getListByStore(merchantStore, customerCriteria, language);
//   }

//   private CustomerCriteria createCustomerCriteria(Integer start, Integer count) {
//     CustomerCriteria customerCriteria = new CustomerCriteria();
//     Optional.ofNullable(start).ifPresent(customerCriteria::setStartIndex);
//     Optional.ofNullable(count).ifPresent(customerCriteria::setMaxCount);
//     return customerCriteria;
//   }

//   @GetMapping("/private/customer/{id}")
//   @ApiImplicitParams({
//       @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT"),
//       @ApiImplicitParam(name = "lang", dataType = "string", defaultValue = "vi")
//   })
//   public ReadableCustomer get(
//       @PathVariable Long id,
//       @ApiIgnore MerchantStore merchantStore,
//       @ApiIgnore Language language) {
//       return customerFacade.getCustomerById(id, merchantStore, language);
//   }
//   @GetMapping("/private/customer/store/bank")
//   public MerchantStore get(HttpServletRequest request) {
//     MerchantStore store = (MerchantStore)request.getAttribute(Constants.ADMIN_STORE);
//     return store;
//   }
  /**
   * Get logged in customer profile
   * @param merchantStore
   * @param language
   * @param request
   * @return
   */
  @GetMapping("/private/customer/profile")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT"),
      @ApiImplicitParam(name = "lang", dataType = "string", defaultValue = "vi")
  })
  public ReadableCustomer getAuthUser(
      @ApiIgnore MerchantStore merchantStore,
      @ApiIgnore Language language,
      HttpServletRequest request) {
    Principal principal = request.getUserPrincipal();
    String userName = principal.getName();
    return customerFacade.getCustomerByNick(userName, merchantStore, language);
  }

  @GetMapping("/private/customer/wallet/other")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT"),
      @ApiImplicitParam(name = "lang", dataType = "string", defaultValue = "vi")
  })
  public List<ReadableWallet> getWallet(HttpServletRequest request) {
    UsernamePasswordAuthenticationToken principal = (UsernamePasswordAuthenticationToken)request.getUserPrincipal();
		JWTUser customer =  (JWTUser)principal.getPrincipal();
		Long id = customer.getId();
    List<Customer> customers = customerService.getByShare(id);
    List<ReadableWallet> wallets = new ArrayList<>();
    if(customers!=null){
      for(Customer c: customers){
        if(c.getWallet()!=null){
          wallets.add(new ReadableWallet(c.getWallet().getId(), c.getId(), c.getWallet().getMoney(), c.getBilling().getFirstName()));
        }
      }
    }
    return wallets;
  }
  
  @RequestMapping( value={"/private/customer/address"}, method=RequestMethod.PATCH, produces ={ "application/json" })
  @ResponseStatus(HttpStatus.ACCEPTED)
  @ApiImplicitParams({
      @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT")
  })
  public ResponseEntity<?> updateAuthUserAddress(@ApiIgnore MerchantStore merchantStore, @RequestBody PersistableCustomer customer, HttpServletRequest request) {
      Principal principal = request.getUserPrincipal();
      String username = principal.getName();

      String keyName = CacheNamesImpl.KEY_CUSTOMER_INFOR + username;
      Long updateTime = (Long)cache.get(keyName);
      if(updateTime!=null){
        long diff = System.currentTimeMillis()-updateTime.longValue();
        long diffHours = TimeUnit.MILLISECONDS.toHours(diff);
        if(diffHours<9){
          return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
      }
      cache.put(System.currentTimeMillis(), keyName);

      customerFacade.updateAddress(username, customer, merchantStore);
      return new ResponseEntity<>(HttpStatus.ACCEPTED);
  }

  @RequestMapping( value={"/private/customer/wallet/share"}, method=RequestMethod.PUT, produces ={ "application/json" })
  @ResponseStatus(HttpStatus.ACCEPTED)
  @ApiImplicitParams({
      @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT")
  })
  public ResponseEntity<?> shareWallet(@ApiIgnore MerchantStore merchantStore, @RequestBody ReadableWallet wallet, HttpServletRequest request) {
      UsernamePasswordAuthenticationToken principal = (UsernamePasswordAuthenticationToken)request.getUserPrincipal();
      JWTUser user =  (JWTUser)principal.getPrincipal();
      Long id = user.getId();

      if(wallet==null || wallet.getFriends()==null || wallet.getFriends().isEmpty()){
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }

      String keyName = CacheNamesImpl.KEY_CUSTOMER_INFOR + user.getUsername();
      Long updateTime = (Long)cache.get(keyName);
      if(updateTime!=null){
        long diff = System.currentTimeMillis()-updateTime.longValue();
        long check = TimeUnit.MILLISECONDS.toSeconds(diff);
        if(check<5){
          return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
      }
      cache.put(System.currentTimeMillis(), keyName);

      Customer customer = customerService.getById(id);
      if(customer!=null && customer.getWallet()!=null){
        Wallet w = customer.getWallet();
        w.setFriends(wallet.getFriends());
        String share = w.friendsToShare();
        if(share!=null){
          w.setShare(share.replace("#" + id + "#", ""));
        }
        try{
          customerService.saveOrUpdate(customer);
          return new ResponseEntity<>(HttpStatus.OK);
        }catch(ServiceException e){
          System.out.println("ERROR Share:" + e.toString());
        }
      }
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  @RequestMapping( value={"/private/customer/token"}, method=RequestMethod.PUT, produces ={ "application/json" })
  @ResponseStatus(HttpStatus.ACCEPTED)
  @Transactional
  public ResponseEntity<?> updateNotificationToken(@RequestBody @Valid NotificationRequest device, HttpServletRequest request) {
      Principal principal = request.getUserPrincipal();
      String username = principal.getName();

      if(StringUtils.isEmpty(device.getOs()) || StringUtils.isEmpty(device.getToken())){
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
      
      String keyName = CacheNamesImpl.KEY_DEVICE + username;
      Long updateTime = (Long)cache.get(keyName);
      if(updateTime!=null){
        long diff = System.currentTimeMillis()-updateTime.longValue();
        long diffHours = TimeUnit.MILLISECONDS.toHours(diff);
        if(diffHours<9){
          return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
      }
      cache.put(System.currentTimeMillis(), keyName);

      try{
        customerRepository.updateToken(device.getOs(), device.getToken(), username);
        PushUtils.subscribe(device.getToken());
      }catch(Exception e){
        System.out.println("Notification update {token: " + device.getToken() + "} "  + e.getMessage());
      }
      return new ResponseEntity<>(HttpStatus.ACCEPTED);
  }
  
//   @PutMapping("/auth/customer/{id}")
//   @ApiOperation(
//       httpMethod = "PUT",
//       value = "Updates a loged in customer profile",
//       notes = "Requires authentication",
//       produces = "application/json",
//       response = PersistableCustomer.class)
//   @ApiImplicitParams({
//       @ApiImplicitParam(name = "store", dataType = "string", defaultValue = "DEFAULT")
//   })
//   public PersistableCustomer update(
//       @ApiIgnore MerchantStore merchantStore,
//       @Valid @RequestBody PersistableCustomer customer,
//       HttpServletRequest request) {
      
//       Principal principal = request.getUserPrincipal();
//       String userName = principal.getName();

//       return customerFacade.update(userName, customer, merchantStore);
//   }
  
  
}
