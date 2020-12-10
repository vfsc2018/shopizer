package com.salesmanager.core.business.repositories.notifications;

import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.NotificationsCriteria;
import com.salesmanager.core.model.order.NotificationsList;





public interface NotificationsRepositoryCustom {
	//CustomerList listByStore(MerchantStore store, CustomerCriteria criteria);
	NotificationsList listByStore2(MerchantStore store, NotificationsCriteria criteria);

}
