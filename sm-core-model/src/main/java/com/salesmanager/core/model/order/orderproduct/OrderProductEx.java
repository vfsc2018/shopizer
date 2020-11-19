package com.salesmanager.core.model.order.orderproduct;

import java.io.Serializable;
import java.math.BigDecimal;
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
	
	
	private int id;
	
	private int parentId;


	private String productName;
	private String sku;
	private int productQuantity;
	private Integer oneTimeCharge;
	private Currency currency;
	private Integer total;
	private String status;
	private String description;
	
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}	
	public int getId() {
		return id;
	}
	public void setId(int id) {
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
	private List<OrderProductEx> relationships = new ArrayList<OrderProductEx>();
	
	
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
	public int getProductQuantity() {
		return productQuantity;
	}
	public void setProductQuantity(int productQuantity) {
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
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}

	
	
	
}
