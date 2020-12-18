package com.salesmanager.core.business.repositories.notifications;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.salesmanager.core.model.message.Notifications;
import java.util.List;

public interface NotificationsRepository extends JpaRepository<Notifications, Long>, NotificationsRepositoryCustom {

	@Query("SELECT COUNT(u) FROM Notifications u WHERE u.order.id=:pid")
    Integer countByOrderId(@Param("pid") Long pid);
	
	@Query("SELECT u FROM Notifications u WHERE u.order.id=:pid Order by u.id DESC")
	List<Notifications> findByOrderId(@Param("pid") Long pid, Pageable page);

	@Query("SELECT COUNT(u) FROM Notifications u WHERE u.customer.id=:pid")
	Integer countByCustomerId(@Param("pid") Long pid);
	
	@Query("SELECT COUNT(u) FROM Notifications u WHERE u.customer.id=?1 AND u.read=?2")
    Integer countByCustomerId(Long id, Integer read);
	
	@Query("SELECT u FROM Notifications u WHERE u.customer.id=:pid Order by u.id DESC")
	List<Notifications> findByCustomerId(@Param("pid") Long pid, Pageable page);


	@Query("SELECT u FROM Notifications u WHERE u.customer.id=?1 AND u.read=?2 Order by u.id DESC")
	List<Notifications> findByCustomerId(Long id, Integer read, Pageable page);
	
}
