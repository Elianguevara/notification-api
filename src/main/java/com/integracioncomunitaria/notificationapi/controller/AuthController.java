package com.integracioncomunitaria.notificationapi.controller;

import com.integracioncomunitaria.notificationapi.config.JwtTokenUtil;
import com.integracioncomunitaria.notificationapi.dto.*;
import com.integracioncomunitaria.notificationapi.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        // 1) Spring intenta autenticar email+clave (usa tu CustomUserDetailsService)
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                req.getEmail(),
                req.getPassword()
            )
        );

        // 2) Extraemos el User con sus authorities
        User user = (User) auth.getPrincipal();
        String role = user.getAuthorities()
                          .stream()
                          .findFirst()
                          .map(a -> a.getAuthority())
                          .orElse("ROLE_CLIENTE");

        // 3) Generamos el JWT con idUsuario y rol
        String token = jwtUtil.generateToken(user.getIdUser(), role);

        // 4) Devolvemos el token al cliente
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
