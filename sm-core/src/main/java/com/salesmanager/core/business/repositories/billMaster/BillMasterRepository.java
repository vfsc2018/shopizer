package com.salesmanager.core.business.repositories.billMaster;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.salesmanager.core.model.catalog.product.BillMaster;


public interface BillMasterRepository extends JpaRepository<BillMaster, Long>,BillMasterRepositoryCustom {
//	

	@Query("SELECT COUNT(u) FROM BillMaster u WHERE u.order.id=:pid")
    Long countByOrderId(@Param("pid") Long pid);
	
	@Query("SELECT u FROM BillMaster u WHERE u.order.id=:pid Order by u.id DESC")
	List<BillMaster> findByOrderId(@Param("pid") Long pid);
	
//	
//
//	@Query("select c from Country c left join fetch c.descriptions cd where cd.language.id=?1")
//	List<Country> listByLanguage(Integer id);
//	
//	/** get country including zones by language **/
//	@Query("select distinct c from Country c left join fetch c.descriptions cd left join fetch c.zones cz left join fetch cz.descriptions where cd.language.id=?1")
//	List<Country> listCountryZonesByLanguage(Integer id);

}
