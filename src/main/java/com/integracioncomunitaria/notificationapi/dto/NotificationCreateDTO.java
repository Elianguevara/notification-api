// src/main/java/com/integracioncomunitaria/notificationapi/dto/NotificationCreateDTO.java
package com.integracioncomunitaria.notificationapi.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Payload para crear una nueva notificación.
 */
@Getter @Setter
public class NotificationCreateDTO {
    private Integer providerId;  // id_provider
    private Integer customerId;  // id_customer
    private String message;      // message
}
