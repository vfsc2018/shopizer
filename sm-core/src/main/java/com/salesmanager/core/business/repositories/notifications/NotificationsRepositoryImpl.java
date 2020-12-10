package com.salesmanager.core.business.repositories.notifications;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;

import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.NotificationsCriteria;
import com.salesmanager.core.model.order.NotificationsList;



public class NotificationsRepositoryImpl implements NotificationsRepositoryCustom {

    @PersistenceContext
    private EntityManager em;
    
    
    
	@SuppressWarnings("unchecked")
	@Override
	public NotificationsList listByStore2(MerchantStore store, NotificationsCriteria criteria) {
		NotificationsList customerList = new NotificationsList();
		StringBuilder baseCountQuery =new StringBuilder("select count(c) from Notifications as c where c.id = c.id ");
		StringBuilder baseQuery = new StringBuilder("select c from Notifications as c where c.id = c.id ");
		
		if(!StringUtils.isBlank(criteria.getMessager())) {
			String nameQuery =" and c.messager like:pMessager  ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		if(!StringUtils.isBlank(criteria.getCustomerName())){
			String nameQuery =" and (c.customer.billing.firstName like:pCustomer or c.customer.billing.lastName like:pCustomer) ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		
		if(!StringUtils.isBlank(criteria.getTopic())) {
			String nameQuery =" and c.topic like:pTopic ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		
		if(!StringUtils.isBlank(criteria.getRead())) {
			String nameQuery = " and c.read =:pRead ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
			
		}
		
		
		Query countQ = em.createQuery(baseCountQuery.toString());
		//object query
		Query objectQ = em.createQuery(baseQuery.toString());

		if(!StringUtils.isBlank(criteria.getMessager())) {
			String nameParam = new StringBuilder().append("%").append(criteria.getMessager()).append("%").toString();
			countQ.setParameter("pMessager",nameParam);
			objectQ.setParameter("pMessager",nameParam);
		}
		
		
		if(!StringUtils.isBlank(criteria.getCustomerName())) {
			String nameParam = new StringBuilder().append("%").append(criteria.getCustomerName()).append("%").toString();
			countQ.setParameter("nm",nameParam);
			objectQ.setParameter("nm",nameParam);
		}

		if(!StringUtils.isBlank(criteria.getCustomerName())) {
			String nameParam = new StringBuilder().append("%").append(criteria.getCustomerName()).append("%").toString();
			countQ.setParameter("pCustomer",nameParam);
			objectQ.setParameter("pCustomer",nameParam);
		}

		if(!StringUtils.isBlank(criteria.getTopic())) {
			String nameParam = new StringBuilder().append("%").append(criteria.getTopic()).append("%").toString();
			countQ.setParameter("pTopic",nameParam);
			objectQ.setParameter("pTopic",nameParam);
		}
		
		if(criteria.getRead()!=null && !criteria.getRead().equals("")) {
			countQ.setParameter("pRead",criteria.getRead());
			objectQ.setParameter("pRead",criteria.getRead());
		}

		
		
		
		Number count = (Number) countQ.getSingleResult();

		customerList.setTotalCount(count.intValue());
		
        if(count.intValue()==0)
        	return customerList;
        
		//TO BE USED
        int max = criteria.getMaxCount();
        int first = criteria.getStartIndex();
        
        objectQ.setFirstResult(first);
        
    	if(max>0) {
    			int maxCount = first + max;

    			if(maxCount < count.intValue()) {
    				objectQ.setMaxResults(maxCount);
    			} else {
    				objectQ.setMaxResults(count.intValue());
    			}
    	}
		
		customerList.setNotificationss(objectQ.getResultList());

		return customerList;
		
		
	}    


}
