package com.salesmanager.core.business.services.order.bill;

import java.util.List;

import org.springframework.validation.BindException;

import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.order.BillItem;


public interface BillItemService extends SalesManagerEntityService<Long, BillItem> {
	List<BillItem> getItemByBillId(Long billId);
	BillItem saveBillItem(BillItem form) throws BindException;
}
