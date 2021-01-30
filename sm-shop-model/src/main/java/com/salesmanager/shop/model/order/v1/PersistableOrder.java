package com.salesmanager.shop.model.order.v1;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.salesmanager.core.model.voucherCode.VoucherCode;
import com.salesmanager.shop.model.order.transaction.PersistablePayment;

/**
 * This object is used when processing an order from the API
 * It will be used for processing the payment and as Order meta data
 * @author c.samson
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersistableOrder extends Order {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private PersistablePayment payment;
	private Long shippingQuote;
	@JsonIgnore
	private Long shoppingCartId;
	@JsonIgnore
	private Long customerId;

	private String code;
	private String securecode;

	@Transient
	private VoucherCode voucherCode;
	
	public VoucherCode getVoucherCode() {
		return voucherCode;
	}

	public void setVoucherCode(VoucherCode voucherCode) {
		this.voucherCode = voucherCode;
	}

	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}

	public String getSecurecode() {
		return securecode;
	}


	public void setSecurecode(String securecode) {
		this.securecode = securecode;
	}
	
	public Long getShoppingCartId() {
		return shoppingCartId;
	}

	public void setShoppingCartId(Long shoppingCartId) {
		this.shoppingCartId = shoppingCartId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public PersistablePayment getPayment() {
		return payment;
	}

	public void setPayment(PersistablePayment payment) {
		this.payment = payment;
	}

	public Long getShippingQuote() {
		return shippingQuote;
	}

	public void setShippingQuote(Long shippingQuote) {
		this.shippingQuote = shippingQuote;
	}
	


}
