package com.salesmanager.core.business.modules.order.total;

public class OrderTotalResponse {
	
	private Double discount = null;
	private Double moneyoff = null;
	private String expiration;
	private int type = 0;
	
	public Double getMoneyoff() {
		return moneyoff;
	}
	public void setMoneyoff(Double moneyoff) {
		this.moneyoff = moneyoff;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getExpiration() {
		return expiration;
	}

	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}

}
