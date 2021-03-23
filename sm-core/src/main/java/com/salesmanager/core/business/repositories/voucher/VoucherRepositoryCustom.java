package com.salesmanager.core.business.repositories.voucher;

import java.util.List;

import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.voucher.Voucher;
import com.salesmanager.core.model.voucher.VoucherCriteria;
import com.salesmanager.core.model.voucher.VoucherList;

public interface VoucherRepositoryCustom {

	VoucherList listByStore(MerchantStore store, VoucherCriteria criteria);
	public List<Voucher> getActiveVoucher();
	public Voucher getVoucher(String code);

}
