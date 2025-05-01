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

    @Column(name = "id_notification")
    private Integer notificationId;

    @Column(length = 50)
    private String event;

    @Column(name = "event_date", columnDefinition = "datetime(6)")
    private LocalDateTime eventDate;

    @Column(name = "id_user")
    private Integer userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_notification", insertable = false, updatable = false)
    private Notification notification;

    public Integer getIdUser() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getIdUser'");
    }

    public void setIdUser(Integer userId2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setIdUser'");
    }
}
