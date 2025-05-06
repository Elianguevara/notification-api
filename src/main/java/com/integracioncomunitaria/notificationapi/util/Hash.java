package com.integracioncomunitaria.notificationapi.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Clase de utilidad para generar hashes seguros de contraseñas
 * usando el algoritmo BCrypt. Útil para pruebas o pre-cálculo de hashes.
 */
public class Hash {

    /**
     * Punto de entrada para generar un hash BCrypt de una contraseña de ejemplo.
     * En un entorno real, el encode se realizaría dentro de un PasswordEncoder configurado en Spring.
     */
    public static void main(String[] args) {
        // Texto plano de la contraseña a hashear
        String rawPassword = "1234";

        // Crea un codificador BCrypt con fuerza por defecto (10 salt rounds)
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Genera el hash seguro de la contraseña
        String hash = encoder.encode(rawPassword);

        // Muestra en consola el hash resultante
        System.out.println("El hash es " + hash);
    }
}
