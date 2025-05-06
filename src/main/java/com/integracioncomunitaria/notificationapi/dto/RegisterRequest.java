// src/main/java/com/integracioncomunitaria/notificationapi/dto/RegisterRequest.java
package com.integracioncomunitaria.notificationapi.dto;

import com.integracioncomunitaria.notificationapi.entity.RoleType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class RegisterRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String lastName;

    @Email @NotBlank
    private String email;

    @NotBlank @Size(min = 6)
    private String password;

    @NotNull
    private RoleType roleType;  
}
