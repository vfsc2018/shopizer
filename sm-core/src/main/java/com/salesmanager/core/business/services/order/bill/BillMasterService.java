package com.salesmanager.core.business.services.order.bill;

import java.util.List;

import org.springframework.validation.BindException;

import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.order.BillMaster;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.BillMasterCriteria;
import com.salesmanager.core.model.order.BillMasterList;
import com.salesmanager.core.model.order.CollectBill;
import com.salesmanager.core.model.order.orderstatus.OrderStatus;

public interface BillMasterService extends SalesManagerEntityService<Long, BillMaster> {
	
	public BillMaster saveAnnouncement(BillMaster form) throws BindException;
	public Long countByOrderId(Long orderId);
	public List<BillMaster> findByOrderId(Long pid);

	public List<BillMaster> findLast(Long customerId, List<OrderStatus> status);

	BillMasterList getListByStore2(MerchantStore store, BillMasterCriteria criteria);
	List<CollectBill> collectBill(String billIds);
	List<CollectBill> collectOrder(String orderIds);
}
