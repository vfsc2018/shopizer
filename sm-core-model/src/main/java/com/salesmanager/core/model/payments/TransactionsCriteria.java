package com.salesmanager.core.model.payments;

import com.salesmanager.core.model.common.Criteria;

public class TransactionsCriteria extends Criteria {
	
	private Long transactionId;
	private String date;
	private String detail;


	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}


}
