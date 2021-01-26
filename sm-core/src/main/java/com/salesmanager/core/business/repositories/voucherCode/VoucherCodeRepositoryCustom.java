package com.salesmanager.core.business.repositories.voucherCode;

import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.voucherCode.VoucherCodeCriteria;
import com.salesmanager.core.model.voucherCode.VoucherCodeList;

public interface VoucherCodeRepositoryCustom {

	VoucherCodeList listByStore(MerchantStore store, VoucherCodeCriteria criteria);
	int getVoucherCodeByVoucherId(Long voucherId);
	public int countGrByVoucherId(Long voucherId);

}
