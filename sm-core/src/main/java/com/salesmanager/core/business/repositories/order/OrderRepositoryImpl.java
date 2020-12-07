package com.salesmanager.core.business.repositories.order;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;

import com.salesmanager.core.business.utils.RepositoryHelper;
import com.salesmanager.core.model.common.CriteriaOrderBy;
import com.salesmanager.core.model.common.GenericEntityList;
import com.salesmanager.core.model.merchant.MerchantStore;
import com.salesmanager.core.model.order.OrderCriteria;
import com.salesmanager.core.model.order.OrderList;
import com.salesmanager.core.model.order.orderstatus.OrderStatus;

public class OrderRepositoryImpl implements OrderRepositoryCustom {

	
    @PersistenceContext
    private EntityManager em;
    
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
    
    /**
     * @deprecated
     */
	@SuppressWarnings("unchecked")
	@Override
	public OrderList listByStore(MerchantStore store, OrderCriteria criteria) {
		

		OrderList orderList = new OrderList();
		StringBuilder countBuilderSelect = new StringBuilder();
		StringBuilder objectBuilderSelect = new StringBuilder();
		
		//com.salesmanager.core.model.order.orderstatus.OrderStatus.valueOf(criteria.getStatus())

		
		
		
		
		// String orderByCriteria = " order by o.id desc";
		
		// if(criteria.getOrderBy()!=null) {
		// 	if(CriteriaOrderBy.ASC.name().equals(criteria.getOrderBy().name())) {
		// 		orderByCriteria = " order by o.id asc";
		// 	}
		// }
		
		String countBaseQuery = "select count(o) from Order as o";
		String baseQuery = "select o from Order as o left join fetch o.orderTotal ot left join fetch o.orderProducts op left join fetch o.orderAttributes oa left join fetch op.orderAttributes opo left join fetch op.prices opp";
		countBuilderSelect.append(countBaseQuery);
		objectBuilderSelect.append(baseQuery);

		
		
		StringBuilder countBuilderWhere = new StringBuilder();
		StringBuilder objectBuilderWhere = new StringBuilder();
		String whereQuery = " where o.merchant.id=:mId";
		countBuilderWhere.append(whereQuery);
		objectBuilderWhere.append(whereQuery);
		
		if(criteria.getPurchased()) {
			String nameQuery =" and o.paymentTime is not NULL ";
			countBuilderWhere.append(nameQuery);
			objectBuilderWhere.append(nameQuery);
		}

		if(!StringUtils.isBlank(criteria.getDate())) {
			String nameQuery =" and TO_CHAR(o.datePurchased,'DD/MM/YYYY') = :dc ";
			countBuilderWhere.append(nameQuery);
			objectBuilderWhere.append(nameQuery);
		}

		if(criteria.getStartDate()!=null) {
			String nameQuery =" and o.fromDate >= :fdate ";
			countBuilderWhere.append(nameQuery);
			objectBuilderWhere.append(nameQuery);
		}
		if(criteria.getEndDate()!=null) {
			String nameQuery =" and o.toDate <= :tdate ";
			countBuilderWhere.append(nameQuery);
			objectBuilderWhere.append(nameQuery);
		}

		if(!StringUtils.isBlank(criteria.getPhone())) {
			String nameQuery =" and o.billing.telephone like:ph ";
			countBuilderWhere.append(nameQuery);
			objectBuilderWhere.append(nameQuery);
		}

		if(!StringUtils.isBlank(criteria.getCustomerName())) {
			String nameQuery =" and (o.billing.firstName like:nm or o.billing.lastName like:nm) ";
			countBuilderWhere.append(nameQuery);
			objectBuilderWhere.append(nameQuery);
		}

		if(!StringUtils.isBlank(criteria.getAddress())) {
			String nameParam = " and (o.billing.address like :addr OR o.billing.city like :addr OR o.billing.state like :addr OR o.delivery.address like :addr OR o.delivery.city like :addr OR o.delivery.state like :addr) "; // or o.billing.zone.code like :addr or o.delivery.zone.code like :addr) ";
			countBuilderWhere.append(nameParam);
			objectBuilderWhere.append(nameParam);
		}
		
		if(!StringUtils.isBlank(criteria.getPaymentMethod())) {
			String paymentQuery =" and o.paymentModuleCode like:pm";
			countBuilderWhere.append(paymentQuery);
			objectBuilderWhere.append(paymentQuery);
		}
		
		if(criteria.getCustomerId()!=null) {
			String customerQuery =" and o.customerId =:cid";
			countBuilderWhere.append(customerQuery);
			objectBuilderWhere.append(customerQuery);
		}
		
		if(criteria.getId()!=null) {
			String pIdQuery =" and o.id <= :pId";
			countBuilderWhere.append(pIdQuery);
			objectBuilderWhere.append(pIdQuery);
		}
		
		if(criteria.getStatus()!=null) {
			String statusQuery =" and o.status =:pStatus";
			countBuilderWhere.append(statusQuery);
			objectBuilderWhere.append(statusQuery);
		}
		
		if(!StringUtils.isBlank(criteria.getCriteriaOrderByField())) {
			objectBuilderWhere.append(" order by o." + criteria.getCriteriaOrderByField() + " " + criteria.getOrderBy().name().toLowerCase());
		}else{
			objectBuilderWhere.append(" order by o.id desc ");
		}

		//count query
		Query countQ = em.createQuery(
				countBuilderSelect.toString() + countBuilderWhere.toString());
		
		//object query
		Query objectQ = em.createQuery(
				objectBuilderSelect.toString() + objectBuilderWhere.toString());

		countQ.setParameter("mId", store.getId());
		objectQ.setParameter("mId", store.getId());

		if(criteria.getAddress()!=null) {
			String nameParam = new StringBuilder().append("%").append(criteria.getAddress()).append("%").toString();
			countQ.setParameter("addr",nameParam);
			objectQ.setParameter("addr",nameParam);
		}

		if(criteria.getDate()!=null) {
			countQ.setParameter("dc",criteria.getDate());
			objectQ.setParameter("dc",criteria.getDate());
		}

		if(criteria.getStartDate()!=null) {
			countQ.setParameter("fdate", criteria.getStartDate());
			objectQ.setParameter("fdate",criteria.getStartDate());
		}
		if(criteria.getEndDate()!=null) {
			countQ.setParameter("tdate",criteria.getEndDate());
			objectQ.setParameter("tdate",criteria.getEndDate());
		}

		if(!StringUtils.isBlank(criteria.getPhone())) {
			String nameParam = new StringBuilder().append("%").append(criteria.getPhone()).append("%").toString();
			countQ.setParameter("ph",nameParam);
			objectQ.setParameter("ph",nameParam);
		}

		if(!StringUtils.isBlank(criteria.getCustomerName())) {
			String nameParam = new StringBuilder().append("%").append(criteria.getCustomerName()).append("%").toString();
			countQ.setParameter("nm",nameParam);
			objectQ.setParameter("nm",nameParam);
		}
		
		if(!StringUtils.isBlank(criteria.getPaymentMethod())) {
			String payementParam = new StringBuilder().append("%").append(criteria.getPaymentMethod()).append("%").toString();
			countQ.setParameter("pm",payementParam);
			objectQ.setParameter("pm",payementParam);
		}
		
		if(criteria.getCustomerId()!=null) {
			countQ.setParameter("cid", criteria.getCustomerId());
			objectQ.setParameter("cid",criteria.getCustomerId());
		}
		
		if(criteria.getId()!=null) {
			countQ.setParameter("pId", criteria.getId());
			objectQ.setParameter("pId",criteria.getId());
		}
		
		
		
		if(criteria.getStatus()!=null) {
			if(!checkEnum(criteria.getStatus())){
				countQ.setParameter("pStatus",null);
				objectQ.setParameter("pStatus",null);				
			}else{
				countQ.setParameter("pStatus",OrderStatus.valueOf(criteria.getStatus()));
				objectQ.setParameter("pStatus",OrderStatus.valueOf(criteria.getStatus()));
			}
		}
		

		Number count = (Number) countQ.getSingleResult();

		orderList.setTotalCount(count.intValue());
		
        if(count.intValue()==0)
        	return orderList;
        
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
		
    	orderList.setOrders(objectQ.getResultList());

		return orderList;
		
		
	}

