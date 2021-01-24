package com.salesmanager.shop.store.api.v1.customer;

import javax.inject.Named;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.apache.commons.lang.Validate;
import org.apache.http.auth.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.utils.CacheUtils;
import com.salesmanager.core.business.utils.CoreConfiguration;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.reference.language.Language;
import com.salesmanager.shop.model.customer.PersistableCustomer;
import com.salesmanager.shop.store.controller.customer.facade.CustomerFacade;
import com.salesmanager.shop.store.controller.store.facade.StoreFacade;
import com.salesmanager.shop.store.security.AuthenticationRequest;
import com.salesmanager.shop.store.security.AuthenticationResponse;
import com.salesmanager.shop.store.security.JWTTokenUtil;
import com.salesmanager.shop.store.security.NotificationRequest;
import com.salesmanager.shop.store.security.PasswordRequest;
import com.salesmanager.shop.store.security.user.JWTUser;
import com.salesmanager.shop.utils.LanguageUtils;
import com.salesmanager.shop.utils.SmsUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1")
@Api(tags = {"Customer authentication resource (Customer Authentication Api)"})
@SwaggerDefinition(tags = {
    @Tag(name = "Customer authentication resource", description = "Authenticates customer, register customer and reset customer password")
})
public class AuthenticateCustomerApi {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticateCustomerApi.class);

    @Value("${authToken.header}")
    private String tokenHeader;

    @Value("${pushToken.header}")
    private String pushHeader;

	@Inject
	private CustomerService customerService;

	@Inject
	@Named("passwordEncoder")
    private PasswordEncoder passwordEncoder;
    
	@Inject
    private CoreConfiguration configuration;
    
    @Inject
    private AuthenticationManager jwtCustomerAuthenticationManager;

    @Inject
    private JWTTokenUtil jwtTokenUtil;

    @Inject
    private UserDetailsService jwtCustomerDetailsService;
    
    @Inject
    private CustomerFacade customerFacade;

    @Inject
    private CacheUtils cache;
    
    @Inject
    private StoreFacade storeFacade;
    
    @Inject
    private LanguageUtils languageUtils;
    
    /**
     * Create new customer for a given MerchantStore, then authenticate that customer
     */
    @RequestMapping( value={"/customer/register"}, method=RequestMethod.POST, produces ={ "application/json" })
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(httpMethod = "POST", value = "Registers a customer to the application", notes = "Used as self-served operation",response = AuthenticationResponse.class)
    @ResponseBody
    public ResponseEntity<?> register(@Valid @RequestBody PersistableCustomer customer, HttpServletRequest request, HttpServletResponse response) throws Exception {

        
        LOGGER.info("Register");
        //try {
            
            MerchantStore merchantStore = storeFacade.getByCode(request);
            Language language = languageUtils.getRESTLanguage(request);  
            
            //Transition
            customer.setUserName(customer.getEmailAddress());
            
            Validate.notNull(customer.getUserName(),"Username cannot be null");
            Validate.notNull(customer.getFirstName(),"Firstname cannot be null");
            Validate.notNull(customer.getBilling(),"Requires billing information");
            Validate.notNull(customer.getBilling().getPhone(),"Requires billing phone");
            Validate.notNull(customer.getBilling().getAddress(),"Requires billing address");
            Validate.notNull(customer.getBilling().getCountry(),"Requires customer Country code");
            
            customerFacade.registerCustomer(customer, merchantStore, language);
            
            // Perform the security
            Authentication authentication = null;
            try {
                
                authentication = jwtCustomerAuthenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                customer.getUserName(),
                                customer.getPassword()
                        )
                );
                
            } catch(Exception e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            if(authentication == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Reload password post-security so we can generate token
            final JWTUser userDetails = (JWTUser)jwtCustomerDetailsService.loadUserByUsername(customer.getUserName());
            final String token = jwtTokenUtil.generateToken(userDetails);

            // Return the token
            return ResponseEntity.ok(new AuthenticationResponse(customer.getId(),token));

        
    }

    /**
     * Authenticate a customer using username & password
     * @param authenticationRequest
     * @param device
     * @return
     * @throws AuthenticationException
     */
    @RequestMapping(value = "/customer/login", method = RequestMethod.POST, produces ={ "application/json" })
    @ApiOperation(httpMethod = "POST", value = "Authenticates a customer to the application", notes = "Customer can authenticate after registration, request is {\"username\":\"admin\",\"password\":\"password\"}",response = ResponseEntity.class)
    @ResponseBody
    public ResponseEntity<?> authenticate(@RequestBody @Valid AuthenticationRequest authenticationRequest) throws AuthenticationException {

    	//TODO SET STORE in flow
        // Perform the security
        String keyName = CacheUtils.KEY_LOGIN + authenticationRequest.getUsername();
        Authentication authentication = null;
        try {
            
            Integer counter = (Integer)cache.getFromCache(keyName);
            if(counter==null || counter.intValue()<5){
                counter = counter==null?1:(counter+1);
                cache.putInCache(counter, authenticationRequest.getUsername());
            }else{
                return new ResponseEntity<>("{\"message\":\"Bad credentials\"}",HttpStatus.UNAUTHORIZED);
            }
                //to be used when username and password are set
            authentication = jwtCustomerAuthenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    )
            );

        } catch(BadCredentialsException unn) {
        	return new ResponseEntity<>("{\"message\":\"Bad credentials\"}",HttpStatus.UNAUTHORIZED);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        if(authentication == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try{
            cache.removeFromCache(keyName);
        }catch(Exception e){

        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        // todo create one for social
        final JWTUser userDetails = (JWTUser)jwtCustomerDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Return the token
        return ResponseEntity.ok(new AuthenticationResponse(userDetails.getId(),token));
    }

    @PutMapping(value = "/customer/password/reset")
    @ApiOperation(httpMethod = "PUT", value = "Reset customer password", notes = "Reset password request object is {\"username\":\"test@email.com\"}",response = ResponseEntity.class)
    public ResponseEntity<?> resetPassword(@RequestBody @Valid AuthenticationRequest authenticationRequest, HttpServletRequest request) {
                 
        MerchantStore merchantStore = storeFacade.getByCode(request);
        try{
            String username = authenticationRequest.getUsername() + "@vfsc.vn";
            Customer customer = customerFacade.getCustomerByUserName(username, merchantStore);
            
            if(customer == null){
                return ResponseEntity.notFound().build();
            }
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Exception when reseting password");
        }
        
        String keyName = CacheUtils.KEY_RESET + authenticationRequest.getUsername();
        AuthenticationRequest auth = (AuthenticationRequest)cache.get(keyName);
        
        if(auth==null){
            authenticationRequest.setCounter(0);
            authenticationRequest.setTime(System.currentTimeMillis());
            cache.put(authenticationRequest, keyName);
        }else if(auth.getCounter().intValue()<3){
            auth.setTime(System.currentTimeMillis());
            auth.setCounter(auth.getCounter() + 1);
            cache.put(auth, keyName);
        }else{
            long diff = System.currentTimeMillis()-auth.getTime();
            long diffHours = TimeUnit.MILLISECONDS.toHours(diff);
            // long remain = diff % (24 * 60 * 60 * 1000);
            // long diffHours = remain / (60 * 60 * 1000);
            if(diffHours>7){
                authenticationRequest.setCounter(0);
                authenticationRequest.setTime(System.currentTimeMillis());
                cache.put(authenticationRequest, keyName);
            }else{
                return ResponseEntity.badRequest().body("Exception when reseting password");
            }
        }

            auth = (AuthenticationRequest)cache.get(keyName);
            String telephone = auth.getUsername();
            String text = configuration.getProperty("SMS_OTP_PASSWORD") + auth.getOtp();
            if(SmsUtils.sendTextMessage(telephone, text)){
                return ResponseEntity.ok("OTP have been sent to " + telephone);
            }
                     
            return ResponseEntity.badRequest().body("Exception when reseting password: " + telephone);
    }

    @PutMapping(value = "/customer/password/otp")
    @ApiOperation(httpMethod = "PUT", value = "Reset customer password", notes = "Reset password request object is {\"username\":\"test@email.com\"}",response = ResponseEntity.class)
    public ResponseEntity<?> otpPassword(@RequestBody @Valid AuthenticationRequest authenticationRequest, HttpServletRequest request) {
  
        String keyName = CacheUtils.KEY_RESET + authenticationRequest.getUsername();
        AuthenticationRequest auth = (AuthenticationRequest)cache.get(keyName);
        if(auth==null || auth.getOtp()==null || authenticationRequest.getOtp()==null){
            return ResponseEntity.noContent().build();
        }

        long diff = System.currentTimeMillis()-auth.getTime();
        long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diff);

        if(diffMinutes>5){
            return ResponseEntity.status(HttpStatus.GONE).body("OTP Expired when reset password");
        }
        
        try {
            if(auth.getOtp().equals(authenticationRequest.getOtp()) 
                && auth.getUsername().equals(authenticationRequest.getUsername()) 
                && auth.getPassword().equals(authenticationRequest.getPassword())){
                MerchantStore merchantStore = storeFacade.getByCode(request);
                // Language language = languageUtils.getRESTLanguage(request);
                String username = authenticationRequest.getUsername() + "@vfsc.vn";
                Customer customer = customerFacade.getCustomerByUserName(username, merchantStore);
                if(customer == null){
                    return ResponseEntity.notFound().build();
                }
                String encodedPassword = passwordEncoder.encode(auth.getPassword());
			
			    customer.setPassword(encodedPassword);
			
			    customerService.saveOrUpdate(customer);
                 
                cache.removeFromCache(keyName);         
                return ResponseEntity.ok("Reset password successfull for " + auth.getUsername());
            }else{
                auth.setOtp(null);
                cache.put(auth,keyName);
                return ResponseEntity.badRequest().body("Wrong OTP when reset password");
            }
            
        } catch(Exception e) {
            return ResponseEntity.badRequest().body("Exception when reseting password " + e.getMessage());
        }
    }
    
  
    @PutMapping(value = "/customer/password")
    @ApiOperation(httpMethod = "PUT", value = "Sends a request to change password", notes = "Password change request is {\"username\":\"test@email.com\"}",response = ResponseEntity.class)
    public ResponseEntity<?> changePassword(@RequestBody @Valid PasswordRequest passwordRequest, HttpServletRequest request) throws Exception {
        String token = request.getHeader(tokenHeader);
        
        if(token != null && token.contains("Bearer")) {
            token = token.substring("Bearer ".length(),token.length());
        }
          
        String username = jwtTokenUtil.getUsernameFromToken(token);

        System.out.println("changePassword(" + username + ") --------------------- TOKEN : " + token);
  
        if(username == null || passwordRequest.getUsername()==null || !passwordRequest.getUsername().equals(username)){
            return ResponseEntity.badRequest().body("Exception Username when change password");
        }

        try {
            MerchantStore merchantStore = storeFacade.getByCode(request);
  
            Customer customer = customerFacade.getCustomerByUserName(passwordRequest.getUsername(), merchantStore);
            
            if(customer == null){
                return ResponseEntity.badRequest().body("Exception Customer when change password");
            }
  
            //need to validate if password matches
            if(!customerFacade.passwordMatch(passwordRequest.getCurrent(), customer)) {
                return ResponseEntity.badRequest().body("Username or password does not match");
            }
            
            if(!passwordRequest.getPassword().equals(passwordRequest.getRepeatPassword())) {
                return ResponseEntity.badRequest().body("Both passwords do not match");
            }
            
            customerFacade.changePassword(customer, passwordRequest.getPassword());           
            return ResponseEntity.ok(username);
            
        } catch(Exception e) {
            return ResponseEntity.badRequest().body("Exception when change password "+e.getMessage());
        }
    }

    @RequestMapping(value = "/private/customer/refresh", method = RequestMethod.GET, produces ={ "application/json" })
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);

        if(token != null && token.contains("Bearer")) {
            token = token.substring("Bearer ".length(),token.length());
        }
          
        String username = jwtTokenUtil.getUsernameFromToken(token);

        System.out.println("refreshToken(" + username + ") --------------------- TOKEN : " + token);

        JWTUser user = (JWTUser) jwtCustomerDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new AuthenticationResponse(user.getId(),refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
