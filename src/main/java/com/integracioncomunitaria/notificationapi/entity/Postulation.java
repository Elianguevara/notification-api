// src/main/java/com/integracioncomunitaria/notificationapi/entity/Postulation.java
package com.integracioncomunitaria.notificationapi.entity;

import com.integracioncomunitaria.notificationapi.entity.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "postulation")
@Getter @Setter
public class Postulation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpostulation")
    private Integer idPostulation;


    @Column(length = 45)
    private String winner;

    @Column(length = 45)
    private String proposal;

    @Column
    private Integer cost;

    @Column(name = "id_state")
    private Integer stateId;

    @Column(length = 45)
    private String current;

    @Column(length = 55)
    private String state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_petition", insertable = false, updatable = false)
    private Petition petition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_provider", insertable = false, updatable = false)
    private Provider provider;

    public Postulation() {
    }
}
