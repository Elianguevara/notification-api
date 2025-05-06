package com.integracioncomunitaria.notificationapi.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) utilizado para recibir los datos
 * necesarios al crear una nueva notificación vía API REST.
 */
@Getter
@Setter
public class NotificationCreateDTO {

    /**
     * ID del proveedor al que se asocia la notificación.
     * Puede ser null si la notificación no está dirigida a un proveedor.
     */
    private Integer providerId;

    /**
     * ID del cliente al que se asocia la notificación.
     * Puede ser null si la notificación no está dirigida a un cliente.
     */
    private Integer customerId;

    /**
     * Contenido del mensaje de la notificación.
     * Campo obligatorio: describe la información que se desea notificar.
     */
    private String message;
}
