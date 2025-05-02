// src/main/java/com/integracioncomunitaria/notificationapi/dto/RegisterRequest.java
package com.integracioncomunitaria.notificationapi.dto;

import com.integracioncomunitaria.notificationapi.entity.RoleType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
public class RegisterRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String lastName;

    @NotBlank
    private String username;

    @Email @NotBlank
    private String email;

    @NotBlank @Size(min = 6)
    private String password;

    @NotNull
    private RoleType roleType;

    // Si es cliente o ambos
    private CustomerDTO customer;

    // Si es proveedor o ambos
    private ProviderDTO provider;

    @Getter @Setter
    public static class CustomerDTO {
        @NotBlank private String name;
        @PastOrPresent private LocalDate dateYear;
        private String dni;
        private String phone;
        private String address;
        private BigDecimal gpsLat;
        private BigDecimal gpsLon;
    }

    @Getter @Setter
    public static class ProviderDTO {
        @NotBlank private String name;
        private String address;
        private Float gpsLat;
        private Float gpsLong;
        private Integer typeProviderId;
        private Integer gradeProviderId;
        private Integer professionId;
        private Long offerId;
        private Integer categoryId;
    }
}