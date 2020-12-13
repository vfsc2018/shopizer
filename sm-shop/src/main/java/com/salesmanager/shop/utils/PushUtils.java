package com.salesmanager.shop.utils;

import java.util.ArrayList;
import java.util.List;
import com.salesmanager.shop.store.security.NotificationPush;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class PushUtils {
	
	final static String GATEWAY="http://payment.vifotec.com:8080/api/notifications/";

	private PushUtils(){

	}
	
	public static boolean subsribe(String token, String topic) {
		NotificationPush packet = new NotificationPush(topic);
		List<String> fcm = new ArrayList<>();
		fcm.add(token);
		packet.setFcmTokens(fcm);
	    RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<NotificationPush> entity = new HttpEntity<>(packet, SessionUtil.getGatewayHeader());

		try{
			ResponseEntity<?> response = restTemplate.postForEntity(GATEWAY + "subcribe-topic", entity, String.class);
			return (response.getStatusCode()==HttpStatus.OK);
		}catch(Exception e){
			System.out.println("subsribe: " + e.getMessage());
		}
		return false;
	}

	public static boolean unsubsribe(String token, String topic) {
		NotificationPush packet = new NotificationPush(topic);
		List<String> fcm = new ArrayList<>();
		fcm.add(token);
		packet.setFcmTokens(fcm);
	    RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<NotificationPush> entity = new HttpEntity<>(packet, SessionUtil.getGatewayHeader());

		try{
			ResponseEntity<?> response = restTemplate.postForEntity(GATEWAY + "unsubcribe-topic", entity, String.class);
			return (response.getStatusCode()==HttpStatus.OK);
		}catch(Exception e){
			System.out.println("unsubsribe: " + e.getMessage());
		}
		return false;
	}

	private static boolean send(NotificationPush packet, String action) {
		
	    RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<NotificationPush> entity = new HttpEntity<>(packet, SessionUtil.getGatewayHeader());

		try{
			ResponseEntity<?> response = restTemplate.postForEntity(GATEWAY + action, entity, String.class);
			return (response.getStatusCode()==HttpStatus.OK);
		}catch(Exception e){
			System.out.println("action: " + e.getMessage());
		}
		return false;
	}

	public static boolean toAll(NotificationPush packet) {
		packet.setTopic("all");
		return send(packet, "push-noti-to-topic");
	}

	public static boolean toTopic(NotificationPush packet) {
		return send(packet, "push-noti-to-topic");
	}

	public static boolean toToken(NotificationPush packet) {
		return send(packet, "push-noti-with-fcm-token");
	}

	

}
