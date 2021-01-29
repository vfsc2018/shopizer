package com.salesmanager.shop.store.api.v1.voucher;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salesmanager.core.business.services.voucherCode.VoucherCodeService;
import com.salesmanager.core.model.voucher.Voucher;
import com.salesmanager.core.model.voucherCode.VoucherCode;
import com.salesmanager.shop.admin.controller.voucherCode.VoucherCodeCheck;
import com.salesmanager.shop.utils.VoucherUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@RestController
@RequestMapping("/api/v1")
@Api(tags = { "Voucher management resource (Voucher Management Api)" })
@SwaggerDefinition(tags = { @Tag(name = "Voucher management resource", description = "Manage Voucher") })
public class VoucherApi {

	private static final Logger LOGGER = LoggerFactory.getLogger(VoucherApi.class);

	@Inject
	private VoucherCodeService voucherCodeService;


	private String sha1(String input) 
    { 
        try { 
            // getInstance() method is called with algorithm SHA-1 
            MessageDigest md = MessageDigest.getInstance("SHA-1"); 
            byte[] messageDigest = md.digest(input.getBytes()); 
  
            // Convert byte array into signum representation 
            BigInteger no = new BigInteger(1, messageDigest); 
  
            // Convert message digest into hex value 
            String hashtext = no.toString(16); 
  
            // Add preceding 0s to make it 32 bit 
            while (hashtext.length() < 32) { 
                hashtext = "0" + hashtext; 
            } 
  
            // return the HashText 
            return hashtext; 
        } 
  
        // For specifying wrong message digest algorithms 
        catch (NoSuchAlgorithmException e) { 
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
	
	public VoucherCode getVoucher(VoucherCodeCheck code) {
		
		if (code.getCode() != null && code.getSecurecode()!=null){
			String hash = sha1extra(code.getCode());
			if(hash!=null && hash.equals(code.getSecurecode())){
				long[] k = VoucherUtils.decode(code.getCode());
				if(k.length==3 && k[0]==VoucherUtils.TYPE_ORDER_PAYMENT){
					int index = ((Long)k[2]).intValue();
					VoucherCode voucherCode = voucherCodeService.getVoucherCode(code.getCode());
					if(voucherCode!=null && voucherCode.getIndex()==index && voucherCode.getBlocked()==0 && voucherCode.getUsed()==null){
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

	@PostMapping("/public/voucher/check")
	public ResponseEntity<?> check(@Valid @RequestBody VoucherCodeCheck code, HttpServletRequest request) {
		VoucherCode entity = getVoucher(code);
		if (entity==null){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("{\"voucher\":\"" + entity.getVoucher().getDescription()+ "\"}", HttpStatus.OK);
	}
}
