package com.integracioncomunitaria.notificationapi.entity;

import com.integracioncomunitaria.notificationapi.entity.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad que representa una postulación a una petición en el sistema.
 * Hereda de BaseEntity para incluir campos de auditoría automáticos:
 * - idUserCreate, dateCreate
 * - idUserUpdate, dateUpdate
 */
@Entity
@Table(name = "postulation")
@Getter
@Setter
public class Postulation extends BaseEntity {

    /**
     * Clave primaria de la tabla `postulation`.
     * Generada automáticamente con estrategia IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpostulation")
    private Integer idPostulation;

    /**
     * Nombre del proveedor ganador de la postulación.
     * Hasta 45 caracteres.
     */
    @Column(length = 45)
    private String winner;

    /**
     * Descripción de la propuesta realizada en la postulación.
     * Hasta 45 caracteres.
     */
    @Column(length = 45)
    private String proposal;

    /**
     * Costo asociado a la propuesta.
     */
    @Column
    private Integer cost;

    /**
     * Clave foránea al estado interno de la postulación.
     * Corresponde a la columna `id_state`.
     */
    @Column(name = "id_state")
    private Integer stateId;

    /**
     * Descripción breve del estado actual de la postulación.
     * Hasta 45 caracteres.
     */
    @Column(length = 45)
    private String current;

    /**
     * Nombre completo del estado de la postulación.
     * Hasta 55 caracteres.
     */
    @Column(length = 55)
    private String state;

    /**
     * Relación Many-to-One con la entidad Petition.
     * La columna `id_petition` se maneja directamente en otra entidad,
     * por eso esta relación no es insertable ni actualizable aquí.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_petition", insertable = false, updatable = false)
    private Petition petition;

    /**
     * Relación Many-to-One con la entidad Provider.
     * La columna `id_provider` se maneja directamente en otra entidad,
     * por eso esta relación no es insertable ni actualizable aquí.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_provider", insertable = false, updatable = false)
    private Provider provider;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public Postulation() {
    }
}
