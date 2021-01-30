package com.salesmanager.core.model.voucher;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.json.simple.JSONAware;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.model.common.audit.AuditListener;
import com.salesmanager.core.model.common.audit.AuditSection;
import com.salesmanager.core.model.common.audit.Auditable;
import com.salesmanager.core.model.generic.SalesManagerEntity;


@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@EntityListeners(value = AuditListener.class)
@Table(name = "VOUCHER", schema= SchemaConstant.SALESMANAGER_SCHEMA)
public class Voucher extends SalesManagerEntity<Long, Voucher> implements Auditable, JSONAware {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "VOUCHER_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	private String code;
	private String description;
	private Integer point = 0;
	private Integer discount = 0;
	@Min(0)
	@Max(100)
	private Integer percent = 0;
	private Integer status = 0;
	private Integer blocked = 0;
	private String blockMessage;
	private Date startDate;
	private Date endDate;
	private String weekDays;
	private String dayOfMonth;
	private Integer startTime;
	private Integer endTime;
	private Date approved;
	private Long partnerId;
	private Date expire;
	private String manager;
	private String productSku;
	
	public String getProductSku() {
		return productSku;
	}

	public void setProductSku(String productSku) {
		this.productSku = productSku;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPoint() {
		return point;
	}


	public void setPoint(Integer point) {
		this.point = point;
	}


	public Integer getDiscount() {
		return discount;
	}


	public void setDiscount(Integer discount) {
		this.discount = discount;
	}


	public Integer getPercent() {
		return percent;
	}


	public void setPercent(Integer percent) {
		this.percent = percent;
	}

	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	public Integer getBlocked() {
		return blocked;
	}


	public void setBlocked(Integer blocked) {
		this.blocked = blocked;
	}


	public String getBlockMessage() {
		return blockMessage;
	}


	public void setBlockMessage(String blockMessage) {
		this.blockMessage = blockMessage;
	}


	public Date getStartDate() {
		return startDate;
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public String getWeekDays() {
		return weekDays;
	}


	public void setWeekDays(String weekDays) {
		this.weekDays = weekDays;
	}


	public String getDayOfMonth() {
		return dayOfMonth;
	}


	public void setDayOfMonth(String dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}


	public Integer getStartTime() {
		return startTime;
	}


	public void setStartTime(Integer startTime) {
		this.startTime = startTime;
	}


	public Integer getEndTime() {
		return endTime;
	}


	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}


	public Date getApproved() {
		return approved;
	}


	public void setApproved(Date approved) {
		this.approved = approved;
	}


	public Long getPartnerId() {
		return partnerId ;
	}


	public void setPartnerId(Long partnerId ) {
		this.partnerId = partnerId ;
	}


	public Date getExpire() {
		return expire;
	}


	public void setExpire(Date expire) {
		this.expire = expire;
	}


	public String getManager() {
		return manager ;
	}


	public void setManager(String manager ) {
		this.manager  = manager ;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	@Override
	public String toJSONString() {
		
	
		
		return null;
	}


	@Override
	public AuditSection getAuditSection() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setAuditSection(AuditSection auditSection) {
		// TODO Auto-generated method stub
		
	}



}
