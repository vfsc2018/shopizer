package com.salesmanager.core.business.services.notifications;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;

import com.salesmanager.core.business.repositories.notifications.NotificationsRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.message.Notifications;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.NotificationsCriteria;
import com.salesmanager.core.model.order.NotificationsList;

@Service("notificationsService")
public class NotificationsServiceImpl extends SalesManagerEntityServiceImpl<Long, Notifications> implements NotificationsService {

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
	
	public Long countByOrderId(Long orderId){
		return notificationsRepository.countByOrderId(orderId);
	}
	
	public List<Notifications> findByOrderId(Long pid){
		return notificationsRepository.findByOrderId(pid);
	}
	
}
