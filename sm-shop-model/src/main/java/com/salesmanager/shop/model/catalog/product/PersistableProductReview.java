package com.salesmanager.shop.model.catalog.product;


public class PersistableProductReview extends ProductReviewEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long customerId;
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	


}
