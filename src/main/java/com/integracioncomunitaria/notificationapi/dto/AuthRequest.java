package com.integracioncomunitaria.notificationapi.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO utilizado para recibir las credenciales de un usuario
 * cuando intenta autenticarse (login).
 */
@Getter
@Setter
public class AuthRequest {

    /**
     * Email del usuario que se va a autenticar.
     * Debe coincidir con el registrado en la base de datos.
     */
    private String email;

    /**
     * Contraseña del usuario que se va a autenticar.
     * Se comparará (tras codificarla) con la almacenada en la base de datos.
     */
    private String password;
}
