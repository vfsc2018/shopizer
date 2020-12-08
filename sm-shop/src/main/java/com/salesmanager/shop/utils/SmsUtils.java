package com.salesmanager.shop.utils;

import com.salesmanager.shop.model.user.Sms;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class SmsUtils {
	
	private SmsUtils(){

	}
	
	public static boolean sendTextMessage(String telephone, String text) {
		final String SMS_GATEWAY="http://payment.vifotec.com:8080/api/sms/send-sms";
		
		if(telephone==null || telephone.trim().length()<10){
			System.out.println("SMS wrong phone:" + text);
			return false;
		}
		String phone = telephone.trim().replace(" ", "");
		if(phone.trim().length()!=10){
			System.out.println("SMS wrong phone:" + phone);
			return false;
		}

		Sms sms = new Sms();
		sms.setPhone(phone);
		sms.setText(text);
	    RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<Sms> entity = new HttpEntity<>(sms, SessionUtil.getBasicHeader("a42d4482-d5e6-40fc-bc5b-3ea7ec89b66b"));

		try{
			ResponseEntity<?> response = restTemplate.postForEntity(SMS_GATEWAY, entity, String.class);
			return (response.getStatusCode()==HttpStatus.OK);
		}catch(Exception e){
			System.out.println("SMS:" + e.getMessage());
		}
		return false;
		
	}

}
