package com.integracioncomunitaria.notificationapi.config;

// Importación de clases para manejar peticiones HTTP y excepciones
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Anotación para indicar que un parámetro no puede ser nulo
import org.springframework.lang.NonNull;

// Importaciones necesarias para manejar autenticación de Spring Security
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

// Manejo de excepciones específicas de JWT
import io.jsonwebtoken.JwtException;

import java.io.IOException;
import java.util.List;

/**
 * Filtro JWT que intercepta cada petición HTTP una sola vez para validar el token JWT.
 * Si el token es válido, autentica al usuario y establece su contexto de seguridad.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Utilidad personalizada para trabajar con tokens JWT
    private final JwtTokenUtil jwtUtil;

    // Constructor que recibe la dependencia JwtTokenUtil por inyección
    public JwtAuthenticationFilter(JwtTokenUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Método que se ejecuta automáticamente para cada petición HTTP.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain)
                                    throws ServletException, IOException {

        // Obtiene la cabecera "Authorization" desde la petición HTTP
        String header = request.getHeader("Authorization");

        // Verifica si el encabezado contiene un token JWT válido con formato "Bearer <token>"
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            
            // Extrae solo el token JWT desde el encabezado, descartando el prefijo "Bearer "
            String token = header.substring(7);

            try {
                // Obtiene el ID del usuario contenido en el token JWT
                Integer userId = jwtUtil.getUserId(token);

                // Obtiene el rol del usuario desde el token JWT
                String role = jwtUtil.getRole(token);

                // Crea un objeto de autenticación basado en el ID de usuario y su rol
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userId,    // Identificador del usuario autenticado (puede ser un objeto más complejo según tu implementación)
                        null,      // Contraseña (no se utiliza porque ya tenemos un token JWT validado)
                        List.of(new SimpleGrantedAuthority(role)) // Roles/autoridades del usuario autenticado
                );

                // Establece la autenticación generada en el contexto de seguridad de Spring
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (JwtException ex) {
                // Si hay un error en la validación del JWT (token inválido o expirado), se captura aquí
                // Actualmente no realiza ninguna acción adicional, solo deja la autenticación en blanco
            }
        }

        // Continúa con el resto de filtros y finalmente la petición llega al controlador
        chain.doFilter(request, response);
    }
}
