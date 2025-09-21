// src/main/java/com/integracioncomunitaria/notificationapi/dto/NotificationCreateDTO.java
package com.integracioncomunitaria.notificationapi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank; // Importar si el mensaje es obligatorio

/**
 * Payload para crear una nueva notificación.
 * El cliente solo envía el mensaje. Los IDs de cliente/proveedor
 * se obtienen automáticamente del usuario logueado en el backend.
 */
@Getter
@Setter
public class NotificationCreateDTO {

    /**
     * Contenido del mensaje de la notificación.
     * Campo obligatorio: describe la información que se desea notificar.
     */
    @NotBlank(message = "El mensaje no puede estar vacío") // Añadir validación si el mensaje es obligatorio
    private String message;

    // Se eliminan providerId y customerId porque se obtienen del usuario logueado en el servicio
    // private Integer providerId;
    // private Integer customerId;
}