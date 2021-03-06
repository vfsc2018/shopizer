package com.salesmanager.core.business.repositories.billMaster;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;

import com.salesmanager.core.business.utils.ProductPriceUtils;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.BillMasterCriteria;
import com.salesmanager.core.model.order.BillMasterList;
import com.salesmanager.core.model.order.CollectBill;
import com.salesmanager.core.model.order.orderstatus.OrderStatus;



public class BillMasterRepositoryImpl implements BillMasterRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Inject
	private ProductPriceUtils priceUtils;
    
	@Override
	public List<CollectBill> collectBill(String billIds) {
		String whereId = "";
		if(StringUtils.isNotBlank(billIds)){
			whereId = " billMaster.id in ("+ billIds +") and ";
		}
		StringBuilder sql = new StringBuilder("")
			.append(" select code,name,unit,sum(quantity*quantityOfParent) as quantity,sum(quantity*quantityOfParent*price) as totalMoney from BillItem  ")
			.append(" where " + whereId + " parentId>0 ")
			.append(" group by code,unit,name ");

		List<CollectBill> dataList = new ArrayList<>();
		List<Object[]> results = em.createQuery(sql.toString(), Object[].class).getResultList();
		for (Object[] row : results) {
			CollectBill bean = new CollectBill();
			bean.setCode((String) row[0]);
			bean.setName((String) row[1]);
			bean.setUnit((String) row[2]);
			bean.setQuantity(priceUtils.getFormatedQuantity(row[3]));
			bean.setTotalMoney(priceUtils.getTotalMoney(row[4]));
			
			//get Quantity parentId
			// sql = new StringBuffer("");
			
			
			
			dataList.add(bean);
		}
		
		return dataList;
		
	}
    
	@Override
	public List<CollectBill> collectOrder(String orderIds) {
		StringBuilder sql = new StringBuilder("")
			.append(" select sku,productName,sum(productQuantity),sum(productQuantity*oneTimeCharge) as totalMoney from OrderProduct ")
			.append(" where order.id in("+ orderIds +") ")
			.append(" group by sku,productName ");
		
		List<CollectBill> dataList = new ArrayList<>();
		List<Object[]> results = em.createQuery(sql.toString(), Object[].class).getResultList();
		for (Object[] row : results) {
			CollectBill bean = new CollectBill();
			bean.setCode((String) row[0]);
			bean.setName((String) row[1]);
			bean.setQuantity(priceUtils.getFormatedQuantity(row[2]));
			bean.setTotalMoney(priceUtils.getTotalMoney(row[3]));
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
			String nameQuery =" and lower(c.address) LIKE lower(:addr)  ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		if(!StringUtils.isBlank(criteria.getCustomerName())){
			String nameQuery =" and (lower(c.order.billing.firstName) LIKE lower(:nm) or c.order.billing.lastName LIKE :nm) ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		if(!StringUtils.isBlank(criteria.getPhone())) {
			String nameQuery =" and c.phone like :ph ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		
		if(!StringUtils.isBlank(criteria.getSku())) {
			String nameQuery =" and lower(c.sku) LIKE lower(:sk) ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}
		
		if(!StringUtils.isBlank(criteria.getProductName())) {
			String nameQuery =" and lower(c.productName) LIKE lower(:nm) ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}

		if(criteria.getId()!=null) {
			String nameQuery = " and c.id <= :pId ";
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
		}

		if(criteria.getOrderId()!=null) {
			String nameQuery = " and c.order.id = :pOrderId " ;
			baseCountQuery.append(nameQuery);
			baseQuery.append(nameQuery);
			
		}
		if(!StringUtils.isBlank(criteria.getStatus())) {
			String nameQuery = " and c.status = :pStatus " ;
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
			if(!checkEnum(criteria.getStatus())){
				countQ.setParameter("pStatus",null);
				objectQ.setParameter("pStatus",null);				
			}else{			
				countQ.setParameter("pStatus",OrderStatus.valueOf(criteria.getStatus()));
				objectQ.setParameter("pStatus",OrderStatus.valueOf(criteria.getStatus()));	
			}		
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

	private boolean checkEnum(String input){
    	boolean result = false;
    	try {
			//if(criteria.getStatus()!=null && criteria.getStatus().equals(OrderStatus.))
    		for (OrderStatus status : OrderStatus.values()) {
    			if(input!=null && input.equals(status.name())){
    				result = true;
    				break;
    			}
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return result;
    	
    }

}
