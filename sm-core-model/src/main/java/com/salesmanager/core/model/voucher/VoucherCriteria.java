package com.salesmanager.core.model.voucher;

import java.util.Date;

import com.salesmanager.core.model.common.Criteria;

public class VoucherCriteria extends Criteria {
	
	private Long id;
	private boolean blocked;
	private boolean approved;
	private Date startDate;
	private Date endDate;
	private String manager;
	private String product;
	
	/***************************/
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public boolean getBlocked() {
		return blocked;
	}
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
	public boolean getApproved() {
		return approved;
	}
	public void setApproved(boolean approved) {
		this.approved = approved;
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
	
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getManager() {
		return manager ;
	}
	public void setManager(String manager ) {
		this.manager  = manager ;
	}

}
