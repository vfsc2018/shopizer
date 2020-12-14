package com.salesmanager.shop.model.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.salesmanager.core.model.order.orderstatus.OrderStatus;
import com.salesmanager.core.model.order.payment.CreditCard;
import com.salesmanager.core.model.payments.PaymentType;
import com.salesmanager.core.utils.CloneUtils;
// import com.salesmanager.shop.model.order.total.OrderTotal;
import com.salesmanager.shop.model.order.v0.Order;
import com.salesmanager.shop.model.order.total.OrderTotal;

public class OrderEntity extends Order {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<OrderTotal> totals;
	private List<OrderAttribute> attributes = new ArrayList<>();
	
	private PaymentType paymentType;
	private String paymentModule;
	private String shippingModule;
	private List<OrderStatus> previousOrderStatus;
	private OrderStatus orderStatus;
	private CreditCard creditCard;
	private Date datePurchased;
	private Date fromDate;
	private Date toDate;
	private Date paymentTime;
	private String currency;
	private boolean customerAgreed;
	private boolean confirmedAddress;
	private String comments;
	private Long customerId;
	

	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public void setTotals(List<OrderTotal> totals) {
		this.totals = totals;
	}
	public List<OrderTotal> getTotals() {
		return totals;
	}
	public PaymentType getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}
	public String getPaymentModule() {
		return paymentModule;
	}
	public void setPaymentModule(String paymentModule) {
		this.paymentModule = paymentModule;
	}
	public String getShippingModule() {
		return shippingModule;
	}
	public void setShippingModule(String shippingModule) {
		this.shippingModule = shippingModule;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}
	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public Date getPaymentTime() {
		return CloneUtils.clone(paymentTime);
	}

	public void setPaymentTime(Date paymentTime) {
		this.paymentTime = CloneUtils.clone(paymentTime);
	}

	public Date getFromDate() {
		return CloneUtils.clone(fromDate);
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = CloneUtils.clone(fromDate);
	}

	public Date getToDate() {
		return CloneUtils.clone(toDate);
	}

	public void setToDate(Date toDate) {
		this.toDate = CloneUtils.clone(toDate);
	}
	public Date getDatePurchased() {
		return datePurchased;
	}
	public void setDatePurchased(Date datePurchased) {
		this.datePurchased = datePurchased;
	}
	public void setPreviousOrderStatus(List<OrderStatus> previousOrderStatus) {
		this.previousOrderStatus = previousOrderStatus;
	}
	public List<OrderStatus> getPreviousOrderStatus() {
		return previousOrderStatus;
	}
	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public boolean isCustomerAgreed() {
		return customerAgreed;
	}
	public void setCustomerAgreed(boolean customerAgreed) {
		this.customerAgreed = customerAgreed;
	}
	public boolean isConfirmedAddress() {
		return confirmedAddress;
	}
	public void setConfirmedAddress(boolean confirmedAddress) {
		this.confirmedAddress = confirmedAddress;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public List<OrderAttribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<OrderAttribute> attributes) {
		this.attributes = attributes;
	}


}
