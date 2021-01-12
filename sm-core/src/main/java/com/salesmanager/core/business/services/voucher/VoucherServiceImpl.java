package com.salesmanager.core.business.services.voucher;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.repositories.voucher.VoucherRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.voucher.Voucher;
import com.salesmanager.core.model.voucher.VoucherCriteria;
import com.salesmanager.core.model.voucher.VoucherList;

@Service("voucherService")
public class VoucherServiceImpl extends SalesManagerEntityServiceImpl<Long, Voucher> implements VoucherService {

	@Inject
	private VoucherRepository vouchersRepository;

	
	@Override
	public VoucherList getListByStore(MerchantStore store, VoucherCriteria criteria) {
		return vouchersRepository.listByStore(store, criteria);
	}
    
    
	@Inject
	public VoucherServiceImpl(VoucherRepository vouchersRepository) {
		super(vouchersRepository);
		this.vouchersRepository = vouchersRepository;
	}
	
	
	public Voucher saveVoucher(Voucher form) throws BindException {
		return vouchersRepository.saveAndFlush(form);
	}

	public boolean deleteVoucher(Long id) throws ServiceException {
		vouchersRepository.deleteById(id);
		return true;

	}
	
}
