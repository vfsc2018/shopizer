package com.salesmanager.core.business.repositories.notifications;

import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.NotificationsCriteria;
import com.salesmanager.core.model.order.NotificationsList;

public interface NotificationsRepositoryCustom {
	//CustomerList listByStore(MerchantStore store, CustomerCriteria criteria);
	NotificationsList listByStore2(MerchantStore store, NotificationsCriteria criteria);
	// Integer countByOrderId(Long orderId);
	// Integer countByCustomerId(Long customerId);
	// Integer countByCustomerId(Long customerId, Integer read);
	// List<Notifications> findByOrderId(Long orderId, Pageable page);
	// List<Notifications> findByCustomerId(Long customerId, Pageable page);

}
