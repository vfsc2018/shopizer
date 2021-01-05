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

import org.json.simple.JSONAware;

import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.model.common.audit.AuditListener;
import com.salesmanager.core.model.common.audit.AuditSection;
import com.salesmanager.core.model.common.audit.Auditable;
import com.salesmanager.core.model.generic.SalesManagerEntity;


@Entity
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
	private int point;
	private int discount;
	private int status;
	private int blocked;
	private String blockMessage;
	private Date startDate;
	private Date endDate;
	private String weekDays;
	private String dayOfMonth;
	private int startTime;
	private int endTime;
	private Date approved;
	private long customerId;
	private Date expire;
	private long creatorId;
	

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

	public int getPoint() {
		return point;
	}


	public void setPoint(int point) {
		this.point = point;
	}


	public int getDiscount() {
		return discount;
	}


	public void setDiscount(int discount) {
		this.discount = discount;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
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


	public int getStartTime() {
		return startTime;
	}


	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}


	public int getEndTime() {
		return endTime;
	}


	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}


	public Date getApproved() {
		return approved;
	}


	public void setApproved(Date approved) {
		this.approved = approved;
	}


	public long getCustomerId() {
		return customerId;
	}


	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}


	public Date getExpire() {
		return expire;
	}


	public void setExpire(Date expire) {
		this.expire = expire;
	}


	public long getCreatorId() {
		return creatorId;
	}


	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
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
