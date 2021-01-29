package com.salesmanager.shop.admin.controller.voucherCode;

import java.io.Serializable;

/**
 * @author Ducdv83@gmail.com
 *
 */
public class VoucherCodeCheck{

	private String code;
	private String securecode;

	public VoucherCodeCheck(){
		
	}
	
	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}

	public String getSecurecode() {
		return securecode;
	}


	public void setSecurecode(String securecode) {
		this.securecode = securecode;
	}
	
}
