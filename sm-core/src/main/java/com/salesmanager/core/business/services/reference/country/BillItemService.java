package com.salesmanager.core.business.services.reference.country;

import java.util.List;

import org.springframework.validation.BindException;

import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.catalog.product.relationship.BillItem;


public interface BillItemService extends SalesManagerEntityService<Long, BillItem> {
	List<BillItem> getItemByBillId(Long billId);
	BillItem saveBillItem(BillItem form) throws BindException;
}
