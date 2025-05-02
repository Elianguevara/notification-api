// src/main/java/com/integracioncomunitaria/notificationapi/entity/NotificationHistory.java
package com.integracioncomunitaria.notificationapi.entity;

import com.integracioncomunitaria.notificationapi.entity.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_history")
@Getter @Setter
public class NotificationHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notification_history")
    private Integer idNotificationHistory;


    @Column(name = "event", length = 50, nullable = false)
    private String event;

    @Column(name = "event_date", columnDefinition = "datetime(6)", nullable = false)
    private LocalDateTime eventDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_notification", nullable = false)
    private Notification notification;

    public NotificationHistory() {
    }

}
