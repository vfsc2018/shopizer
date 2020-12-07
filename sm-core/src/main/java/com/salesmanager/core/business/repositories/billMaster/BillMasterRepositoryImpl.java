package com.salesmanager.core.business.repositories.billMaster;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;

import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.BillMasterCriteria;
import com.salesmanager.core.model.order.BillMasterList;
import com.salesmanager.core.model.order.CollectBill;



public class BillMasterRepositoryImpl implements BillMasterRepositoryCustom {

    @PersistenceContext
    private EntityManager em;
    
    
	@Override
	public List<CollectBill> collectBill(String billIds) {
		StringBuffer sql = new StringBuffer("")
			.append(" select code,name,unit,sum(quantity*quantityOfParent) as quantity,sum(quantity*quantityOfParent*price) as totalMoney from BillItem  ")
			.append(" where billMaster.id in("+ billIds +") ")
			.append(" group by code,unit,name ");

		List<CollectBill> dataList = new ArrayList<CollectBill>();
		List<Object[]> results = em.createQuery(sql.toString(), Object[].class).getResultList();
		for (Object[] row : results) {
			CollectBill bean = new CollectBill();
			bean.setCode((String) row[0]);
			bean.setName((String) row[1]);
			bean.setUnit((String) row[2]);
			bean.setQuantity((Double) row[3]);
			bean.setTotalMoney((Double) row[4]);
			
			//get Quantity parentId
			sql = new StringBuffer("");
			
			
			
			dataList.add(bean);
		}
		
		return dataList;
		
	}
    
	@Override
	public List<CollectBill> collectOrder(String orderIds) {
		StringBuffer sql = new StringBuffer("")
			.append(" select sku,productName,sum(productQuantity),sum(productQuantity*oneTimeCharge) as totalMoney from OrderProduct ")
			.append(" where order.id in("+ orderIds +") ")
			.append(" group by sku,productName ");
		
		List<CollectBill> dataList = new ArrayList<CollectBill>();
		List<Object[]> results = em.createQuery(sql.toString(), Object[].class).getResultList();
		for (Object[] row : results) {
			CollectBill bean = new CollectBill();
			bean.setCode((String) row[0]);
			bean.setName((String) row[1]);
			bean.setQuantity(((Long) row[2]).doubleValue());
			bean.setTotalMoney(((BigDecimal) row[3]).doubleValue());
			dataList.add(bean);
		}
		
		return dataList;
		
	}
	
    
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
		if(!StringUtils.isBlank(criteria.getAddress())) {
			String nameQuery =" and c.address like:addr  ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		if(!StringUtils.isBlank(criteria.getCustomerName())){
			String nameQuery =" and (c.order.billing.firstName like:nm or c.order.billing.lastName like:nm) ";
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

		if(!StringUtils.isBlank(criteria.getDate())) {
			countQ.setParameter("dc",criteria.getDate());
			objectQ.setParameter("dc",criteria.getDate());
		}
		if(!StringUtils.isBlank(criteria.getCustomerName())) {
			String nameParam = new StringBuilder().append("%").append(criteria.getCustomerName()).append("%").toString();
			countQ.setParameter("nm",nameParam);
			objectQ.setParameter("nm",nameParam);
		}

		if(!StringUtils.isBlank(criteria.getPhone())) {
			String nameParam = new StringBuilder().append("%").append(criteria.getPhone()).append("%").toString();
			countQ.setParameter("ph",nameParam);
			objectQ.setParameter("ph",nameParam);
		}

		if(!StringUtils.isBlank(criteria.getAddress())) {
			String nameParam = new StringBuilder().append("%").append(criteria.getAddress()).append("%").toString();
			countQ.setParameter("addr",nameParam);
			objectQ.setParameter("addr",nameParam);
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
