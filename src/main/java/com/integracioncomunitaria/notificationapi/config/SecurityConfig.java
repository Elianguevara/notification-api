package com.integracioncomunitaria.notificationapi.config;

// Importaciones para configuración general de Spring
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Importaciones para JPA y auditoría
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// Importaciones de Spring Security
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Importaciones para CORS (Cross-Origin Resource Sharing)
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// Importación del servicio personalizado de usuario
import com.integracioncomunitaria.notificationapi.service.CustomUserDetailsService;

import java.util.List;
import java.util.Optional;

/**
 * Clase principal de configuración de seguridad y auditoría para Spring Security y JPA.
 */
@Configuration
@EnableWebSecurity // Activa la seguridad web en la aplicación
@EnableJpaAuditing(auditorAwareRef = "auditorProvider") // Activa la auditoría automática en entidades JPA
public class SecurityConfig {

    // Dependencias para validación de JWT y gestión de usuarios
    private final JwtTokenUtil jwtUtil;
    private final CustomUserDetailsService uds;

    // Constructor con inyección de dependencias
    public SecurityConfig(JwtTokenUtil jwtUtil,
                          CustomUserDetailsService uds) {
        this.jwtUtil = jwtUtil;
        this.uds = uds;
    }

    /**
     * Configuración principal de seguridad HTTP para toda la aplicación.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Instancia tu filtro personalizado para JWT
        var jwtFilter = new JwtAuthenticationFilter(jwtUtil);

        http
            // Configuración de CORS y desactivación de protección CSRF
            .cors().and()
            .csrf(csrf -> csrf.disable())

            // Configuración de autorización sobre rutas específicas
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas (sin autenticación requerida)
                .requestMatchers("/auth/login", "/auth/register", "/error").permitAll()

                // Rutas protegidas (requieren roles específicos)
                .requestMatchers("/api/notifications/**")
                .hasAnyAuthority("ROLE_CLIENTE","ROLE_PROVEEDOR","ROLE_AMBOS")

                // Todas las demás rutas quedan denegadas por seguridad
                .anyRequest().denyAll()
            )

            // Define el servicio que carga detalles de usuario
            .userDetailsService(uds)

            // Inserta el filtro JWT antes del filtro de autenticación estándar de Spring Security
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configuración personalizada para permitir peticiones desde el frontend en localhost:5173 (por ejemplo, React).
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // Origen permitido (frontend local)
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        // Métodos HTTP permitidos
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        // Cabeceras permitidas en las solicitudes
        config.setAllowedHeaders(List.of("*"));
        // Permitir cookies y credenciales en peticiones
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica configuración CORS a todas las rutas
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * Bean que provee el AuthenticationManager, necesario para autenticar usuarios.
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authBuilder
            // Servicio que carga datos del usuario desde la base de datos
            .userDetailsService(uds)
            // Codificador de contraseña utilizado (en este caso, sin encriptación - NO seguro para producción)
            .passwordEncoder(passwordEncoder());

        return authBuilder.build();
    }

    /**
     * Bean para codificar contraseñas.
     * Actualmente usa un encoder inseguro (NoOp), solo válido para desarrollo/pruebas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // NO usar en producción: no protege contraseñas
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * Configuración para auditoría automática en entidades JPA.
     * Retorna el ID del usuario actualmente autenticado para registrar acciones automáticas.
     */
    @Bean
    public AuditorAware<Integer> auditorProvider() {
        return () -> {
            // Obtiene la autenticación actual del contexto de seguridad
            var auth = org.springframework.security.core.context.SecurityContextHolder
                    .getContext()
                    .getAuthentication();

            // Si existe un usuario autenticado, devuelve su ID
            if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof Integer) {
                return Optional.of((Integer) auth.getPrincipal());
            }
            // Si no hay usuario autenticado, devuelve vacío (no audita)
            return Optional.empty();
        };
    }
}
