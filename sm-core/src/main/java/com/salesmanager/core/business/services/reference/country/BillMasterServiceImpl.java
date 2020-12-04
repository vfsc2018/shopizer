package com.salesmanager.core.business.services.reference.country;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;

import com.salesmanager.core.business.repositories.billMaster.BillMasterRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.catalog.product.BillMaster;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.BillMasterCriteria;
import com.salesmanager.core.model.order.BillMasterList;
import com.salesmanager.core.model.order.CollectBill;

@Service("billMasterService")
public class BillMasterServiceImpl extends SalesManagerEntityServiceImpl<Long, BillMaster> implements BillMasterService {

	private BillMasterRepository billMasterRepository;

	@Override
	public List<CollectBill> collectBill(String billIds){
		return billMasterRepository.collectBill(billIds);
	}
	
	@Override
	public List<CollectBill> collectOrder(String orderIds){
		return billMasterRepository.collectOrder(orderIds);
	}
	
	@Override
	public BillMasterList getListByStore2(MerchantStore store, BillMasterCriteria criteria) {
		return billMasterRepository.listByStore2(store, criteria);
	}
    
    
	@Inject
	public BillMasterServiceImpl(BillMasterRepository billMasterRepository) {
		super(billMasterRepository);
		this.billMasterRepository = billMasterRepository;
	}
	
	
	public BillMaster saveAnnouncement(BillMaster form) throws BindException {
		BillMaster entity =new BillMaster();
			
		entity = billMasterRepository.saveAndFlush(form);
		return entity;
	}
	
	public Long countByOrderId(Long orderId){
		return billMasterRepository.countByOrderId(orderId);
	}
	
	public List<BillMaster> findByOrderId(Long pid){
		return billMasterRepository.findByOrderId(pid);
	}
	
}
