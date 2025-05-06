package com.integracioncomunitaria.notificationapi.config;

// Importaciones de JWT para crear y validar tokens
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Clase de utilidad para generar, validar y obtener información desde tokens JWT.
 * Es usada principalmente para autenticación segura en APIs REST.
 */
@Component
public class JwtTokenUtil {

    // Se inyecta desde application.properties el secreto para firmar JWTs
    @Value("${jwt.secret}")
    private String jwtSecret;

    // Se inyecta desde application.properties el tiempo de expiración del JWT (en milisegundos)
    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    /**
     * Genera la clave secreta usada para firmar el JWT utilizando el algoritmo HS256.
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Genera un token JWT incluyendo el ID del usuario y su rol como claims.
     * El token tendrá una fecha de emisión y una fecha de expiración.
     *
     * @param userId ID del usuario a incluir en el JWT.
     * @param role Rol del usuario (p.ej. "ROLE_CLIENTE").
     * @return Token JWT firmado como String.
     */
    public String generateToken(Integer userId, String role) {
        Date now = new Date(); // Fecha y hora actual
        Date expiry = new Date(now.getTime() + jwtExpirationMs); // Fecha de expiración

        return Jwts.builder()
            .claim("userId", userId) // Claim personalizado con el ID del usuario
            .claim("role", role)     // Claim personalizado con el rol del usuario
            .setIssuedAt(now)        // Fecha de creación del token
            .setExpiration(expiry)   // Fecha de expiración del token
            .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Firma el JWT con clave secreta
            .compact();              // Compacta el token en formato String
    }

    /**
     * Valida y extrae los claims de un token JWT recibido desde el cliente.
     *
     * @param token Token JWT en formato String.
     * @return Objeto con los claims del JWT si es válido.
     * @throws JwtException si el token no es válido o expiró.
     */
    public Jws<Claims> validateAndParse(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey()) // Configura la clave secreta para validar la firma
            .build()
            .parseClaimsJws(token); // Extrae los claims del JWT
    }

    /**
     * Obtiene el ID del usuario desde un token JWT válido.
     *
     * @param token Token JWT del que extraer la información.
     * @return ID del usuario como Integer.
     */
    public Integer getUserId(String token) {
        Claims claims = validateAndParse(token).getBody();
        return claims.get("userId", Integer.class);
    }

    /**
     * Obtiene el rol del usuario desde un token JWT válido.
     *
     * @param token Token JWT del que extraer la información.
     * @return Rol del usuario como String.
     */
    public String getRole(String token) {
        Claims claims = validateAndParse(token).getBody();
        return claims.get("role", String.class);
    }
}
