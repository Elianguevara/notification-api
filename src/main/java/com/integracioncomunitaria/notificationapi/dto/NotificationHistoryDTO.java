// src/main/java/com/integracioncomunitaria/notificationapi/dto/NotificationHistoryDTO.java
package com.integracioncomunitaria.notificationapi.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Para devolver un evento de historial.
 */
@Getter @Setter @AllArgsConstructor
public class NotificationHistoryDTO {
    private Integer id;             // id_notification_history
    private Integer notificationId; // id_notification
    private String event;           // event
    private LocalDateTime eventDate;// event_date
    private Integer userId;         // id_user
    private LocalDateTime dateCreate; // date_create
}
