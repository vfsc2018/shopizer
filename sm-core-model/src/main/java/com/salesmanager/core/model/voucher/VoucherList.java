package com.salesmanager.core.model.voucher;

import java.util.ArrayList;
import java.util.List;

import com.salesmanager.core.model.common.EntityList;


public class VoucherList extends EntityList {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2945008499870663935L;

	public List<Voucher> getVouchers() {
		return vouchers;
	}

	public void setVouchers(List<Voucher> vouchers) {
		this.vouchers = vouchers;
	}

	private List<Voucher> vouchers = new ArrayList<>();
	


}
