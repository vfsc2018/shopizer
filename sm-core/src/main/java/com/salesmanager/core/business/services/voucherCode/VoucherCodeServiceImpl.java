package com.salesmanager.core.business.services.vouchercode;

import java.util.List;

import javax.inject.Inject;

import org.hashids.Hashids;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.constants.Constants;
import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.repositories.vouchercode.VoucherCodeRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.vouchercode.VoucherCode;
import com.salesmanager.core.model.vouchercode.VoucherCodeCriteria;
import com.salesmanager.core.model.vouchercode.VoucherCodeList;

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
		Hashids hashids = new Hashids("o", Constants.CODE_MINLEN, Constants.CODE_ALPHABET);
		return hashids.encode(type, a, b);
	}
	@Override
	public long[] decode(String code) {
		Hashids hashids = new Hashids("o", Constants.CODE_MINLEN, Constants.CODE_ALPHABET);
		return hashids.decode(code);
	}
}
