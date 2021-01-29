package com.salesmanager.core.business.services.voucherCode;


import java.util.List;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.voucherCode.VoucherCode;
import com.salesmanager.core.model.voucherCode.VoucherCodeCriteria;
import com.salesmanager.core.model.voucherCode.VoucherCodeList;

public interface VoucherCodeService extends SalesManagerEntityService<Long, VoucherCode> {
	
	public VoucherCode saveVoucher(VoucherCode code);
	public List<?> saveVoucher(List<VoucherCode> code);
	VoucherCodeList getListByStore(MerchantStore store, VoucherCodeCriteria criteria);
	public boolean deleteVoucher(Long id) throws ServiceException;
	public int countCodeByVoucherId(Long voucherId);
	public int getMaxIndexByVoucherId(Long voucherId);
	public VoucherCode getVoucherCode(Long voucherId, Integer index);
	public VoucherCode getVoucherCode(String code);
}
