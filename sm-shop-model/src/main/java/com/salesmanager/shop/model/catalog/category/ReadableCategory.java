package com.salesmanager.shop.model.catalog.category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.Transient;

public class ReadableCategory extends CategoryEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CategoryDescription description;//one category based on language
	private int productCount;
	private String store;
	private List<ReadableCategory> children = new ArrayList<>() {
		private static final long serialVersionUID = 2L;
		
		@Override
		public boolean add(ReadableCategory x) {
			int index = Collections.binarySearch(this, x, new Comparator<>() {
				public int compare(ReadableCategory u1, ReadableCategory u2)
				{
					return u1.getSortOrder()>u2.getSortOrder()?1:-1;
				}
			});
			if (index < 0) index = ~index;
			super.add(index, x);
			return true;
		}
	};
	
	
	public void setDescription(CategoryDescription description) {
		this.description = description;
	}
	public CategoryDescription getDescription() {
		return description;
	}

	public int getProductCount() {
		return productCount;
	}
	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}
	public List<ReadableCategory> getChildren() {
		return children;
	}
	public void setChildren(List<ReadableCategory> children) {
		this.children = children;
	}
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}

}
