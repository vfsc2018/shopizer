package com.salesmanager.core.business.services.notifications;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;

import com.salesmanager.core.business.repositories.notifications.NotificationsRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.message.Notifications;
import com.salesmanager.core.model.order.NotificationsCriteria;
import com.salesmanager.core.model.order.NotificationsList;

@Service("notificationsService")
public class NotificationsServiceImpl extends SalesManagerEntityServiceImpl<Long, Notifications> implements NotificationsService {

	@Inject
	private NotificationsRepository notificationsRepository;

	@Override
	public NotificationsList getListByStore2(MerchantStore store, NotificationsCriteria criteria) {
		return notificationsRepository.listByStore2(store, criteria);
	}
    
    
	@Inject
	public NotificationsServiceImpl(NotificationsRepository notificationsRepository) {
		super(notificationsRepository);
		this.notificationsRepository = notificationsRepository;
	}
	
	
	public Notifications saveAnnouncement(Notifications form) throws BindException {
		return notificationsRepository.saveAndFlush(form);
	}


	
}
