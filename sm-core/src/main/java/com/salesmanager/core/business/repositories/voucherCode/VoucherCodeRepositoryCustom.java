package com.salesmanager.core.business.repositories.vouchercode;

import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.vouchercode.VoucherCode;
import com.salesmanager.core.model.vouchercode.VoucherCodeCriteria;
import com.salesmanager.core.model.vouchercode.VoucherCodeList;

public interface VoucherCodeRepositoryCustom {

	VoucherCodeList listByStore(MerchantStore store, VoucherCodeCriteria criteria);
	int countCodeByVoucherId(Long voucherId);
	public int getMaxIndexByVoucherId(Long voucherId);
	public VoucherCode getVoucherCode(Long voucherId, Integer index);
	public VoucherCode getVoucherCode(String code);
	

}
