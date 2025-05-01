package com.integracioncomunitaria.notificationapi.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// Para devolver al cliente
@Getter @Setter @AllArgsConstructor
public class NotificationDTO {
    private Integer id;
    private Integer providerId;
    private Integer customerId;
    private String type;
    private String message;
    private boolean viewed;
    private LocalDateTime dateCreate;
}
