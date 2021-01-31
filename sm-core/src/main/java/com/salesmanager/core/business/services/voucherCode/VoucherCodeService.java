package com.salesmanager.core.business.services.vouchercode;


import java.util.List;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.vouchercode.VoucherCode;
import com.salesmanager.core.model.vouchercode.VoucherCodeCriteria;
import com.salesmanager.core.model.vouchercode.VoucherCodeList;

public interface VoucherCodeService extends SalesManagerEntityService<Long, VoucherCode> {
	

	public final static long TYPE_ORDER_PAYMENT = 3;

	public VoucherCode saveVoucher(VoucherCode code);
	public List<?> saveVoucher(List<VoucherCode> code);
	VoucherCodeList getListByStore(MerchantStore store, VoucherCodeCriteria criteria);
	public boolean deleteVoucher(Long id) throws ServiceException;
	public int countCodeByVoucherId(Long voucherId);
	public int getMaxIndexByVoucherId(Long voucherId);
	public VoucherCode getVoucherCode(Long voucherId, Integer index);
	public VoucherCode getVoucherCode(String code);

	public String encode(Long type, Long a, Long b);

	public long[] decode(String code);
}
