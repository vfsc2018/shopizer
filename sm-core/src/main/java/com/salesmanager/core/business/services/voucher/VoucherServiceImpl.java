package com.salesmanager.core.business.services.voucher;

import java.util.List;

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
	private VoucherRepository voucherRepository;

	@Override
	public VoucherList getListByStore(MerchantStore store, VoucherCriteria criteria) {
		return voucherRepository.listByStore(store, criteria);
	}
    
	@Override
	public List<Voucher> getVoucherEndDate(){
		return voucherRepository.getVoucherEndDate();
	}
    
	@Inject
	public VoucherServiceImpl(VoucherRepository voucherRepository) {
		super(voucherRepository);
		this.voucherRepository = voucherRepository;
	}
	
	
	public Voucher saveVoucher(Voucher form) throws BindException {
		return voucherRepository.saveAndFlush(form);
	}

	public boolean deleteVoucher(Long id) throws ServiceException {
		voucherRepository.deleteById(id);
		return true;

	}
	
}
