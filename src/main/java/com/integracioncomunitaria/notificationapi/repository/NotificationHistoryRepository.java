package com.integracioncomunitaria.notificationapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.integracioncomunitaria.notificationapi.entity.NotificationHistory;

public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, Integer> {
    List<NotificationHistory> findByNotificationId(Integer notificationId);
}
