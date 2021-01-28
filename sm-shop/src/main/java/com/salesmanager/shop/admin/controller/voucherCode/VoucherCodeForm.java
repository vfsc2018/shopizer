package com.salesmanager.shop.admin.controller.voucherCode;

import java.io.Serializable;

/**
 * @author Ducdv83@gmail.com
 *
 */
public class VoucherCodeForm  implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6457270709635287365L;
	private Long id;
	private Long voucherId;
	private int index;
	private Integer amtCode;
	private String batch;
	
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}

	public Integer getAmtCode() {
		return amtCode;
	}
	public void setAmtCode(Integer amtCode) {
		this.amtCode = amtCode;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	private int blocked;
	private String blockMessage;
	public int getBlocked() {
		return blocked;
	}
	public void setBlocked(int blocked) {
		this.blocked = blocked;
	}
	public String getBlockMessage() {
		return blockMessage;
	}
	public void setBlockMessage(String blockMessage) {
		this.blockMessage = blockMessage;
	}
	private String code;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	private String securecode;
	private Long customerId ;
	private String used;
	private String redeem;
	private Long orderId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getVoucherId() {
		return voucherId;
	}
	public void setVoucherId(Long voucherId) {
		this.voucherId = voucherId;
	}
	public String getSecurecode() {
		return securecode;
	}
	public void setSecurecode(String securecode) {
		this.securecode = securecode;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
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
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
}
