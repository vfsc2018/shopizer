package com.salesmanager.core.business.repositories.voucherCode;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;

import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.voucherCode.VoucherCode;
import com.salesmanager.core.model.voucherCode.VoucherCodeCriteria;
import com.salesmanager.core.model.voucherCode.VoucherCodeList;



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
		
		if(criteria.getVoucherId()!=null && criteria.getVoucherId()>0) {
			String nameQuery =" and c.voucher.id = :voucher  ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		
		if(!StringUtils.isBlank(criteria.getCode())) {
			String nameQuery =" and c.code = :code ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		
		if(criteria.getIndex()>0) {
			String nameQuery =" and c.index = :index ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		
		if(criteria.getCustomerId()!=null && criteria.getCustomerId()>0) {
			String nameQuery =" and c.customer.id = :customer  ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		if(!StringUtils.isBlank(criteria.getUsed())) {
			String nameQuery =" and c.used = :used ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		if(!StringUtils.isBlank(criteria.getRedeem())) {
			String nameQuery =" and c.redeem = :redeem ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		
		if(criteria.getOrderId()!=null && criteria.getOrderId()>0) {
			String nameQuery =" and c.order.id = :orderId  ";
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
		
		if(criteria.getVoucherId()!=null && criteria.getVoucherId()>0) {
			countQ.setParameter("voucher",criteria.getVoucherId());
			objectQ.setParameter("voucher",criteria.getVoucherId());
		}
		
		if(!StringUtils.isBlank(criteria.getCode())) {
			countQ.setParameter("code",criteria.getCode());
			objectQ.setParameter("code",criteria.getCode());
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
		if(!StringUtils.isBlank(criteria.getRedeem())) {
			countQ.setParameter("redeem",criteria.getRedeem());
			objectQ.setParameter("redeem",criteria.getRedeem());
		}
		
		if(criteria.getOrderId()!=null && criteria.getOrderId()>0) {
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
	public int getVoucherCodeByVoucherId(Long voucherId) {
		List<VoucherCode> lstdata = new ArrayList<VoucherCode>();
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
	public int countGrByVoucherId(Long voucherId) {

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
