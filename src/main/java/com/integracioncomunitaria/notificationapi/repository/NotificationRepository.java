// src/main/java/com/integracioncomunitaria/notificationapi/repository/NotificationRepository.java
package com.integracioncomunitaria.notificationapi.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page; // <- CORRECCIÓN 1: Import correcto
import org.springframework.data.domain.Pageable; // <- CORRECCIÓN 2: Añadir este import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.integracioncomunitaria.notificationapi.entity.Notification;

@Repository
public interface NotificationRepository
        extends JpaRepository<Notification, Integer> {

    /**
     * Todas las notificaciones activas de un cliente dado.
     */
    List<Notification> findByCustomer_IdCustomerAndDeletedFalse(Integer customerId);

    /**
     * Todas las notificaciones activas de un proveedor dado.
     */
    List<Notification> findByProvider_IdProviderAndDeletedFalse(Integer providerId);

    /**
     * Búsqueda flexible por cliente, proveedor, visto, rango de fechas
     * y que no estén marcadas como borradas y paginada.
     */
    @Query("""
       SELECT n
         FROM Notification n
        WHERE n.deleted = false
          AND (:customerId IS NULL OR n.customer.idCustomer = :customerId)
          AND (:providerId IS NULL OR n.provider.idProvider = :providerId)
          AND (:viewed     IS NULL OR n.viewed = :viewed)
          AND n.dateCreate BETWEEN :from AND :to
     """)
    Page<Notification> findFiltered(
      @Param("customerId") Integer customerId,
      @Param("providerId") Integer providerId,
      @Param("viewed")     Boolean viewed,
      @Param("from")       LocalDateTime from,
      @Param("to")         LocalDateTime to,
      Pageable pageable
    );
}