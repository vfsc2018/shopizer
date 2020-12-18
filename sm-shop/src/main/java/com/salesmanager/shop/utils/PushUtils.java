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

	final static String SUBSCRIBLE = GATEWAY + "subcribe-topic";
	final static String UNSUBSCRIBLE = GATEWAY + "unsubcribe-topic";
	final static String ACTION_SEND_TO_TOPIC = "push-noti-to-topic";
	final static String ACTION_SEND_TO_TOKEN = "push-noti-with-fcm-token";


	private PushUtils(){

	}
	
	public static boolean subscribe(String token, String topic) {
		NotificationPush packet = new NotificationPush(topic);
		List<String> fcm = new ArrayList<>();
		fcm.add(token);
		packet.setFcmTokens(fcm);
	    RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<NotificationPush> entity = new HttpEntity<>(packet, SessionUtil.getGatewayHeader());

		try{
			ResponseEntity<?> response = restTemplate.postForEntity(SUBSCRIBLE, entity, String.class);
			return (response.getStatusCode()==HttpStatus.OK);
		}catch(Exception e){
			System.out.println("subscribe: " + e.getMessage());
		}
		return false;
	}

	public static boolean unsubscribe(String token, String topic) {
		NotificationPush packet = new NotificationPush(topic);
		List<String> fcm = new ArrayList<>();
		fcm.add(token);
		packet.setFcmTokens(fcm);
	    RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<NotificationPush> entity = new HttpEntity<>(packet, SessionUtil.getGatewayHeader());

		try{
			ResponseEntity<?> response = restTemplate.postForEntity(UNSUBSCRIBLE, entity, String.class);
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
		return send(packet, ACTION_SEND_TO_TOPIC);
	}

	public static boolean toTopic(NotificationPush packet) {
		return send(packet, ACTION_SEND_TO_TOPIC);
	}

	public static boolean toToken(NotificationPush packet) {
		return send(packet, ACTION_SEND_TO_TOKEN);
	}

	

}
