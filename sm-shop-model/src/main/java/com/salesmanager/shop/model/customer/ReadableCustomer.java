package com.salesmanager.shop.model.customer;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.salesmanager.shop.model.customer.attribute.ReadableCustomerAttribute;
import com.salesmanager.shop.model.security.ReadableGroup;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReadableCustomer extends CustomerEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ReadableCustomerAttribute> attributes = new ArrayList<>();
	private List<ReadableGroup> groups = new ArrayList<>();
	private Integer point;

	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}
	
	public void setAttributes(List<ReadableCustomerAttribute> attributes) {
		this.attributes = attributes;
	}
	public List<ReadableCustomerAttribute> getAttributes() {
		return attributes;
	}
	public List<ReadableGroup> getGroups() {
		return groups;
	}
	public void setGroups(List<ReadableGroup> groups) {
		this.groups = groups;
	}

}
