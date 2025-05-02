package com.integracioncomunitaria.notificationapi.controller;

import com.integracioncomunitaria.notificationapi.config.JwtTokenUtil;
import com.integracioncomunitaria.notificationapi.dto.*;
import com.integracioncomunitaria.notificationapi.entity.User;
import com.integracioncomunitaria.notificationapi.service.RegistrationService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtUtil;
    private final RegistrationService regService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenUtil jwtUtil,
                          RegistrationService regService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.regService = regService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );
        User user = (User) auth.getPrincipal();
        String role = user.getAuthorities().stream()
                          .findFirst()
                          .map(a -> a.getAuthority())
                          .orElse("ROLE_CLIENTE");
        String token = jwtUtil.generateToken(user.getIdUser(), role);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest req) {
        regService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
