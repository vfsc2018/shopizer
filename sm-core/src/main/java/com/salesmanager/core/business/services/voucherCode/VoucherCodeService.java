package com.salesmanager.core.business.services.voucherCode;

import org.springframework.validation.BindException;

import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.voucherCode.VoucherCode;
import com.salesmanager.core.model.voucherCode.VoucherCodeCriteria;
import com.salesmanager.core.model.voucherCode.VoucherCodeList;

public interface VoucherCodeService extends SalesManagerEntityService<Long, VoucherCode> {
	
	public VoucherCode saveVoucher(VoucherCode form) throws BindException;
	VoucherCodeList getListByStore(MerchantStore store, VoucherCodeCriteria criteria);
}
