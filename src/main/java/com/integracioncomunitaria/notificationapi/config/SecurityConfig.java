// src/main/java/com/integracioncomunitaria/notificationapi/config/SecurityConfig.java
package com.integracioncomunitaria.notificationapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.integracioncomunitaria.notificationapi.service.CustomUserDetailsService;

import java.util.Optional;

@Configuration
@EnableWebSecurity
// Habilitamos el auditorAwareRef para Spring Data JPA Auditing
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class SecurityConfig {

    private final JwtTokenUtil jwtUtil;
    private final CustomUserDetailsService uds;

    public SecurityConfig(JwtTokenUtil jwtUtil,
                          CustomUserDetailsService uds) {
        this.jwtUtil = jwtUtil;
        this.uds = uds;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        var jwtFilter = new JwtAuthenticationFilter(jwtUtil);

        http
          .csrf(csrf -> csrf.disable())
          .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/login", "/auth/register", "/error").permitAll()
                .requestMatchers("/api/notifications/**")
                .hasAnyAuthority("ROLE_CLIENTE","ROLE_PROVEEDOR","ROLE_AMBOS")
                .anyRequest().denyAll()
          )
          .userDetailsService(uds)
          .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder =
            http.getSharedObject(AuthenticationManagerBuilder.class);

        authBuilder
            .userDetailsService(uds)
            .passwordEncoder(passwordEncoder());

        return authBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Sólo para desarrollo: texto plano
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * AuditorAware que extrae el userId del SecurityContextHolder para Spring Data JPA.
     */
    @Bean
    public AuditorAware<Integer> auditorProvider() {
        return () -> {
            Authentication auth = SecurityContextHolder
                                  .getContext()
                                  .getAuthentication();
            if (auth != null
             && auth.isAuthenticated()
             && auth.getPrincipal() instanceof Integer) {
                return Optional.of((Integer) auth.getPrincipal());
            }
            return Optional.empty();
        };
    }
}
