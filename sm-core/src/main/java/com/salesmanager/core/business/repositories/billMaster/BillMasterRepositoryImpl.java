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
		
		if(!StringUtils.isBlank(criteria.getDate())) {
			String nameQuery =" and TO_CHAR(c.dateExported,'DD/MM/YYYY') = :dc ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}

		if(!StringUtils.isBlank(criteria.getPhone())) {
			String nameQuery =" and c.phone like:ph ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		
		if(!StringUtils.isBlank(criteria.getSku())) {

			baseCountQuery.append(" and c.sku like:sk ");
			baseQuery.append(" and c.sku like:sk ");
			
		}
		
		if(!StringUtils.isBlank(criteria.getProductName())) {
			
			baseCountQuery.append(" and c.productName like:nm");
			baseQuery.append(" and c.productName like:nm");
			
		}

		if(criteria.getId()!=null) {
			String nameQuery = " and c.id <= :pId ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}

		if(criteria.getOrderId()!=null) {
			baseCountQuery.append(" and c.order.id = :pOrderId");
			baseQuery.append(" and c.order.id = :pOrderId");
			
		}
		if(!StringUtils.isBlank(criteria.getStatus())) {
			baseCountQuery.append(" and c.status = :pStatus");
			baseQuery.append(" and c.status = :pStatus");
			
		}

		if(!StringUtils.isBlank(criteria.getCriteriaOrderByField())) {
			baseQuery.append(" order by c." + criteria.getCriteriaOrderByField() + " " + criteria.getOrderBy().name().toLowerCase());
		}else{
			baseQuery.append(" order by c.id desc ");
		}
		
		Query countQ = em.createQuery(baseCountQuery.toString());
		//object query
		Query objectQ = em.createQuery(baseQuery.toString());

		if(criteria.getDate()!=null) {
			countQ.setParameter("dc",criteria.getDate());
			objectQ.setParameter("dc",criteria.getDate());
		}

		if(!StringUtils.isBlank(criteria.getPhone())) {
			String nameParam = new StringBuilder().append("%").append(criteria.getPhone()).append("%").toString();
			countQ.setParameter("ph",nameParam);
			objectQ.setParameter("ph",nameParam);
		}

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
		if(criteria.getId()!=null) {
			countQ.setParameter("pId",criteria.getId());
			objectQ.setParameter("pId",criteria.getId());
		}

		if(criteria.getOrderId()!=null) {
			
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
