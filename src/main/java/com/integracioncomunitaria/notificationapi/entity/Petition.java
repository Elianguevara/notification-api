// src/main/java/com/integracioncomunitaria/notificationapi/entity/Petition.java
package com.integracioncomunitaria.notificationapi.entity;

import com.integracioncomunitaria.notificationapi.entity.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;


@Entity
@Table(name = "petition")
@Getter @Setter
public class Petition extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_petition")
    private Integer idPetition;

    @Column(name = "id_type_petition")
    private Integer typePetitionId;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "date_since")
    private LocalDate dateSince;

    @Column(name = "date_until")
    private LocalDate dateUntil;

    @Column(name = "id_customer")
    private Integer customerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_customer", insertable = false, updatable = false)
    private Customer customer;
}
