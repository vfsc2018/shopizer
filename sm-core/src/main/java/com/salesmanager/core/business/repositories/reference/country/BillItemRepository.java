package com.salesmanager.core.business.repositories.reference.country;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salesmanager.core.model.order.BillItem;


public interface BillItemRepository extends JpaRepository <BillItem, Long> {
//	
//	@Query("select c from Country c left join fetch c.descriptions cd where c.isoCode=?1")
//	Country findByIsoCode(String code);
//	
//
	@Query("select c from BillItem c where c.billMaster.id=?1 order by c.id asc")
	List<BillItem> getItemByBillId(Long billId);
//	
//	/** get country including zones by language **/
//	@Query("select distinct c from Country c left join fetch c.descriptions cd left join fetch c.zones cz left join fetch cz.descriptions where cd.language.id=?1")
//	List<Country> listCountryZonesByLanguage(Integer id);

}
