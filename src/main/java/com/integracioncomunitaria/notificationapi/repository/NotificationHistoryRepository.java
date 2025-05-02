// src/main/java/com/integracioncomunitaria/notificationapi/repository/NotificationHistoryRepository.java
package com.integracioncomunitaria.notificationapi.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.integracioncomunitaria.notificationapi.entity.NotificationHistory;

@Repository
public interface NotificationHistoryRepository 
        extends JpaRepository<NotificationHistory, Integer> {

    /**
     * Busca todos los historiales asociados a una notificación.
     */
    List<NotificationHistory> findByNotification_IdNotification(Integer notificationId);
}
