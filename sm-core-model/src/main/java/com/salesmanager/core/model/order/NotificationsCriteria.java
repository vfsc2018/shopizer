package com.salesmanager.core.model.order;

import com.salesmanager.core.model.common.Criteria;

public class NotificationsCriteria extends Criteria {
	
	private String messager = null;
	private String topic = null;
	private String customerName = null;
	private String read = null;
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getRead() {
		return read;
	}
	public void setRead(String read) {
		this.read = read;
	}
	public String getMessager() {
		return messager;
	}
	public void setMessager(String messager) {
		this.messager = messager;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	
	

}
