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
import com.salesmanager.core.model.customer.Wallet;
import com.salesmanager.core.model.order.Order;
import com.salesmanager.core.model.payments.PaymentType;
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
        if(payment.getWalletId()!=null && payment.getWalletId()>0){
            payment.setTransactionId(payment.getComplexTransactionId());
        }else{
            Order order = orderService.getById(id);
            if (order == null) {
                return new ResponseEntity<>("{\"order\":" + id + "}", httpHeaders, HttpStatus.BAD_REQUEST);
            }
            BigDecimal total = order.getTotal();
            BigDecimal money = BigDecimal.valueOf(payment.getTotalMoney()*1.0);

            if (total.compareTo(money) != 0) {
                return new ResponseEntity<>("{\"money\":" + money + "}", httpHeaders, HttpStatus.BAD_REQUEST);
            }
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
	public ResponseEntity<?>  vnpay(
        @Valid @RequestParam String id, 
        @Valid @RequestParam String token, 
        @RequestParam String status,
        @RequestParam String transactionId,
        @RequestParam String transactionNo,
        @RequestParam String totalMoney,
        @RequestParam String bankCode, 

        HttpServletRequest request, HttpServletResponse response) {
        
        final HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        if (badToken(request,"VNPAY-768968976-jhgcchgssao-13243254-qtrerobn")) {
            return new ResponseEntity<>("{\"provider\": 0}", httpHeaders, HttpStatus.BAD_REQUEST);
        }
    
        try{
            Long time = Long.valueOf(token);
            Date date = new Date(time);
            System.out.println("PAYMENT token time: " + date.toString());
            
            String details = String.format("{orderId:%s, transaction:%s, bankCode:%s, status:%s, totalMoney:%s}", transactionId, transactionNo, bankCode, status, totalMoney);
            boolean success = status!=null && status.equals(Payment.STATUS_SUCCESS);
            Payment payment = new Payment();
            boolean topup = payment.isComplexTransactionId(id);
            if(topup){
                payment.setComplexTransactionId(id);
                Customer customer = customerService.getById(payment.getCustomerId());
                if(topup(payment, customer, true)){
                    return new ResponseEntity<>("{\"wallet\":" + payment.getWalletId() + "}", httpHeaders, HttpStatus.OK);
                }else{
                    return new ResponseEntity<>("{\"wallet\":" + payment.getWalletId() + ",\"token\":" + token + "}", httpHeaders, HttpStatus.BAD_REQUEST);
                }
            }else{
                Long orderId = Long.parseLong(id);
                if(orderService.paymentConfirm(orderId, true, success, new BigDecimal(totalMoney), details, null)>=0){
                    return new ResponseEntity<>("{\"id\":" + id + "}", httpHeaders, HttpStatus.OK);
                }else{
                    return new ResponseEntity<>("{\"id\":" + id + ",\"token\":" + token + "}", httpHeaders, HttpStatus.BAD_REQUEST);
                }
            }
        }catch(Exception e){
            System.out.println("PAYMENT ERROR: " + e.getMessage());
        }
        return new ResponseEntity<>("{\"token\":" + token + "}", httpHeaders, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = { "/private/payment/wallet" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?>  wallet(@Valid @RequestBody Payment payment, HttpServletRequest request, HttpServletResponse response) {
        final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
       
        if (payment.getCustomerId()==null || payment.getWalletId()!=null){
            return new ResponseEntity<>("{\"Wallet\":" + payment.getCustomerId() + ":" + payment.getWalletId() + "}", httpHeaders, HttpStatus.BAD_REQUEST);
        }

        Long id = Long.valueOf(payment.getTransactionId());
        
        Principal principal = request.getUserPrincipal();
        String userName = principal.getName();

        Customer customer = customerService.getByNick(userName);

        if (customer == null) {
            return new ResponseEntity<>("{\"customer\":" + userName + "}", httpHeaders, HttpStatus.BAD_REQUEST);
        }

        Customer customerPay = customer;
        boolean myMoney = true;
        if(payment.getCustomerId().longValue()!=customer.getId().longValue()){
            myMoney = false;
            customerPay = customerService.getById(payment.getCustomerId());
        }
        Wallet walletPay = customerPay.getWallet();
        // wallet is null || wallet not itergrity || wallet not share to you
        if(walletPay==null || (!myMoney && (walletPay.getShare()==null || walletPay.getId().longValue()!=payment.getWalletId().longValue() || walletPay.getShare().indexOf("#" + customer.getId() + "#")<0))){
            return new ResponseEntity<>("{\"Wallet\":" + payment.getCustomerId() + ":" + payment.getWalletId() + "}", httpHeaders, HttpStatus.BAD_REQUEST);
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

        if (walletPay.getMoney()<total.intValue()) {
            return new ResponseEntity<>("{\"Money in wallet not enought\":" + money + "}", httpHeaders, HttpStatus.BAD_REQUEST);
        }
        try{
            Date date = new Date();
            System.out.println("PAYMENT wallet time: " + date.toString());
            String transactionId = payment.getTransactionId();
            String transactionNo = payment.getTransactionId();
            String bankCode = payment.getBankCode();
            String status = Payment.STATUS_SUCCESS;
            String totalMoney = total.toString();

            String details = String.format("{orderId:%s, transaction:%s, bankCode:%s, status:%s, totalMoney:%s}", transactionId, transactionNo, bankCode, status, totalMoney);
            boolean success = status!=null && status.equals(Payment.STATUS_SUCCESS);
            // paymentConfirm: changeMoney if walletPay is my wallet
            int moneyPay = orderService.paymentConfirm(id, true, success, new BigDecimal(totalMoney), details,  walletPay);
            if(moneyPay>=0){
                if(!myMoney){ // 
                    walletPay.changeMoney(-1*moneyPay);
                    customerPay.setWallet(walletPay);
                    customerService.saveOrUpdate(customerPay);
                }
                return new ResponseEntity<>("{\"id\":" + id + "\"customerId\":" + customerPay.getId() + "\"walletId\":" + walletPay.getId() + "}", httpHeaders, HttpStatus.OK);
            }
        }catch(Exception e){
            System.out.println("PAYMENT WALLET ERROR: " + e.getMessage());
        }
        return new ResponseEntity<>("{\"id\":" + id + "}", httpHeaders, HttpStatus.BAD_REQUEST);
    }

    private boolean topup(Payment payment, Customer customer, boolean confirmed){
        int money = payment.getTotalMoney();

        if (money<10000 || money>50000000) {
            return false;
        }

        Wallet wallet = customer.getWallet();
        if (wallet==null) {
            wallet = new Wallet();
            wallet.setStatus(0);
            wallet.setTopup(0);
            wallet.setMoney(0);
        }
        if(confirmed){
            wallet.setMoney(money);
        }else{
            wallet.setTopup(money);
        }

        try{
            customer.setWallet(wallet);
            customerService.saveOrUpdate(customer);
            return true;
        }catch(Exception e){
            System.out.println("PAYMENT WALLET ERROR: " + e.getMessage());
        }
        return false;
    }

    @RequestMapping(value = { "/private/payment/wallet/topup" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?>  walletTopup(@Valid @RequestBody Payment payment, HttpServletRequest request, HttpServletResponse response) {
        final HttpHeaders httpHeaders= new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
       
        Principal principal = request.getUserPrincipal();
        String userName = principal.getName();
        Customer customer = customerService.getByNick(userName);

        if (customer == null) {
            return new ResponseEntity<>("{\"customer\":" + userName + "}", httpHeaders, HttpStatus.BAD_REQUEST);
        }

        if(topup(payment, customer, false)){
            return new ResponseEntity<>("{\"money\":" + payment.getTotalMoney()  + "}", httpHeaders, HttpStatus.OK);
        }
        return new ResponseEntity<>("{\"money\":" + payment.getTotalMoney()  + "}", httpHeaders, HttpStatus.BAD_REQUEST);
    }
  
}
