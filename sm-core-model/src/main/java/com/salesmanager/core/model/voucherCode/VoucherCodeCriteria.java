package com.salesmanager.core.model.vouchercode;

import com.salesmanager.core.model.common.Criteria;

public class VoucherCodeCriteria extends Criteria {
	
	private Long id;
	private Long voucherId;
	public Long getVoucherId() {
		return voucherId;
	}
	public void setVoucherId(Long voucherId) {
		this.voucherId = voucherId;
	}
	private String batch;
	private String securecode;
	private String used ;
	private String redeem;
	private Long customerId;
	private boolean blocked;
	private boolean available;

	public boolean getAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}

	public boolean getBlocked() {
		return blocked;
	}
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	private Long orderId;
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getSecurecode() {
		return securecode;
	}
	public void setSecurecode(String securecode) {
		this.securecode = securecode;
	}
	public String getUsed() {
		return used;
	}
	public void setUsed(String used) {
		this.used = used;
	}
	public String getRedeem() {
		return redeem;
	}
	public void setRedeem(String redeem) {
		this.redeem = redeem;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}
	
}
