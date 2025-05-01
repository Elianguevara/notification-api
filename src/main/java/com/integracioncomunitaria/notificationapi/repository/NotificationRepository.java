package com.integracioncomunitaria.notificationapi.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.integracioncomunitaria.notificationapi.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByCustomerId(Integer customerId);
    List<Notification> findByProviderId(Integer providerId);

    // Si quieres filtros avanzados:
    @Query("""
      SELECT n FROM Notification n
       WHERE (:customerId IS NULL OR n.customer.idCustomer = :customerId)
         AND (:providerId IS NULL  OR n.provider.idProvider = :providerId)
         AND (:viewed IS NULL      OR n.viewed = :viewed)
         AND n.dateCreate BETWEEN :from AND :to
    """)
    List<Notification> findFiltered(
      @Param("customerId") Integer c,
      @Param("providerId") Integer p,
      @Param("viewed") Boolean viewed,
      @Param("from") LocalDateTime from,
      @Param("to") LocalDateTime to
    );
}
