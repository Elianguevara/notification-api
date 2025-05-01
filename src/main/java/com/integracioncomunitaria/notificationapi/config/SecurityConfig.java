package com.integracioncomunitaria.notificationapi.config;

import com.integracioncomunitaria.notificationapi.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.crypto.password.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
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
             .requestMatchers("/auth/**").permitAll()
             .requestMatchers("/api/notifications/**")
                .hasAnyAuthority("ROLE_CUSTOMER","ROLE_PROVIDER","ROLE_BOTH")
             .anyRequest().denyAll()
          )
          // Usamos nuestro UserDetailsService
          .userDetailsService(uds)
          // Insertamos el filtro JWT
          .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Registramos un AuthenticationManager que use customUserDetailsService
     * y el encoder que definimos abajo (NoOp, para texto plano).
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder =
            http.getSharedObject(AuthenticationManagerBuilder.class);

        authBuilder
            .userDetailsService(uds)
            .passwordEncoder(passwordEncoder());

        return authBuilder.build();
    }

    /**
     * NO OPCODE EN PRODUCCIÓN.
     * Permite comparar directamente texto plano contra texto plano.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
