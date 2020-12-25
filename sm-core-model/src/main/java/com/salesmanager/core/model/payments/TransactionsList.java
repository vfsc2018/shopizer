package com.salesmanager.core.model.payments;

import java.util.ArrayList;
import java.util.List;

import com.salesmanager.core.model.common.EntityList;


public class TransactionsList extends EntityList {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4643474343560656948L;
	private List<Transaction> transactions = new ArrayList<>();
	
	public List<Transaction> getTransactions() {
		return transactions;
	}
	
	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

}
