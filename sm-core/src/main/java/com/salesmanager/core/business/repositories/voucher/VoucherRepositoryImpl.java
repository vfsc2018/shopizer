package com.salesmanager.core.business.repositories.voucher;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;

import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.voucher.Voucher;
import com.salesmanager.core.model.voucher.VoucherCriteria;
import com.salesmanager.core.model.voucher.VoucherList;

public class VoucherRepositoryImpl implements VoucherRepositoryCustom {

    @PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public List<Voucher> getActiveVoucher() {

		StringBuilder baseQuery = new StringBuilder("select c from Voucher as c ");
		baseQuery.append(" WHERE c.blocked=0 and c.endDate >= now() ORDER by c.id desc ");
		Query objectQ = em.createQuery(baseQuery.toString());
		return objectQ.getResultList();
	}    
    
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
		
		if(criteria.getBlocked()) {
			String nameQuery =" and c.blocked > 0 ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		
		if(criteria.getApproved()) {
			String nameQuery =" and c.approved is not NULL ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}

		if(criteria.getStartDate()!=null) {
			String nameQuery =" and c.startDate >= :startDate ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		if(criteria.getEndDate()!=null) {
			String nameQuery =" and c.endDate <= :endDate ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}

		if(!StringUtils.isBlank(criteria.getCode())) {
			String nameQuery =" and lower(c.code) LIKE lower(:code) ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}	

		if(!StringUtils.isBlank(criteria.getProduct())) {
			String nameQuery =" and lower(c.productSku) ILIKE lower(:product) ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}

		if(!StringUtils.isBlank(criteria.getManager())) {
			String nameQuery =" and lower(c.manager) LIKE lower(:manager) ";
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

		if(criteria.getId()!=null) {
			countQ.setParameter("pid",criteria.getId());
			objectQ.setParameter("pid",criteria.getId());
		}

		if(criteria.getStartDate()!=null) {
			countQ.setParameter("startDate", criteria.getStartDate());
			objectQ.setParameter("startDate",criteria.getStartDate());
		}
		if(criteria.getEndDate()!=null) {
			countQ.setParameter("endDate",criteria.getEndDate());
			objectQ.setParameter("endDate",criteria.getEndDate());
		}
		if(!StringUtils.isBlank(criteria.getProduct())) {
			String nameParam = new StringBuilder().append("%").append(criteria.getProduct()).append("%").toString();
			countQ.setParameter("product",nameParam);
			objectQ.setParameter("product",nameParam);
		}	
		if(!StringUtils.isBlank(criteria.getCode())) {
			String nameParam = new StringBuilder().append("%").append(criteria.getCode()).append("%").toString();
			countQ.setParameter("code",nameParam);
			objectQ.setParameter("code",nameParam);
		}		
		if(!StringUtils.isBlank(criteria.getManager())) {
			String nameParam = new StringBuilder().append("%").append(criteria.getManager()).append("%").toString();
			countQ.setParameter("manager",nameParam);
			objectQ.setParameter("manager",nameParam);
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
