// src/main/java/com/integracioncomunitaria/notificationapi/entity/Notification.java
package com.integracioncomunitaria.notificationapi.entity;

import com.integracioncomunitaria.notificationapi.entity.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "notification")
@Getter @Setter
public class Notification extends BaseEntity {

    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notification")
    private Integer idNotification;

    @Column(name = "deleted", columnDefinition = "TINYINT(1)", nullable = false)
    private Boolean deleted = false;

    @Column(name = "message", columnDefinition = "text")
    private String message;

    @Column(name = "viewed", columnDefinition = "TINYINT(1)")
    private Boolean viewed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_provider")
    private Provider provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_customer")
    private Customer customer;

    public boolean isViewed() {
        return Boolean.TRUE.equals(this.viewed);
      }

    public Notification(Integer idNotification) {
        this.idNotification = idNotification;
    }

    public Notification() {
    }
      

      
}
