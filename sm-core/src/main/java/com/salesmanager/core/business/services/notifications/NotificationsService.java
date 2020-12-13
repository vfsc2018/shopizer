package com.salesmanager.core.business.services.notifications;

import org.springframework.validation.BindException;

import com.salesmanager.core.business.services.common.generic.SalesManagerEntityService;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.message.Notifications;
import com.salesmanager.core.model.order.NotificationsCriteria;
import com.salesmanager.core.model.order.NotificationsList;

public interface NotificationsService extends SalesManagerEntityService<Long, Notifications> {
	
	public Notifications saveAnnouncement(Notifications form) throws BindException;


	NotificationsList getListByStore2(MerchantStore store, NotificationsCriteria criteria);
}
