package com.salesmanager.core.model.vouchercode;

import java.util.ArrayList;
import java.util.List;

import com.salesmanager.core.model.common.EntityList;


public class VoucherCodeList extends EntityList {


	private static final long serialVersionUID = -2945008499870663935L;


	private List<VoucherCode> voucherCodes = new ArrayList<>();


	public List<VoucherCode> getVoucherCodes() {
		return voucherCodes;
	}


	public void setVoucherCodes(List<VoucherCode> voucherCodes) {
		this.voucherCodes = voucherCodes;
	}
	


}
