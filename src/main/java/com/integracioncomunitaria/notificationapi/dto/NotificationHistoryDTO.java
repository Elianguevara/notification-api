package com.integracioncomunitaria.notificationapi.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO utilizado para enviar al cliente los detalles de un evento
 * en el historial de una notificación.
 * Cada instancia representa una acción (CREATED, VIEWED, DELETED, etc.)
 * ocurrida sobre una notificación.
 */
@Getter
@Setter
@AllArgsConstructor
public class NotificationHistoryDTO {

    /**
     * Identificador único del registro de historial.
     * Corresponde al campo `id_notification_history` en la base de datos.
     */
    private Integer id;

    /**
     * ID de la notificación a la que pertenece este evento.
     * Equivale al campo `id_notification` en la tabla de historial.
     */
    private Integer notificationId;

    /**
     * Tipo de evento registrado, por ejemplo:
     * - "CREATED": cuando se crea la notificación.
     * - "VIEWED": cuando se marca como vista.
     * - "DELETED": cuando se marca como eliminada.
     */
    private String event;

    /**
     * Fecha y hora en que ocurrió el evento.
     * Corresponde al campo `event_date`.
     */
    private LocalDateTime eventDate;

    /**
     * ID del usuario que realizó la acción.
     * Equivale al campo `id_user` en la tabla de historial.
     */
    private Integer userId;

    /**
     * Fecha y hora en que se inserta el registro de historial.
     * Usualmente coincide con `eventDate` y se usa para auditoría adicional.
     * Corresponde al campo `date_create`.
     */
    private LocalDateTime dateCreate;
}
