package com.salesmanager.core.business.repositories.customer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;

import com.salesmanager.core.model.customer.CustomerCriteria;
import com.salesmanager.core.model.customer.CustomerList;
import com.salesmanager.core.model.merchant.MerchantStore;


public class CustomerRepositoryImpl implements CustomerRepositoryCustom {

	
    @PersistenceContext
    private EntityManager em;
    
	@SuppressWarnings("unchecked")
	@Override
	public CustomerList listByStore(MerchantStore store, CustomerCriteria criteria) {
		

		CustomerList customerList = new CustomerList();
		StringBuilder countBuilderSelect = new StringBuilder();
		StringBuilder objectBuilderSelect = new StringBuilder();
		
		String baseCountQuery = "select count(c) from Customer as c";
		String baseQuery = "select c from Customer as c left join fetch c.auditSection  left join fetch c.delivery.country left join fetch c.delivery.zone left join fetch c.billing.country left join fetch c.billing.zone";
		countBuilderSelect.append(baseCountQuery);
		objectBuilderSelect.append(baseQuery);
		
		StringBuilder countBuilderWhere = new StringBuilder();
		StringBuilder objectBuilderWhere = new StringBuilder();
		String whereQuery = " where c.merchantStore.id=:mId";
		countBuilderWhere.append(whereQuery);
		objectBuilderWhere.append(whereQuery);

		if(criteria.getId()!=null) {
			String nameQuery =" and c.id <= :cid ";
			countBuilderWhere.append(nameQuery);
			objectBuilderWhere.append(nameQuery);
		}
		if(!StringUtils.isBlank(criteria.getDate())) {
			String nameQuery =" and TO_CHAR(c.auditSection.dateCreated,'DD/MM/YYYY') = :dc ";
			countBuilderWhere.append(nameQuery);
			objectBuilderWhere.append(nameQuery);
		}

		if(!StringUtils.isBlank(criteria.getPhone())) {
			String nameQuery =" and c.billing.telephone like :ph ";
			countBuilderWhere.append(nameQuery);
			objectBuilderWhere.append(nameQuery);
		}

		if(!StringUtils.isBlank(criteria.getName())) {
			String nameQuery =" and (lower(c.billing.firstName) LIKE lower(:nm) or c.billing.lastName LIKE :nm) ";
			countBuilderWhere.append(nameQuery);
			objectBuilderWhere.append(nameQuery);
		}
		
		if(!StringUtils.isBlank(criteria.getFirstName())) {
			String nameQuery =" and lower(c.billing.firstName) LIKE lower(:fn) ";
			countBuilderWhere.append(nameQuery);
			objectBuilderWhere.append(nameQuery);
		}

		if(!StringUtils.isBlank(criteria.getAddress())) {
			String nameQuery =" and lower(c.billing.address) LIKE :lower(:addr) ";
			countBuilderWhere.append(nameQuery);
			objectBuilderWhere.append(nameQuery);
		}
		
		if(!StringUtils.isBlank(criteria.getLastName())) {
			String nameQuery =" and c.billing.lastName LIKE :ln";
			countBuilderWhere.append(nameQuery);
			objectBuilderWhere.append(nameQuery);
		}
		
		if(!StringUtils.isBlank(criteria.getEmail())) {
			String mailQuery =" and c.emailAddress like :email";
			countBuilderWhere.append(mailQuery);
			objectBuilderWhere.append(mailQuery);
		}
		
		if(!StringUtils.isBlank(criteria.getCountry())) {
			String countryQuery =" and c.billing.country.isoCode like:country";
			countBuilderWhere.append(countryQuery);
			objectBuilderWhere.append(countryQuery);
		}
		
		objectBuilderSelect.append(" left join fetch c.attributes ca left join fetch ca.customerOption cao left join fetch ca.customerOptionValue cav left join fetch cao.descriptions caod left join fetch cav.descriptions  left join fetch c.groups");
		

		if(!StringUtils.isBlank(criteria.getCriteriaOrderByField())) {
			objectBuilderWhere.append(" order by c." + criteria.getCriteriaOrderByField() + " " + criteria.getOrderBy().name().toLowerCase());
		}else{
			// objectBuilderWhere.append(" order by c.id desc ");
		}
		
		//count query
		Query countQ = em.createQuery(
				countBuilderSelect.toString() + countBuilderWhere.toString());
		
		//object query
		Query objectQ = em.createQuery(
				objectBuilderSelect.toString() + objectBuilderWhere.toString());

		countQ.setParameter("mId", store.getId());
		objectQ.setParameter("mId", store.getId());
		

		if(criteria.getId()!=null) {
			countQ.setParameter("cid",criteria.getId());
			objectQ.setParameter("cid",criteria.getId());
		}
		if(!StringUtils.isBlank(criteria.getDate())) {
			countQ.setParameter("dc",criteria.getDate());
			objectQ.setParameter("dc",criteria.getDate());
		}

		if(!StringUtils.isBlank(criteria.getPhone())) {
			String nameParam = new StringBuilder().append("%").append(criteria.getPhone()).append("%").toString();
			countQ.setParameter("ph",nameParam);
			objectQ.setParameter("ph",nameParam);
		}

		if(!StringUtils.isBlank(criteria.getName())) {
			String nameParam = new StringBuilder().append("%").append(criteria.getName()).append("%").toString();
			countQ.setParameter("nm",nameParam);
			objectQ.setParameter("nm",nameParam);
		}
		
		if(!StringUtils.isBlank(criteria.getFirstName())) {
			String nameParam = new StringBuilder().append("%").append(criteria.getFirstName()).append("%").toString();
			countQ.setParameter("fn",nameParam);
			objectQ.setParameter("fn",nameParam);
		}

		if(!StringUtils.isBlank(criteria.getAddress())) {
			String nameParam = new StringBuilder().append("%").append(criteria.getAddress()).append("%").toString();
			countQ.setParameter("addr",nameParam);
			objectQ.setParameter("addr",nameParam);
		}
		
		if(!StringUtils.isBlank(criteria.getLastName())) {
			String nameParam = new StringBuilder().append("%").append(criteria.getLastName()).append("%").toString();
			countQ.setParameter("ln",nameParam);
			objectQ.setParameter("ln",nameParam);
		}
		
		if(!StringUtils.isBlank(criteria.getEmail())) {
			String email = new StringBuilder().append("%").append(criteria.getEmail()).append("%").toString();
			countQ.setParameter("email",email);
			objectQ.setParameter("email",email);
		}
		
		if(!StringUtils.isBlank(criteria.getCountry())) {
			String country = new StringBuilder().append("%").append(criteria.getCountry()).append("%").toString();
			countQ.setParameter("country",country);
			objectQ.setParameter("country",country);
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
		
		customerList.setCustomers(objectQ.getResultList());

		return customerList;
		
		
	}

    

}
