package com.salesmanager.core.business.repositories.payments;

import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.payments.TransactionsCriteria;
import com.salesmanager.core.model.payments.TransactionsList;

public interface TransactionRepositoryCustom {

	TransactionsList listByStore2(MerchantStore store, TransactionsCriteria criteria);


}
