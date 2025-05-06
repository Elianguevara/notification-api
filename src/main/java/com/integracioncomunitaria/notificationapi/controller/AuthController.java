package com.integracioncomunitaria.notificationapi.controller;

// Importaciones necesarias para el controlador
import com.integracioncomunitaria.notificationapi.config.JwtTokenUtil;
import com.integracioncomunitaria.notificationapi.dto.AuthRequest;
import com.integracioncomunitaria.notificationapi.dto.AuthResponse;
import com.integracioncomunitaria.notificationapi.dto.RegisterRequest;
import com.integracioncomunitaria.notificationapi.entity.User;
import com.integracioncomunitaria.notificationapi.service.RegistrationService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

// Controlador REST que maneja autenticación y registro de usuarios
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtUtil;
    private final RegistrationService regService;

    // Constructor con inyección de dependencias de servicios necesarios
    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenUtil jwtUtil,
                          RegistrationService regService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.regService = regService;
    }

    /**
     * Endpoint para inicio de sesión del usuario.
     * Valida las credenciales y retorna un JWT en caso de éxito.
     *
     * Ruta: POST /auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {

        // Usa AuthenticationManager para autenticar usuario y contraseña
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );

        // Recupera el usuario autenticado
        User user = (User) auth.getPrincipal();

        // Obtiene el primer rol asociado al usuario
        String role = user.getAuthorities().stream()
                          .findFirst()
                          .map(a -> a.getAuthority())
                          .orElse("ROLE_CLIENTE"); // Rol por defecto si no hay ninguno explícito

        // Genera un JWT usando ID y rol del usuario autenticado
        String token = jwtUtil.generateToken(user.getIdUser(), role);

        // Retorna el token JWT encapsulado en un objeto AuthResponse
        return ResponseEntity.ok(new AuthResponse(token));
    }

    /**
     * Endpoint para registro de nuevos usuarios.
     * Valida la información recibida y registra al usuario en el sistema.
     *
     * Ruta: POST /auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest req) {

        // Utiliza el servicio RegistrationService para registrar un nuevo usuario
        regService.register(req);

        // Retorna código HTTP 201 (CREATED) para indicar registro exitoso
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
