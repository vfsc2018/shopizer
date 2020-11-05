package com.salesmanager.core.business.services.reference.country;

import org.springframework.validation.BindException;

import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.catalog.product.relationship.BillItem;


public interface BillItemService extends SalesManagerEntityService<Integer, BillItem> {
	
	BillItem saveBillItem(BillItem form) throws BindException;
}