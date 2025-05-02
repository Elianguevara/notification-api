package com.integracioncomunitaria.notificationapi.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.integracioncomunitaria.notificationapi.entity.NotificationHistory;
@Repository
public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, Integer> {
    /**
     * Busca los historiales por el ID de la notificación
     * ahora a través de la propiedad notification.idNotification
     */
    List<NotificationHistory> findByNotification_IdNotification(Integer notificationId);
}
