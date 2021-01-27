package com.salesmanager.core.model.voucherCode;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.json.simple.JSONAware;

import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.model.common.audit.AuditListener;
import com.salesmanager.core.model.common.audit.AuditSection;
import com.salesmanager.core.model.common.audit.Auditable;
import com.salesmanager.core.model.customer.Customer;
import com.salesmanager.core.model.generic.SalesManagerEntity;
import com.salesmanager.core.model.order.Order;
import com.salesmanager.core.model.voucher.Voucher;


@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "VOUCHER_CODE", schema= SchemaConstant.SALESMANAGER_SCHEMA)
public class VoucherCode extends SalesManagerEntity<Long, VoucherCode> implements Auditable, JSONAware {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "VOUCHER_CODE_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="VOUCHER_ID", nullable=true)
	private Voucher voucher;
	
	private String code;
	private String securecode;
	private String batch;
	private int blocked;
	private String blockMessage;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CUSTOMER_ID", nullable=true)
	private Customer customer;
	
	private Date used;
	private int index;
	private Date redeem;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ORDER_ID", nullable=true)
	private Order order;
	
	
	@Override
	public String toJSONString() {
		return null;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Voucher getVoucher() {
		return voucher;
	}


	public void setVoucher(Voucher voucher) {
		this.voucher = voucher;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
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


	public int getBlocked() {
		return blocked;
	}


	public void setBlocked(int blocked) {
		this.blocked = blocked;
	}


	public String getBlockMessage() {
		return blockMessage;
	}


	public void setBlockMessage(String blockMessage) {
		this.blockMessage = blockMessage;
	}


	public Customer getCustomer() {
		return customer;
	}


	public void setCustomer(Customer customer) {
		this.customer = customer;
	}


	public Date getUsed() {
		return used;
	}


	public void setUsed(Date used) {
		this.used = used;
	}


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public Date getRedeem() {
		return redeem;
	}


	public void setRedeem(Date redeem) {
		this.redeem = redeem;
	}


	public Order getOrder() {
		return order;
	}


	public void setOrder(Order order) {
		this.order = order;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	@Override
	public AuditSection getAuditSection() {
		return null;
	}


	@Override
	public void setAuditSection(AuditSection auditSection) {
	}



}
