package com.salesmanager.core.business.repositories.order;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;



public class BillMasterRepositoryImpl implements BillMasterRepositoryCustom {

    @PersistenceContext
    private EntityManager em;
    


}
