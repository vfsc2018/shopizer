package com.salesmanager.core.business.repositories.notifications;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salesmanager.core.model.message.Notifications;


public interface NotificationsRepository extends JpaRepository<Notifications, Long>, NotificationsRepositoryCustom {

/*	@Query("SELECT COUNT(u) FROM Notifications u WHERE u.order.id=:pid")
    Long countByOrderId(@Param("pid") Long pid);
	
	@Query("SELECT u FROM Notifications u WHERE u.order.id=:pid Order by u.id DESC")
	List<Notifications> findByOrderId(@Param("pid") Long pid);

	@Query("SELECT COUNT(u) FROM Notifications u WHERE u.customer.id=:pid")
    Long countByCustomerId(@Param("pid") Long pid);
	
	@Query("SELECT u FROM Notifications u WHERE u.customer.id=:pid Order by u.id DESC")
	List<Notifications> findByCustomerId(@Param("pid") Long pid);*/
	
}
