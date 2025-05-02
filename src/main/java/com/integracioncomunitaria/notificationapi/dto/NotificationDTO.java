// src/main/java/com/integracioncomunitaria/notificationapi/dto/NotificationDTO.java
package com.integracioncomunitaria.notificationapi.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Para devolver una notificación al cliente.
 */
@Getter @Setter @AllArgsConstructor
public class NotificationDTO {
    private Integer id;           // id_notification
    private Integer providerId;   // id_provider
    private Integer customerId;   // id_customer
    private String message;       // message
    private boolean viewed;       // viewed
    private LocalDateTime dateCreate; // date_create
    private boolean deleted;


}
