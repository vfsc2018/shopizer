package com.salesmanager.core.business.repositories.voucherCode;

import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.voucherCode.VoucherCode;
import com.salesmanager.core.model.voucherCode.VoucherCodeCriteria;
import com.salesmanager.core.model.voucherCode.VoucherCodeList;

public interface VoucherCodeRepositoryCustom {

	VoucherCodeList listByStore(MerchantStore store, VoucherCodeCriteria criteria);
	int countCodeByVoucherId(Long voucherId);
	public int getMaxIndexByVoucherId(Long voucherId);
	public VoucherCode getVoucherCode(Long voucherId, Integer index);
	public VoucherCode getVoucherCode(String code);
	

}
