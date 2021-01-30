package com.salesmanager.core.business.services.voucher;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;

import com.salesmanager.core.business.exception.ServiceException;
import com.salesmanager.core.business.repositories.voucher.VoucherRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.services.voucherCode.VoucherCodeService;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.voucher.Voucher;
import com.salesmanager.core.model.voucher.VoucherCriteria;
import com.salesmanager.core.model.voucher.VoucherList;
import com.salesmanager.core.model.voucherCode.VoucherCode;

@Service("voucherService")
public class VoucherServiceImpl extends SalesManagerEntityServiceImpl<Long, Voucher> implements VoucherService {

	@Inject
	private VoucherRepository voucherRepository;

	@Inject
	private VoucherCodeService voucherCodeService;


	private String sha1(String input) 
    { 
        try { 
            MessageDigest mDigest = MessageDigest.getInstance("SHA1");
			byte[] result = mDigest.digest(input.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < result.length; i++) {
				sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
			}
        	return sb.toString();
        }catch (NoSuchAlgorithmException e) { 
            return null;
        } 
	} 

	private String sha1extra(String code) 
    { 
		String hash = sha1(code);
		if(hash==null) return null;
		Calendar cal = Calendar.getInstance();

        int day = cal.get(Calendar.DAY_OF_MONTH); 
        int month = cal.get(Calendar.MONTH); 
  
		return sha1(day + hash + month);
	}
	@Override
	public VoucherCode getVoucher(String code, String securecode) {
		System.out.println("getVoucher:" + code + " = " + securecode);
		if (code != null && securecode!=null){
			String hash = sha1extra(code);
			System.out.println("getVoucher Hash:" + hash);
			if(hash!=null && hash.equals(securecode)){
				long[] k = voucherCodeService.decode(code);
				for(int i=0;i<k.length;i++){
					System.out.println("getVoucher decode [" + i + "] = " + k[i]);
				}
				
				if(k.length==3 && k[0]==VoucherCodeService.TYPE_ORDER_PAYMENT){
					int index = ((Long)k[2]).intValue();
					VoucherCode voucherCode = voucherCodeService.getVoucherCode(code);
					if(voucherCode!=null && voucherCode.getIndex()==index && voucherCode.getBlocked()==0 && voucherCode.getUsed()==null){
						System.out.println("getVoucherCode genuine: " + voucherCode.getId());
						Voucher voucher = voucherCode.getVoucher();
						if (voucher==null || voucher.getId().longValue()!=k[1] || voucher.getBlocked()>0 || voucher.getEndDate()==null || voucher.getEndDate().before(new Date()) || voucher.getStartDate()==null || voucher.getStartDate().after(new Date())){
							return null;
						}
						return voucherCode;
					}
				}
			}
		}
		return null;
	}

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
