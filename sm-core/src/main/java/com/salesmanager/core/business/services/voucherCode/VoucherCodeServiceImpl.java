package com.salesmanager.core.business.services.voucherCode;

import java.util.List;

import javax.inject.Inject;

import org.hashids.Hashids;
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


	final static String alphabet = "78912346QAZWSXEDCRFVTGBYHNUJMKLP";
	final static int min = 12;

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
	
	@Override
	public VoucherCode saveVoucher(VoucherCode code) {
		return voucherCodesRepository.saveAndFlush(code); 
	}
	@Override
	public  List<?> saveVoucher(List<VoucherCode> code) {
		return voucherCodesRepository.saveAll(code);
	}
	@Override
	public boolean deleteVoucher(Long id) throws ServiceException {
		voucherCodesRepository.deleteById(id);
		return true;
	}
	@Override
	public int countCodeByVoucherId(Long voucherId){
		return voucherCodesRepository.countCodeByVoucherId(voucherId);
	}
	@Override
	public VoucherCode getVoucherCode(Long voucherId, Integer index) {
		return voucherCodesRepository.getVoucherCode(voucherId, index);
	}
	@Override
	public VoucherCode getVoucherCode(String code) {
		return voucherCodesRepository.getVoucherCode(code);
	}
	@Override
	public int getMaxIndexByVoucherId(Long voucherId){
		return  voucherCodesRepository.getMaxIndexByVoucherId(voucherId);
	}
	@Override
	public String encode(Long type, Long a, Long b) {
		Hashids hashids = new Hashids("o", min, alphabet);
		return hashids.encode(type, a, b);
	}
	@Override
	public long[] decode(String code) {
		Hashids hashids = new Hashids("o", min, alphabet);
		return hashids.decode(code);
	}
}
