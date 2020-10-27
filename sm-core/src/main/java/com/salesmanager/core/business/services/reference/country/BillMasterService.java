package com.salesmanager.core.business.services.reference.country;

import java.util.List;

import org.springframework.validation.BindException;

import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.catalog.product.BillMaster;

public interface BillMasterService extends SalesManagerEntityService<Integer, BillMaster> {
	
	public BillMaster saveAnnouncement(BillMaster form) throws BindException;
	public Long countByOrderId(Long orderId);
	public List<BillMaster> findByOrderId(Long pid);
	
}
