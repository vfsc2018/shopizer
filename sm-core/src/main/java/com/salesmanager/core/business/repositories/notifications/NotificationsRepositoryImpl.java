package com.salesmanager.core.business.repositories.notifications;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.NotificationsCriteria;
import com.salesmanager.core.model.order.NotificationsList;
import com.salesmanager.core.model.message.Notifications;

import java.util.Collections;
import java.util.List;
public class NotificationsRepositoryImpl implements NotificationsRepositoryCustom  {

    @PersistenceContext
	private EntityManager em;

	@Inject 
	NotificationsRepository notificationsRepository;
	

	public Notifications save(Notifications entity) {
		return notificationsRepository.save(entity);
	}

	public Notifications findById(Long id) {
		return notificationsRepository.getOne(id);
	}

	public Integer countByOrderId(Long orderId) {
		return notificationsRepository.countByOrderId(orderId);
	}

	@Override
	public Integer countByCustomerId(Long customerId, String read) {
		if(read!=null){
			if(read.equals("all")){
				return notificationsRepository.countByCustomerId(customerId);
			}else if(read.equals("1")){
				return notificationsRepository.countByCustomerId(customerId, 1);
			}else if(read.equals("0")){
				return notificationsRepository.countByCustomerId(customerId, 0);
			}
		}
		return 0;
	}

	public List<Notifications> findByOrderId(Long id, Pageable page){
		return notificationsRepository.findByOrderId(id, page);
	}

	@Override
	public List<Notifications> findByCustomerId(Long id, String read, Pageable page){
		if(read!=null){
			if(read.equals("all")){
				return notificationsRepository.findByCustomerId(id,page);
			}else if(read.equals("1")){
				return notificationsRepository.findByCustomerId(id,1,page);
			}else if(read.equals("0")){
				return notificationsRepository.findByCustomerId(id,0,page);
			}
		}
		return Collections.emptyList();

	}
    
	@SuppressWarnings("unchecked")
	@Override
	public NotificationsList listByStore2(MerchantStore store, NotificationsCriteria criteria) {
		NotificationsList customerList = new NotificationsList();
		StringBuilder baseCountQuery =new StringBuilder("select count(c) from Notifications as c where c.id = c.id ");
		StringBuilder baseQuery = new StringBuilder("select c from Notifications as c where c.id = c.id ");
		if(criteria.getId()!=null) {
			String nameQuery =" and c.id <= :id  ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}

		if(!StringUtils.isBlank(criteria.getDate())) {
			String nameQuery =" and TO_CHAR(c.auditSection.dateCreated,'DD/MM/YYYY') = :dc ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}

		if(!StringUtils.isBlank(criteria.getMessage())) {
			String nameQuery =" and lower(c.message) LIKE lower(:pMessager)  ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		if(!StringUtils.isBlank(criteria.getCustomerName())){
			String nameQuery =" and (lower(c.customer.billing.firstName) LIKE lower(:pCustomer) or c.customer.billing.lastName LIKE :pCustomer) ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		
		if(!StringUtils.isBlank(criteria.getTopic())) {
			String nameQuery =" and c.topic LIKE :pTopic ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		
		if(criteria.getRead()!=null && criteria.getRead().booleanValue()) {
			String nameQuery = " and c.read > 0 ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
			
		}
		
		if(!StringUtils.isBlank(criteria.getCriteriaOrderByField())) {
			baseQuery.append(" order by c." + criteria.getCriteriaOrderByField() + " " + criteria.getOrderBy().name().toLowerCase());
		}else{
			// baseQuery.append(" order by c.id desc ");
		}
	
		Query countQ = em.createQuery(baseCountQuery.toString());
		//object query
		Query objectQ = em.createQuery(baseQuery.toString());

		if(!StringUtils.isBlank(criteria.getDate())) {
			countQ.setParameter("dc",criteria.getDate());
			objectQ.setParameter("dc",criteria.getDate());
		}

		if(!StringUtils.isBlank(criteria.getMessage())) {
			String nameParam = new StringBuilder().append("%").append(criteria.getMessage()).append("%").toString();
			countQ.setParameter("pMessager",nameParam);
			objectQ.setParameter("pMessager",nameParam);
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

		if(criteria.getId()!=null) {
			countQ.setParameter("id",criteria.getId());
			objectQ.setParameter("id",criteria.getId());
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
