package com.salesmanager.core.business.services.order.bill;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;

import com.salesmanager.core.business.repositories.billMaster.BillMasterRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.order.BillMaster;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.BillMasterCriteria;
import com.salesmanager.core.model.order.BillMasterList;
import com.salesmanager.core.model.order.CollectBill;
import com.salesmanager.core.model.order.orderstatus.OrderStatus;

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
		return billMasterRepository.saveAndFlush(form);
	}
	
	public Long countByOrderId(Long orderId){
		return billMasterRepository.countByOrderId(orderId);
	}
	
	public List<BillMaster> findByOrderId(Long pid){
		return billMasterRepository.findByOrderId(pid);
	}

	public List<BillMaster> findLast(Long customerId, List<OrderStatus> status, Pageable pageable){
		return billMasterRepository.findLast(customerId, status, pageable);
	}
	
}
