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

 

    @Column(length = 255)
    private String type;

    @Column(columnDefinition = "text")
    private String message;

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean viewed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_provider", nullable = true)
    private Provider provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_customer", nullable = true)
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
