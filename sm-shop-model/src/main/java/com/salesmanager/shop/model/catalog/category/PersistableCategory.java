package com.salesmanager.shop.model.catalog.category;

import java.util.ArrayList;
import java.util.List;

public class PersistableCategory extends CategoryEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<CategoryDescription> descriptions;//always persist description
	private List<PersistableCategory> children = new ArrayList<>();
	
	public List<CategoryDescription> getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(List<CategoryDescription> descriptions) {
		this.descriptions = descriptions;
	}
	public List<PersistableCategory> getChildren() {
		return children;
	}
	public void setChildren(List<PersistableCategory> children) {
		this.children = children;
	}

}
