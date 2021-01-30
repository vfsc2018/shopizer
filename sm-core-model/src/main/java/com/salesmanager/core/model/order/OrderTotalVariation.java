package com.salesmanager.core.model.order;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains a list of negative OrderTotal variation
 * that will be shown in the order summary
 * @author carlsamson
 *
 */
public abstract class OrderTotalVariation {
	

	List<OrderTotal> variations = new ArrayList<>();

	protected OrderTotalVariation(){}

	public List<OrderTotal> getVariations() {
		return variations;
	}

	public void setVariations(List<OrderTotal> variations) {
		this.variations = variations;
	}

}
