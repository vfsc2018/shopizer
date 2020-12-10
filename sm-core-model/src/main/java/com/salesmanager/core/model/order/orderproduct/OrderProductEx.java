package com.salesmanager.core.model.order.orderproduct;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.salesmanager.core.model.order.orderstatus.OrderStatus;
import com.salesmanager.core.model.reference.currency.Currency;



public class OrderProductEx implements Serializable {
	



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	List<OrderStatus> orderStatusList = Arrays.asList(OrderStatus.values()); 
	
	
	private Long id;
	
	private Long parentId;
	private String phone;
	private String address;

	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	private String productName;
	private String sku;
	private Double productQuantity;
	private Integer oneTimeCharge;
	private Currency currency;
	private Double total;
	private String status;
	private String unit;
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	private String description;
	
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<OrderStatus> getOrderStatusList() {
		return orderStatusList;
	}

	public void setOrderStatusList(List<OrderStatus> orderStatusList) {
		this.orderStatusList = orderStatusList;
	}
	private String dateExported ="";
	
	public String getDateExported() {
		return dateExported;
	}
	public void setDateExported(String dateExported) {
		this.dateExported = dateExported;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	private List<OrderProductEx> relationships = new ArrayList<>();
	
	
	public List<OrderProductEx> getRelationships() {
		return relationships;
	}
	public void setRelationships(List<OrderProductEx> relationships) {
		this.relationships = relationships;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public Double getProductQuantity() {
		return productQuantity;
	}
	public void setProductQuantity(Double productQuantity) {
		this.productQuantity = productQuantity;
	}
	public Integer getOneTimeCharge() {
		return oneTimeCharge;
	}
	public void setOneTimeCharge(Integer oneTimeCharge) {
		this.oneTimeCharge = oneTimeCharge;
	}
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}

	
	
	
}
