package com.integracioncomunitaria.notificationapi.dto;

/**
 * DTO utilizado para enviar al cliente el token JWT generado
 * tras un inicio de sesión exitoso.
 */
public class AuthResponse {

    /**
     * Token JWT firmado que el cliente debe incluir en
     * la cabecera Authorization ("Bearer <token>") de futuras peticiones.
     */
    private String token;

    /**
     * Constructor que inicializa el DTO con el token generado.
     *
     * @param token Token JWT resultante del proceso de autenticación.
     */
    public AuthResponse(String token) {
        this.token = token;
    }

    /**
     * Getter para obtener el token JWT.
     *
     * @return Cadena con el token JWT.
     */
    public String getToken() {
        return token;
    }

    /**
     * Setter opcional si se requiere modificar el token después de crear el DTO.
     *
     * @param token Nuevo valor del token JWT.
     */
    public void setToken(String token) {
        this.token = token;
    }
}
