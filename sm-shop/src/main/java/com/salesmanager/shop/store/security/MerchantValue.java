package com.salesmanager.shop.store.security;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MerchantValue implements Serializable {

    private static final long serialVersionUID = 71077883281625L;

	private String storename;
	private String storebank;
	private String storeaccount;
	private String storeaddress;
	private String storephone;
	private String storecity;
	private String storeEmailAddress;

	public MerchantValue(){

	}

	public void setStoreEmailAddress(String storeEmailAddress){
		this.storeEmailAddress = storeEmailAddress;
	}

	public String setStoreEmailAddress(){
		return storeEmailAddress;
	}

	public void setStorecity(String storecity){
		this.storecity = storecity;
	}

	public String getStorecity(){
		return storecity;
	}

	public void setStorephone(String storephone){
		this.storephone = storephone;
	}

	public String getStorephone(){
		return storephone;
	}

	public void setStoreaddress(String storeaddress){
		this.storeaddress = storeaddress;
	}

	public String getStoreaddress(){
		return storeaddress;
	}

    public void setStorebank(String storebank){
		this.storebank = storebank;
	}

	public String getStorebank(){
		return storebank;
	}
	public String getStoreaccount() {
		return storeaccount;
	}

	public void setStoreaccount(String storeaccount) {
		this.storeaccount = storeaccount;
	}
	public void setStorename(String storename){
		this.storename = storename;
	}

	public String getStorename(){
		return storename;
	}
}
