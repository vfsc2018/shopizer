package com.salesmanager.shop.store.api.v1.order;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.MediaType;

import com.salesmanager.core.business.services.customer.CustomerService;
import com.salesmanager.core.business.services.order.OrderService;
import com.salesmanager.core.business.utils.CoreConfiguration;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.order.Order;
import com.salesmanager.shop.model.user.Payment;
import com.salesmanager.shop.model.user.VnpayResponse;
import com.salesmanager.shop.utils.SessionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@RestController
@RequestMapping(value = "/api/v1")
@Api(tags = {"Payment Api"})
@SwaggerDefinition(tags = {
    @Tag(name = "Payment management resource", description = "payment gateway")
})
public class PaymentApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentApi.class);

    @Value("${authToken.header}")
    private String tokenHeader;

	@Inject
	private CustomerService customerService;
    @Inject
    private CoreConfiguration configuration;

	@Inject
	private OrderService orderService;

    private Payment vnpayPayload(String bank, String info, String tran, int total) {
        
        String callback = configuration.getProperty("PAYMENT_VNPAY_CALLBACK");
        String app = configuration.getProperty("PAYMENT_VFSCFOOD_ID");
        long token = System.currentTimeMillis();

        Payment p = new Payment();
        p.setOrderType(10000);
        p.setEntity(app);
        p.setBankCode(bank);
        p.setPaymentInfo(info);
        p.setTotalMoney(total);
        p.setTransactionId(tran);
        p.setCallbackUrl(String.format(callback, tran, token));

        return p;
        
    }	

	@RequestMapping(value = { "/private/payment/vnpay" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?>  vnpay(@Valid @RequestBody Payment payment, HttpServletRequest request, HttpServletResponse response) {
        final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Long id = Long.valueOf(payment.getTransactionId());
        
        Principal principal = request.getUserPrincipal();
        String userName = principal.getName();

        Customer customer = customerService.getByNick(userName);

        if (customer == null) {
            return new ResponseEntity<>("{\"customer\":" + userName + "}", httpHeaders, HttpStatus.BAD_REQUEST);
        }

        Order order = orderService.getById(id);
        if (order == null) {
            return new ResponseEntity<>("{\"order\":" + id + "}", httpHeaders, HttpStatus.BAD_REQUEST);
        }
        BigDecimal total = order.getTotal();
        BigDecimal money = BigDecimal.valueOf(payment.getTotalMoney()*1.0);

        if (total.compareTo(money) != 0) {
            return new ResponseEntity<>("{\"money\":" + money + "}", httpHeaders, HttpStatus.BAD_REQUEST);
        }

        String uri = configuration.getProperty("PAYMENT_GATEWAY");
        Payment vnpay = vnpayPayload(payment.getBankCode(), payment.getPaymentInfo(), payment.getTransactionId(), payment.getTotalMoney());

        RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<Payment> entity = new HttpEntity<>(vnpay, SessionUtil.getBasicHeader("a42d4482-d5e6-40fc-bc5b-3ea7ec89b66b"));

        ResponseEntity<?> resp = restTemplate.postForEntity(uri, entity, VnpayResponse.class);

        if(resp.getStatusCode()==HttpStatus.OK){
            LOGGER.info(vnpay.getCallbackUrl());
        }else{
            LOGGER.error(String.format("PAYMENT: %s", uri));
        }
        return resp;
    }
    
	private boolean badToken(HttpServletRequest request, String provider) {
        
        String token = request.getHeader(tokenHeader);
        if(token != null && token.contains("Basic")) {
            token = token.substring("Basic ".length(),token.length());
            return !token.equals(provider);
        }
        return true;
    }

	@RequestMapping(value = { "/order/vnpay" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?>  vnpay(@Valid @RequestParam Long id, @Valid @RequestParam String token, HttpServletRequest request, HttpServletResponse response) {
        
        final HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        if (badToken(request,"VNPAY-768968976-jhgcchgssao-13243254-qtrerobn")) {
            return new ResponseEntity<>("{\"provider\": 0}", httpHeaders, HttpStatus.BAD_REQUEST);
        }

        Date date = null;
        try{
            Long time = Long.valueOf(token);
            date = new Date(time);
        }catch(Exception e){
            return new ResponseEntity<>("{\"token\":" + token + "}", httpHeaders, HttpStatus.BAD_REQUEST);
        }
        Order order = orderService.getById(id);
        if (order == null) {
            return new ResponseEntity<>("{\"id\":" + id + "}", httpHeaders, HttpStatus.BAD_REQUEST);
        }
        Date now = new Date(System.currentTimeMillis());
        order.setPaymentTime(now);
        
        try{
            LOGGER.info(String.format("VNPAY buy %s - payment %s - money %s", order.getAuditSection().getDateCreated(), date.toString(), now.toString()));
            orderService.saveOrUpdate(order);
            return new ResponseEntity<>("{\"paymentTime\":" + now + "}", httpHeaders, HttpStatus.OK);
        }catch(Exception e){
            LOGGER.error(String.format("VNPAY setPaymentTime: %s: %s", now.toString(), e.getMessage()));
            return new ResponseEntity<>("{\"paymentTime\":" + e.getMessage() + "}", httpHeaders, HttpStatus.BAD_REQUEST);
        }

        
	}
  
}
