package com.integracioncomunitaria.notificationapi.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO utilizado para enviar al cliente la información de una notificación.
 * Contiene los datos esenciales que no exponen directamente la entidad JPA.
 */
@Getter
@Setter
@AllArgsConstructor
public class NotificationDTO {

    /**
     * Identificador único de la notificación.
     * Corresponde al campo `id_notification` en la base de datos.
     */
    private Integer id;

    /**
     * ID del proveedor asociado a esta notificación.
     * Corresponde al campo `id_provider`. Es nulo si la notificación no va dirigida a un proveedor.
     */
    private Integer providerId;

    /**
     * ID del cliente asociado a esta notificación.
     * Corresponde al campo `id_customer`. Es nulo si la notificación no va dirigida a un cliente.
     */
    private Integer customerId;

    /**
     * Contenido del mensaje de la notificación.
     * Equivale al campo `message`.
     */
    @NotBlank(message = "El mensaje no puede estar vacío") // Añadir validación si el mensaje es obligatorio
    private String message;

    /**
     * Indicador de si la notificación ha sido vista por el destinatario.
     * Equivale al campo `viewed`.
     */
    private boolean viewed;

    /**
     * Marca de tiempo de cuándo se creó la notificación.
     * Equivale al campo `date_create`.
     */
    private LocalDateTime dateCreate;

    /**
     * Indicador de borrado lógico (soft delete).
     * true si la notificación ha sido marcada como eliminada.
     */
    private boolean deleted;
}
