// src/main/java/com/integracioncomunitaria/notificationapi/entity/Provider.java
package com.integracioncomunitaria.notificationapi.entity;

import com.integracioncomunitaria.notificationapi.entity.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Provider extends BaseEntity {

    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_provider")
    private Integer idProvider;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(length = 255)
    private String address;

    private Float gpsLat;
    private Float gpsLong;

    @Column(name = "id_type_provider")
    private Integer typeProviderId;

    
    @Column(name = "id_grade_provider")
    private Integer gradeProviderId;

    @Column(name = "id_profession")
    private Integer professionId;

    @Column(name = "id_offer")
    private Long offerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category", insertable = false, updatable = false)
    private Category category;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    public Provider() {}

    public Provider(Integer idProvider) {
        this.idProvider = idProvider;
    }
}
