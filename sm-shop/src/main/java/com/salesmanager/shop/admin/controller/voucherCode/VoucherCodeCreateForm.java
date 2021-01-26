package com.salesmanager.shop.admin.controller.voucherCode;

import java.io.Serializable;

/**
 * @author Ducdv83@gmail.com
 *
 */
public class VoucherCodeCreateForm  implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 5019056996856028014L;
	private Long voucherId;
	private int index;
	private int amtCode;
	
	
	public Long getVoucherId() {
		return voucherId;
	}
	public void setVoucherId(Long voucherId) {
		this.voucherId = voucherId;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getAmtCode() {
		return amtCode;
	}
	public void setAmtCode(int amtCode) {
		this.amtCode = amtCode;
	}
	
	
}
