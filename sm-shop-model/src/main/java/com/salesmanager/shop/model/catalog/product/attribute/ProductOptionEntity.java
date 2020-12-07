package com.salesmanager.shop.model.catalog.product.attribute;

public class ProductOptionEntity extends ProductOption {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int order;
	
	private String type;
	public void setOrder(int order) {
		this.order = order;
	}
	public int getOrder() {
		return order;
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String getType() {
		return type;
	}

}
