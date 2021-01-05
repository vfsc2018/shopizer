package com.salesmanager.core.business.services.voucher;

import org.springframework.validation.BindException;

import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.voucher.Voucher;
import com.salesmanager.core.model.voucher.VoucherCriteria;
import com.salesmanager.core.model.voucher.VoucherList;

public interface VoucherService extends SalesManagerEntityService<Long, Voucher> {
	
	public Voucher saveVoucher(Voucher form) throws BindException;
	VoucherList getListByStore(MerchantStore store, VoucherCriteria criteria);
}
