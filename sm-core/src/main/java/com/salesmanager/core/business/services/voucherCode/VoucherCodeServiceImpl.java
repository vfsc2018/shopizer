package com.salesmanager.core.business.services.voucherCode;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.repositories.voucherCode.VoucherCodeRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.voucherCode.VoucherCode;
import com.salesmanager.core.model.voucherCode.VoucherCodeCriteria;
import com.salesmanager.core.model.voucherCode.VoucherCodeList;

@Service("voucherCodeService")
public class VoucherCodeServiceImpl extends SalesManagerEntityServiceImpl<Long, VoucherCode> implements VoucherCodeService {

	@Inject
	private VoucherCodeRepository voucherCodesRepository;

	
	@Override
	public VoucherCodeList getListByStore(MerchantStore store, VoucherCodeCriteria criteria) {
		return voucherCodesRepository.listByStore(store, criteria);
	}
    
    
	@Inject
	public VoucherCodeServiceImpl(VoucherCodeRepository voucherCodesRepository) {
		super(voucherCodesRepository);
		this.voucherCodesRepository = voucherCodesRepository;
	}
	
	
	public VoucherCode saveVoucher(VoucherCode form) throws BindException {
		return voucherCodesRepository.saveAndFlush(form);
	}
	
	public boolean deleteVoucher(Long id) throws ServiceException {
		voucherCodesRepository.deleteById(id);
		return true;
	}
	
}
