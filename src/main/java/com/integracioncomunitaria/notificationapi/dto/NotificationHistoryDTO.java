package com.integracioncomunitaria.notificationapi.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// Para el historial de eventos
@Getter @Setter @AllArgsConstructor
public class NotificationHistoryDTO {
    private Integer id;
    private Integer notificationId;
    private String event;
    private LocalDateTime eventDate;
    private Integer userId;
}