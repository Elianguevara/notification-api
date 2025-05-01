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

    public Notification(Integer notifId) {
        //TODO Auto-generated constructor stub
    }

    public Notification() {
        //TODO Auto-generated constructor stub
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notification")
    private Integer idNotification;

    @Column(name = "id_provider")
    private Integer providerId;

    @Column(name = "id_customer")
    private Integer customerId;

    @Column(length = 255)
    private String type;

    @Column(columnDefinition = "text")
    private String message;

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean viewed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_provider", insertable = false, updatable = false)
    private Provider provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_customer", insertable = false, updatable = false)
    private Customer customer;

    public boolean isViewed() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isViewed'");
    }
}
