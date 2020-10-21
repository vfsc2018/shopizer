package com.salesmanager.core.model.order.orderproduct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.salesmanager.core.model.reference.currency.Currency;



public class OrderProductEx {
	private String productName;
	private String sku;
	private int productQuantity;
	private BigDecimal oneTimeCharge;
	private Currency currency;
	private BigDecimal total;
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
	public BigDecimal getOneTimeCharge() {
		return oneTimeCharge;
	}
	public void setOneTimeCharge(BigDecimal oneTimeCharge) {
		this.oneTimeCharge = oneTimeCharge;
	}
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	
	
	
}
