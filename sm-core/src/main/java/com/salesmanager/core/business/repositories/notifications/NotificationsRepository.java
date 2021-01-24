package com.salesmanager.core.business.repositories.notifications;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salesmanager.core.model.message.Notifications;
import java.util.List;

public interface NotificationsRepository extends JpaRepository<Notifications, Long>, NotificationsRepositoryCustom {

	// Notifications findById(Long id);

	@Query("SELECT COUNT(u) FROM Notifications u WHERE u.order.id=?1")
    Integer countByOrderId(Long id);
	
	@Query("SELECT u FROM Notifications u WHERE u.order.id=?1 Order by u.id DESC")
	List<Notifications> findByOrderId(Long id, Pageable page);

	@Query("SELECT COUNT(u) FROM Notifications u WHERE u.customer.id=?1")
	Integer countByCustomerId(Long id);
	
	@Query("SELECT COUNT(u) FROM Notifications u WHERE u.customer.id=?1 AND u.read=?2")
    Integer countByCustomerId(Long id, Integer read);
	
	@Query("SELECT u FROM Notifications u WHERE u.customer.id=?1 Order by u.id DESC")
	List<Notifications> findByCustomerId(Long id, Pageable page);

	@Query("SELECT u FROM Notifications u WHERE u.customer.id=?1 AND u.read=?2 Order by u.id DESC")
	List<Notifications> findByCustomerId(Long id, Integer read, Pageable page);
	
}
