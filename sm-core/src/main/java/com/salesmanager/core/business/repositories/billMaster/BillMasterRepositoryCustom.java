package com.salesmanager.core.business.repositories.billMaster;

import java.util.List;

import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.BillMasterCriteria;
import com.salesmanager.core.model.order.BillMasterList;
import com.salesmanager.core.model.order.CollectBill;





public interface BillMasterRepositoryCustom {
	//CustomerList listByStore(MerchantStore store, CustomerCriteria criteria);
	BillMasterList listByStore2(MerchantStore store, BillMasterCriteria criteria);
	List<CollectBill> collectBill(String billIds);
}
