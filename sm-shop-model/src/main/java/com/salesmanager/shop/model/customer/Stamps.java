package com.salesmanager.shop.model.customer;

import java.math.BigDecimal;

import com.salesmanager.core.model.reference.currency.Currency;

public class Stamps {
	private String productName;
	private String sku;
	private BigDecimal price;
	private Currency currency;
	
	
	
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
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
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