	@Override
	public OrderList listOrders(MerchantStore store, OrderCriteria criteria) {
		OrderList orderList = new OrderList();
		StringBuilder countBuilderSelect = new StringBuilder();
		StringBuilder objectBuilderSelect = new StringBuilder();

		String orderByCriteria = " order by o.id desc";

		if(criteria.getOrderBy()!=null) {
			if(CriteriaOrderBy.ASC.name().equals(criteria.getOrderBy().name())) {
				orderByCriteria = " order by o.id asc";
			}
		}

		
		String baseQuery = "select o from Order as o left join fetch o.delivery.country left join fetch o.delivery.zone left join fetch o.billing.country left join fetch o.billing.zone left join fetch o.orderTotal ot left join fetch o.orderProducts op left join fetch o.orderAttributes oa left join fetch op.orderAttributes opo left join fetch op.prices opp";
		String countBaseQuery = "select count(o) from Order as o";
		
		countBuilderSelect.append(countBaseQuery);
		objectBuilderSelect.append(baseQuery);

		StringBuilder objectBuilderWhere = new StringBuilder();

		String storeQuery =" where o.merchant.code=:mCode";;
		objectBuilderWhere.append(storeQuery);
		countBuilderSelect.append(storeQuery);
		
		if(!StringUtils.isEmpty(criteria.getCustomerName())) {
			String nameQuery =  " and o.billing.firstName like:name or o.billing.lastName like:name";
			objectBuilderWhere.append(nameQuery);
			countBuilderSelect.append(nameQuery);
		}


		if(!StringUtils.isBlank(criteria.getAddress())) {
			String nameQuery =" and o.billing.address like:addr";
			countBuilderSelect.append(nameQuery);
			objectBuilderWhere.append(nameQuery);
		}
		
		if(!StringUtils.isEmpty(criteria.getEmail())) {
			String nameQuery =  " and o.customerEmailAddress like:email";
			objectBuilderWhere.append(nameQuery);
			countBuilderSelect.append(nameQuery);
		}
		
		//id
		if(criteria.getId() != null) {
			String nameQuery =  " and str(o.id) like:id";
			objectBuilderWhere.append(nameQuery);
			countBuilderSelect.append(nameQuery);
		}
		
		//phone
		if(!StringUtils.isEmpty(criteria.getCustomerPhone())) {
			String nameQuery =  " and o.billing.telephone like:phone or o.delivery.telephone like:phone";
			objectBuilderWhere.append(nameQuery);
			countBuilderSelect.append(nameQuery);
		}
		
		//status
		if(!StringUtils.isEmpty(criteria.getStatus())) {
			String nameQuery =  " and o.status =:status";
			objectBuilderWhere.append(nameQuery);
			countBuilderSelect.append(nameQuery);
		}
	
		objectBuilderWhere.append(orderByCriteria);

		//count query
		Query countQ = em.createQuery(
				countBuilderSelect.toString());

		//object query
		Query objectQ = em.createQuery(
				objectBuilderSelect.toString() + objectBuilderWhere.toString());
		
		//customer name
		if(!StringUtils.isEmpty(criteria.getCustomerName())) {
			countQ.setParameter("name", like(criteria.getCustomerName()));
			objectQ.setParameter("name", like(criteria.getCustomerName()));
		}
		if(!StringUtils.isEmpty(criteria.getAddress())) {
			countQ.setParameter("addr", like(criteria.getAddress()));
			objectQ.setParameter("addr", like(criteria.getAddress()));
		}
		//email
		if(!StringUtils.isEmpty(criteria.getEmail())) {
			countQ.setParameter("email", like(criteria.getEmail()));
			objectQ.setParameter("email", like(criteria.getEmail()));			
		}
		
		//id
		if(criteria.getId() != null) {
			countQ.setParameter("id", like(String.valueOf(criteria.getId())));
			objectQ.setParameter("id", like(String.valueOf(criteria.getId())));
		}
		
		//phone
		if(!StringUtils.isEmpty(criteria.getCustomerPhone())) {
			countQ.setParameter("phone", like(criteria.getCustomerPhone()));
			objectQ.setParameter("phone", like(criteria.getCustomerPhone()));
		}
		
		//status
		if(!StringUtils.isEmpty(criteria.getStatus())) {
			countQ.setParameter("status", OrderStatus.valueOf(criteria.getStatus().toUpperCase()));
			objectQ.setParameter("status", OrderStatus.valueOf(criteria.getStatus().toUpperCase()));
		}
		

		countQ.setParameter("mCode", store.getCode());
		objectQ.setParameter("mCode", store.getCode());


		Number count = (Number) countQ.getSingleResult();

		if(count.intValue()==0)
			return orderList;

	    @SuppressWarnings("rawtypes")
		GenericEntityList entityList = new GenericEntityList();
	    entityList.setTotalCount(count.intValue());
		
		objectQ = RepositoryHelper.paginateQuery(objectQ, count, entityList, criteria);
		
		//TODO use GenericEntityList

		orderList.setTotalCount(entityList.getTotalCount());
		orderList.setTotalPages(entityList.getTotalPages());

		orderList.setOrders(objectQ.getResultList());

		return orderList;
	}
	
	private String like(String q) {
		return '%' + q + '%';
	}


}
