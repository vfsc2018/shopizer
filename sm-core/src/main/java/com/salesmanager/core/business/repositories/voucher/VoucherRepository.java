package com.salesmanager.core.business.repositories.voucher;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salesmanager.core.model.voucher.Voucher;



public interface VoucherRepository extends JpaRepository<Voucher, Long>,VoucherRepositoryCustom {

/*	@Query("select t from Voucher t join fetch t.order to where to.id = ?1")
	List<Voucher> findByOrder(Long orderId);
	
	@Query("select t from Voucher t join fetch t.order to left join fetch to.orderAttributes toa left join fetch to.orderProducts too left join fetch to.orderTotal toot left join fetch to.orderHistory tood where to is not null and t.transactionDate BETWEEN :from AND :to")
	List<Voucher> findByDates(
			@Param("from") @Temporal(javax.persistence.TemporalType.TIMESTAMP) Date startDate, 
			@Param("to") @Temporal(javax.persistence.TemporalType.TIMESTAMP) Date endDate);*/
}
