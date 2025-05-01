package com.integracioncomunitaria.notificationapi.dto;

import lombok.Getter;
import lombok.Setter;
// Para crear una notificación
@Getter @Setter
public class NotificationCreateDTO {
    private Integer providerId;
    private Integer customerId;
    private String type;
    private String message;
}
