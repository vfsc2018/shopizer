package com.salesmanager.shop.admin.controller.notifications;

import java.io.Serializable;

public class NotificationForm  implements Serializable{


	
	/**
	 * @author Ducdv83@gmail.com
	 */
	private static final long serialVersionUID = -116566484482830743L;
	private Long customerId;
	private Long orderId;
	private String message;	
	private String topic;
	
	
	
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}	
	
	
}
