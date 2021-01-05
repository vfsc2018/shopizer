package com.salesmanager.core.business.repositories.voucher;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;

import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.voucher.VoucherCriteria;
import com.salesmanager.core.model.voucher.VoucherList;


public class VoucherRepositoryImpl implements VoucherRepositoryCustom {

    @PersistenceContext
	private EntityManager em;

	
    
	@SuppressWarnings("unchecked")
	@Override
	public VoucherList listByStore(MerchantStore store, VoucherCriteria criteria) {
		VoucherList customerList = new VoucherList();
		StringBuilder baseCountQuery =new StringBuilder("select count(c) from Voucher as c where c.id = c.id ");
		StringBuilder baseQuery = new StringBuilder("select c from Voucher as c where c.id = c.id ");
		if(criteria.getId()!=null) {
			String nameQuery =" and c.id <= :pid  ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		
		if(criteria.getBlocked()>0) {
			String nameQuery =" and c.blocked <= :pblocked  ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		
		if(!StringUtils.isBlank(criteria.getApproved())) {
			String nameQuery =" and TO_CHAR(c.approved,'DD/MM/YYYY') = :approved ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		if(!StringUtils.isBlank(criteria.getStartDate())) {
			String nameQuery =" and TO_CHAR(c.startDate,'DD/MM/YYYY') = :startDate ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		if(!StringUtils.isBlank(criteria.getEndDate())) {
			String nameQuery =" and TO_CHAR(c.endDate,'DD/MM/YYYY') = :endDate ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		
		if(criteria.getCustomerId()!=null) {
			String nameQuery =" and c.customerId <= :pcustomerId  ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		if(!StringUtils.isBlank(criteria.getExpire())) {
			String nameQuery =" and TO_CHAR(c.expire,'DD/MM/YYYY') = :expire ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}		
		
		
		baseQuery.append(" order by c.id desc ");
	

		Query countQ = em.createQuery(baseCountQuery.toString());
		//object query
		Query objectQ = em.createQuery(baseQuery.toString());

		if(criteria.getId()!=null) {
			countQ.setParameter("pid",criteria.getId());
			objectQ.setParameter("pid",criteria.getId());
		}
		if(criteria.getBlocked()>0) {
			countQ.setParameter("pblocked",criteria.getBlocked());
			objectQ.setParameter("pblocked",criteria.getBlocked());
		}
		if(!StringUtils.isBlank(criteria.getApproved())) {
			countQ.setParameter("approved",criteria.getApproved());
			objectQ.setParameter("approved",criteria.getApproved());
		}

		if(!StringUtils.isBlank(criteria.getStartDate())) {
			countQ.setParameter("startDate",criteria.getStartDate());
			objectQ.setParameter("startDate",criteria.getStartDate());
		}
		if(!StringUtils.isBlank(criteria.getApproved())) {
			countQ.setParameter("endDate",criteria.getEndDate());
			objectQ.setParameter("endDate",criteria.getEndDate());
		}

		if(criteria.getCustomerId()!=null) {
			countQ.setParameter("customerId",criteria.getCustomerId());
			objectQ.setParameter("customerId",criteria.getCustomerId());
		}
		if(!StringUtils.isBlank(criteria.getExpire())) {
			countQ.setParameter("expire",criteria.getExpire());
			objectQ.setParameter("expire",criteria.getExpire());
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
		
		customerList.setVouchers(objectQ.getResultList());

		return customerList;
		
		
	}    


}
