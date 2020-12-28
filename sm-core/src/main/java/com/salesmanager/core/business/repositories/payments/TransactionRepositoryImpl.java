package com.salesmanager.core.business.repositories.payments;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;

import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.payments.TransactionsCriteria;
import com.salesmanager.core.model.payments.TransactionsList;


public class TransactionRepositoryImpl implements TransactionRepositoryCustom {

    @PersistenceContext
	private EntityManager em;

	
    
	@SuppressWarnings("unchecked")
	@Override
	public TransactionsList listByStore2(MerchantStore store, TransactionsCriteria criteria) {
		TransactionsList customerList = new TransactionsList();
		StringBuilder baseCountQuery =new StringBuilder("select count(c) from Transaction as c where c.id = c.id ");
		StringBuilder baseQuery = new StringBuilder("select c from Transaction as c where c.id = c.id ");
		if(criteria.getTransactionId()!=null) {
			String nameQuery =" and c.id <= :pid  ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		
		if(!StringUtils.isBlank(criteria.getDate())) {
			String nameQuery =" and TO_CHAR(c.transactionDate,'DD/MM/YYYY') = :dc ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}

		if(!StringUtils.isBlank(criteria.getDetail())) {
			String nameQuery =" and c.details like:pDetail  ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		
		
		
		baseQuery.append(" order by c.id desc ");
	

		Query countQ = em.createQuery(baseCountQuery.toString());
		//object query
		Query objectQ = em.createQuery(baseQuery.toString());

		if(criteria.getTransactionId()!=null) {
			countQ.setParameter("pid",criteria.getTransactionId());
			objectQ.setParameter("pid",criteria.getTransactionId());
		}
		
		if(!StringUtils.isBlank(criteria.getDate())) {
			countQ.setParameter("dc",criteria.getDate());
			objectQ.setParameter("dc",criteria.getDate());
		}

		if(!StringUtils.isBlank(criteria.getDetail())) {
			String nameParam = new StringBuilder().append("%").append(criteria.getDetail()).append("%").toString();
			countQ.setParameter("pDetail",nameParam);
			objectQ.setParameter("pDetail",nameParam);
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
		
		customerList.setTransactions(objectQ.getResultList());

		return customerList;
		
		
	}    


}
