// src/main/java/com/integracioncomunitaria/notificationapi/controller/RegistrationController.java
package com.integracioncomunitaria.notificationapi.controller;

import com.integracioncomunitaria.notificationapi.dto.RegisterRequest;
import com.integracioncomunitaria.notificationapi.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class RegistrationController {

    private final RegistrationService regService;

    public RegistrationController(RegistrationService regService) {
        this.regService = regService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest req) {
        regService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
