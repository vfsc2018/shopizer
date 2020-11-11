package com.salesmanager.core.model.order;

import com.salesmanager.core.model.common.Criteria;

public class BillMasterCriteria extends Criteria {
	
	private String productName = null;
	private String sku = null;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	
	

}
