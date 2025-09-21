// src/main/java/com/integracioncomunitaria/notificationapi/repository/NotificationHistoryRepository.java
package com.integracioncomunitaria.notificationapi.repository;

import org.springframework.data.domain.Page; // <- CORRECCIÓN 1: Import correcto
import org.springframework.data.domain.Pageable; // <- CORRECCIÓN 2: Añadir este import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.integracioncomunitaria.notificationapi.entity.NotificationHistory;

@Repository
public interface NotificationHistoryRepository 
        extends JpaRepository<NotificationHistory, Integer> {

    /**
     * Busca todos los historiales asociados a una notificación, paginado.
     */
    Page<NotificationHistory> findByNotification_IdNotification(
        Integer notificationId, 
        Pageable pageable
    );
}