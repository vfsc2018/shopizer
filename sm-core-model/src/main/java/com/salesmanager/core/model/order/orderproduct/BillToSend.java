package com.salesmanager.core.model.order.orderproduct;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class BillToSend {
	private String code;
	private Date date;
	private String description;
	private List<BillDetailToSend> detail = new ArrayList<BillDetailToSend>();
//	private String local;
//	private String orgId;
//	private String orgPersonId;
//	private String orgPersonName;
//	private String partnerId;
//	private String partnerPersonId;
//	private String partnerPersonName;
//	private String planId;
//	private String reason;
	
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<BillDetailToSend> getDetail() {
		return detail;
	}
	public void setDetail(List<BillDetailToSend> detail) {
		this.detail = detail;
	}
//	
//	public String getLocal() {
//		return local;
//	}
//	public void setLocal(String local) {
//		this.local = local;
//	}
//	public String getOrgId() {
//		return orgId;
//	}
//	public void setOrgId(String orgId) {
//		this.orgId = orgId;
//	}
//	public String getOrgPersonId() {
//		return orgPersonId;
//	}
//	public void setOrgPersonId(String orgPersonId) {
//		this.orgPersonId = orgPersonId;
//	}
//	public String getOrgPersonName() {
//		return orgPersonName;
//	}
//	public void setOrgPersonName(String orgPersonName) {
//		this.orgPersonName = orgPersonName;
//	}
//	public String getPartnerId() {
//		return partnerId;
//	}
//	public void setPartnerId(String partnerId) {
//		this.partnerId = partnerId;
//	}
//	public String getPartnerPersonId() {
//		return partnerPersonId;
//	}
//	public void setPartnerPersonId(String partnerPersonId) {
//		this.partnerPersonId = partnerPersonId;
//	}
//	public String getPartnerPersonName() {
//		return partnerPersonName;
//	}
//	public void setPartnerPersonName(String partnerPersonName) {
//		this.partnerPersonName = partnerPersonName;
//	}
//	public String getPlanId() {
//		return planId;
//	}
//	public void setPlanId(String planId) {
//		this.planId = planId;
//	}
//	public String getReason() {
//		return reason;
//	}
//	public void setReason(String reason) {
//		this.reason = reason;
//	}
	
}
