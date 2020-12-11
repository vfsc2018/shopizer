package com.salesmanager.core.model.order;

import java.util.ArrayList;
import java.util.List;

import com.salesmanager.core.model.message.Notifications;
import com.salesmanager.core.model.common.EntityList;


public class NotificationsList extends EntityList {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5809164518639190983L;
	/**
	 * 
	 */

	private List<Notifications> notifications = new ArrayList<>();
	
	
	public void setNotificationss(List<Notifications> notifications) {
		this.notifications = notifications;
	}
	public List<Notifications> getNotificationss() {
		return notifications;
	}


}
