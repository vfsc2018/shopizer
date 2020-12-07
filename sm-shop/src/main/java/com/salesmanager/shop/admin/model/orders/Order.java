package com.salesmanager.shop.admin.model.orders;

import com.salesmanager.core.model.common.Billing;
import com.salesmanager.core.model.common.Delivery;
import com.salesmanager.core.model.order.orderstatus.OrderStatus;

import javax.persistence.Transient;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;


public class Order implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String orderHistoryComment = "";
	
	private List<OrderStatus> orderStatusList = Arrays.asList(OrderStatus.values());     
	private String datePurchased = "";
	private String paymentTime;
	
	private String fromDate = "";
	private String toDate = "";
	private String dateExported = "";
	
	private  com.salesmanager.core.model.order.Order order;
	
	@Transient
	private Delivery delivery;
	
	@Transient
	private Billing billing;
	
	
	
	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}	
	
	public String getDateExported() {
		return dateExported;
	}

	public void setDateExported(String dateExported) {
		this.dateExported = dateExported;
	}

	public String getDatePurchased() {
		return datePurchased;
	}

	public void setDatePurchased(String datePurchased) {
		this.datePurchased = datePurchased;
	}

	public String getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(String paymentTime) {
		this.paymentTime = paymentTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderHistoryComment() {
		return orderHistoryComment;
	}

	public void setOrderHistoryComment(String orderHistoryComment) {
		this.orderHistoryComment = orderHistoryComment;
	}

	public List<OrderStatus> getOrderStatusList() {
		return orderStatusList;
	}

	public void setOrderStatusList(List<OrderStatus> orderStatusList) {
		this.orderStatusList = orderStatusList;
	}

	public com.salesmanager.core.model.order.Order getOrder() {
		return order;
	}

	public void setOrder(com.salesmanager.core.model.order.Order order) {
		this.order = order;
	}

	public Delivery getDelivery() {
		return delivery;
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}

	public Billing getBilling() {
		return billing;
	}

	public void setBilling(Billing billing) {
		this.billing = billing;
	}




	
}