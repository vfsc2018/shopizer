package com.salesmanager.core.business.services.voucher;

import java.util.List;

import org.springframework.validation.BindException;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.voucher.Voucher;
import com.salesmanager.core.model.voucher.VoucherCriteria;
import com.salesmanager.core.model.voucher.VoucherList;
import com.salesmanager.core.model.voucherCode.VoucherCode;

public interface VoucherService extends SalesManagerEntityService<Long, Voucher> {
	
	public Voucher saveVoucher(Voucher form) throws BindException;
	VoucherList getListByStore(MerchantStore store, VoucherCriteria criteria);
	public boolean deleteVoucher(Long id) throws ServiceException;
	public List<Voucher> getVoucherEndDate();
	public VoucherCode getVoucher(String code, String securecode);
	
}
