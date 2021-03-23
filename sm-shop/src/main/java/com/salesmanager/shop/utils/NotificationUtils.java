package com.salesmanager.shop.utils;

import javax.inject.Inject;

import com.salesmanager.core.business.repositories.notifications.NotificationsRepository;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.message.Notifications;
import com.salesmanager.core.model.order.Order;

import org.springframework.stereotype.Service;


@Service("NotificationUtils")
public class NotificationUtils {

	@Inject
	private NotificationsRepository notificationsRepository;
	
	public boolean createAfterOrder(Customer customer, Order order) {
		Notifications noti = new Notifications();
		String topic = customer.getNick();
		long orderId = order.getId();
		String data = "{\"orderId\":" + orderId + ",\"status\":" + "\"" + order.getStatus().name() + "\"}";
		String message = "Chuoi VfSC food da nhan duoc don hang #" + orderId + ". Quy khach hay thuc hien thanh toan de nhan hang.";
		noti.setCustomer(customer);
		noti.setOrder(order);
		noti.setMessage(message);
		noti.setTopic(topic);
		Notifications result = notificationsRepository.save(noti);
		String token = customer.getFcmtoken();
		if(token!=null && result.getId()!=null && result.getId().longValue()>0){
			return PushUtils.confirmAfterOrder(token, orderId, message, data);
			
		}
		return false;
	}

	public boolean sendCustomer(Customer customer, String msg, String title) {
		Notifications noti = new Notifications();
		String topic = customer.getNick();
		String data = "{\"message\":" + msg + "\"}";
		String message = msg;
		noti.setCustomer(customer);
		noti.setMessage(message);
		noti.setTopic(topic);
		Notifications result = notificationsRepository.save(noti);
		String token = customer.getFcmtoken();
		if(token!=null && result.getId()!=null && result.getId().longValue()>0){
			return PushUtils.sendCustomer(token, message, data, title);
			
		}
		return false;
	}
	public boolean sendAllCustomer(String msg, String title) {
		Notifications noti = new Notifications();
		String topic = "all";
		String data = "{\"message\":" + msg + "\"}";
		String message = msg;
		noti.setMessage(message);
		noti.setTopic(topic);
		Notifications result = notificationsRepository.save(noti);
		if(result.getId()!=null && result.getId().longValue()>0){
			return PushUtils.sendAllCustomer(message, data, title);
			
		}
		return false;
	}

}
