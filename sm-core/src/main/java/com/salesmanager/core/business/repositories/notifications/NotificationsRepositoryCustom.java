package com.salesmanager.core.business.repositories.notifications;

import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.NotificationsCriteria;
import com.salesmanager.core.model.order.NotificationsList;

import org.springframework.data.domain.Pageable;
import java.util.List;
import com.salesmanager.core.model.message.Notifications;

public interface NotificationsRepositoryCustom {
	//CustomerList listByStore(MerchantStore store, CustomerCriteria criteria);
	NotificationsList listByStore2(MerchantStore store, NotificationsCriteria criteria);
	
	// Integer countByOrderId(Long orderId);
	// Integer countByCustomerId(Long customerId);
	Integer countByCustomerId(Long customerId, String read) ;
	// List<Notifications> findByOrderId(Long orderId, Pageable page);
	List<Notifications> findByCustomerId(Long id, String read, Pageable page);

}
