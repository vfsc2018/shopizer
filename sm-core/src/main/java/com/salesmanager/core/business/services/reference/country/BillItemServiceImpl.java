package com.salesmanager.core.business.services.reference.country;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;

import com.salesmanager.core.business.repositories.reference.country.BillItemRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.catalog.product.relationship.BillItem;

@Service("billItemService")
public class BillItemServiceImpl extends SalesManagerEntityServiceImpl<Integer, BillItem>
		implements BillItemService {
	

	private BillItemRepository billItemRepository;


	
	@Inject
	public BillItemServiceImpl(BillItemRepository billItemRepository) {
		super(billItemRepository);
		this.billItemRepository = billItemRepository;
	}
	
	
	public BillItem saveBillItem(BillItem form) throws BindException {
		BillItem entity =new BillItem();
			
		entity = billItemRepository.saveAndFlush(form);
		return entity;
	}
	
}
