package com.salesmanager.core.business.repositories.vouchercode;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;

import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.vouchercode.VoucherCode;
import com.salesmanager.core.model.vouchercode.VoucherCodeCriteria;
import com.salesmanager.core.model.vouchercode.VoucherCodeList;



public class VoucherCodeRepositoryImpl implements VoucherCodeRepositoryCustom {

    @PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public VoucherCodeList listByStore(MerchantStore store, VoucherCodeCriteria criteria) {
		VoucherCodeList customerList = new VoucherCodeList();
		StringBuilder baseCountQuery =new StringBuilder("select count(c) from VoucherCode as c where c.id = c.id ");
		StringBuilder baseQuery = new StringBuilder("select c from VoucherCode as c where c.id = c.id ");
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
		
		if(criteria.getVoucherId()!=null) {
			String nameQuery =" and c.voucher.id = :voucher  ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		
		if(!StringUtils.isBlank(criteria.getCode())) {
			String nameQuery =" and c.code LIKE :code ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}

		if(!StringUtils.isBlank(criteria.getBatch())) {
			String nameQuery =" and lower(c.batch) LIKE lower(:batch) ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		
		if(criteria.getIndex()>0) {
			String nameQuery =" and c.index <= :index ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		
		if(criteria.getCustomerId()!=null) {
			String nameQuery =" and c.customer.id = :customer ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		if(criteria.getUsed()!=null) {
			String nameQuery =" and TO_CHAR(c.used,'DD/MM/YYYY') = :used ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		
		if(criteria.getOrderId()!=null) {
			String nameQuery =" and c.order.id = :orderId  ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}

		if(criteria.getAvailable()) {
			String nameQuery =" and c.used is NULL ";
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

		if(!StringUtils.isBlank(criteria.getCode())) {
			String nameParam = new StringBuilder().append("%").append(criteria.getCode()).append("%").toString();
			countQ.setParameter("code",nameParam);
			objectQ.setParameter("code",nameParam);
		}	

		if(!StringUtils.isBlank(criteria.getBatch())) {
			String nameParam = new StringBuilder().append("%").append(criteria.getBatch()).append("%").toString();
			countQ.setParameter("batch",nameParam);
			objectQ.setParameter("batch",nameParam);
		}	
		
		if(criteria.getVoucherId()!=null) {
			countQ.setParameter("voucher",criteria.getVoucherId());
			objectQ.setParameter("voucher",criteria.getVoucherId());
		}
		
		if(criteria.getIndex()>0) {
			countQ.setParameter("index",criteria.getIndex());
			objectQ.setParameter("index",criteria.getIndex());
		}
		
		
		if(criteria.getCustomerId()!=null && criteria.getCustomerId()>0) {
			countQ.setParameter("customer",criteria.getCustomerId());
			objectQ.setParameter("customer",criteria.getCustomerId());
		}
		if(!StringUtils.isBlank(criteria.getUsed())) {
			countQ.setParameter("used",criteria.getUsed());
			objectQ.setParameter("used",criteria.getUsed());
		}
		
		if(criteria.getOrderId()!=null) {
			countQ.setParameter("orderId",criteria.getOrderId());
			objectQ.setParameter("orderId",criteria.getOrderId());
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
		
		customerList.setVoucherCodes(objectQ.getResultList());

		return customerList;
		
		
	}    

	@Override
	public VoucherCode getVoucherCode(Long voucherId, Integer index) {
		StringBuilder baseQuery = new StringBuilder("select c from VoucherCode as c where c.voucher.id = ?1 AND c.index=?2 order by c.id");


		Query objectQ = em.createQuery(baseQuery.toString());
		objectQ.setParameter(1,voucherId);
		objectQ.setParameter(2, index);
		try{
			return (VoucherCode)objectQ.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}    

	@Override
	public VoucherCode getVoucherCode(String code) {
		StringBuilder baseQuery = new StringBuilder("select c from VoucherCode as c where c.code = ?1 order by c.id");


		Query objectQ = em.createQuery(baseQuery.toString());
		objectQ.setParameter(1,code);
		try{
			return (VoucherCode)objectQ.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}    
	
	
	@Override
	public int countCodeByVoucherId(Long voucherId) {
		StringBuilder baseQuery = new StringBuilder("select count(c) from VoucherCode as c where c.id = c.id ");

		if(voucherId!=null && voucherId>0) {
			String nameQuery =" and c.voucher.id = :voucher  ";
			baseQuery.append(nameQuery);
		}


		Query objectQ = em.createQuery(baseQuery.toString());
		if(voucherId!=null && voucherId>0) {
			objectQ.setParameter("voucher",voucherId);
		}
		Number count = (Number) objectQ.getSingleResult();
		return count.intValue();
		
		
	}    

	
	@Override
	public int getMaxIndexByVoucherId(Long voucherId) {

		StringBuilder baseQuery = new StringBuilder("select max(c.index) from VoucherCode as c where c.id = c.id ");
		if(voucherId!=null && voucherId>0){
			String nameQuery =" and c.voucher.id = :voucher  ";
			baseQuery.append(nameQuery);
		}
		Query objectQ = em.createQuery(baseQuery.toString());
		if(voucherId!=null && voucherId>0) {
			objectQ.setParameter("voucher",voucherId);
		}
		Number count = (Number)objectQ.getSingleResult();
		if(count==null) return 0;
		return count.intValue();
		
		
	}   
}
