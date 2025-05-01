package com.integracioncomunitaria.notificationapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AuthRequest {
    private String email;
    private String password;
    // getters y setters (o @Data de Lombok)
}
