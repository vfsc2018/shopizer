package com.salesmanager.core.model.payments;

import com.salesmanager.core.model.common.Criteria;

public class TransactionsCriteria extends Criteria {
	
	private int transactionId = 0;

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}


}
