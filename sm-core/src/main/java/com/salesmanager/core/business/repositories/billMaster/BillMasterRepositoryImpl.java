package com.salesmanager.core.business.repositories.billMaster;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;

import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.BillMasterCriteria;
import com.salesmanager.core.model.order.BillMasterList;



public class BillMasterRepositoryImpl implements BillMasterRepositoryCustom {

    @PersistenceContext
    private EntityManager em;
	@SuppressWarnings("unchecked")
	@Override
	public BillMasterList listByStore2(MerchantStore store, BillMasterCriteria criteria) {
		BillMasterList customerList = new BillMasterList();
		StringBuilder baseCountQuery =new StringBuilder("select count(c) from BillMaster as c where c.id = c.id ");
		StringBuilder baseQuery = new StringBuilder("select c from BillMaster as c where c.id = c.id ");
		
		
		
		if(!StringUtils.isBlank(criteria.getSku())) {

			baseCountQuery.append(" and c.sku like:sk ");
			baseQuery.append(" and c.sku like:sk ");
			
		}
		
		if(!StringUtils.isBlank(criteria.getProductName())) {
			
			baseCountQuery.append(" and c.productName like:nm");
			baseQuery.append(" and c.productName like:nm");
			
		}

		if(criteria.getId()>0) {
			
			baseCountQuery.append(" and c.id = :pId");
			baseQuery.append(" and c.id = :pId");
			
		}

		if(criteria.getOrderId()>0) {
			
			baseCountQuery.append(" and c.order.id = :pOrderId");
			baseQuery.append(" and c.order.id = :pOrderId");
			
		}
		if(!StringUtils.isBlank(criteria.getStatus())) {
			
			baseCountQuery.append(" and c.status = :pStatus");
			baseQuery.append(" and c.status = :pStatus");
			
		}
		
		
		Query countQ = em.createQuery(baseCountQuery.toString());
		//object query
		Query objectQ = em.createQuery(baseQuery.toString());

		if(!StringUtils.isBlank(criteria.getSku())) {
			String nameParam = new StringBuilder().append("%").append(criteria.getSku()).append("%").toString();
			countQ.setParameter("sk",nameParam);
			objectQ.setParameter("sk",nameParam);
		}
		
		if(!StringUtils.isBlank(criteria.getProductName())) {
			String nameParam = new StringBuilder().append("%").append(criteria.getProductName()).append("%").toString();
			countQ.setParameter("nm",nameParam);
			objectQ.setParameter("nm",nameParam);
		}		
		if(criteria.getId()>0) {

			countQ.setParameter("pId",criteria.getId());
			objectQ.setParameter("pId",criteria.getId());
			
		}

		if(criteria.getOrderId()>0) {
			
			countQ.setParameter("pOrderId",criteria.getOrderId());
			objectQ.setParameter("pOrderId",criteria.getOrderId());
			
		}
		if(!StringUtils.isBlank(criteria.getStatus())) {
			
			countQ.setParameter("pStatus",criteria.getStatus());
			objectQ.setParameter("pStatus",criteria.getStatus());
			
			
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
		
		customerList.setBillMasters(objectQ.getResultList());

		return customerList;
		
		
	}    


}
