package com.salesmanager.core.business.services.reference.country;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;

import com.salesmanager.core.business.repositories.reference.country.BillMasterRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.catalog.product.BillMaster;

@Service("billMasterService")
public class BillMasterServiceImpl extends SalesManagerEntityServiceImpl<Integer, BillMaster>
		implements BillMasterService {

	private BillMasterRepository billMasterRepository;


	
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