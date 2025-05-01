// src/main/java/com/integracioncomunitaria/notificationapi/entity/Customer.java
package com.integracioncomunitaria.notificationapi.entity;

import com.integracioncomunitaria.notificationapi.entity.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "customer")
@Getter @Setter
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_customer")
    private Integer idCustomer;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(name = "date_year")
    private LocalDate dateYear;

    @Column(length = 20)
    private String dni;

    @Column(length = 50)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 50)
    private String address;

    @Column(name = "id_gender_type")
    private Integer genderType;

    @Column(name = "id_city")
    private Integer cityId;

    @Column(name = "gps_lat", precision = 10, scale = 8)
    private BigDecimal gpsLat;

    @Column(name = "gps_lon", precision = 11, scale = 8)
    private BigDecimal gpsLon;

    @Column(name = "id_user")
    private Integer userId;
}
