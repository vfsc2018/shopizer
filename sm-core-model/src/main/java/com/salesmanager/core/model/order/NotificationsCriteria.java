package com.salesmanager.core.model.order;

import com.salesmanager.core.model.common.Criteria;

public class NotificationsCriteria extends Criteria {
	
	private String message = null;
	private String topic = null;
	private String customerName = null;
	private Boolean read = null;
	private Long id;

	public Long getId(){
		return id;
	}
	public void setId(Long id){
		this.id = id;
	}
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Boolean getRead() {
		return read;
	}
	public void setRead(Boolean read) {
		this.read = read;
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
