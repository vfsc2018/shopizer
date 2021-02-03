package com.salesmanager.shop.store.api.v1.voucher;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salesmanager.core.business.services.voucher.VoucherService;
import com.salesmanager.core.model.voucher.Voucher;
import com.salesmanager.core.model.vouchercode.VoucherCode;
import com.salesmanager.shop.admin.controller.vouchercode.VoucherCodeCheck;
import com.salesmanager.shop.admin.controller.vouchercode.VoucherInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@RestController
@RequestMapping("/api/v1")
@Api(tags = { "Voucher management resource (Voucher Management Api)" })
@SwaggerDefinition(tags = { @Tag(name = "Voucher management resource", description = "Manage Voucher") })
public class VoucherApi {

	@Inject
	private VoucherService voucherService;

	@PostMapping("/private/voucher/check")
	public ResponseEntity<?> check(@Valid @RequestBody VoucherCodeCheck code, HttpServletRequest request) {
		VoucherCode entity = voucherService.getVoucher(code.getCode(), code.getSecurecode());
		if (entity==null){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Voucher v = entity.getVoucher();
		VoucherInfo info = new VoucherInfo();
		info.setDescription(v.getDescription());
		info.setCode(v.getCode());
		if(v.getPoint()!=null){
			info.setPoint(v.getPoint());
		}
		if(v.getDiscount()!=null && v.getDiscount().intValue()>0){
			info.setDiscount(v.getDiscount());
		}
		if(v.getPercent()!=null && v.getPercent().intValue()>0){
			info.setPercent(v.getPercent());
		}
		if(StringUtils.isNotEmpty(v.getProductSku())){
			info.setProduct(v.getProductSku());
		}
		return new ResponseEntity<>(info, HttpStatus.OK);
	}
}
