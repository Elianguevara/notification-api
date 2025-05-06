package com.integracioncomunitaria.notificationapi.entity;

import com.integracioncomunitaria.notificationapi.entity.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Entidad que representa una petición (petition) en el sistema.
 * Hereda de BaseEntity para incluir automáticamente los campos de auditoría:
 * - idUserCreate, dateCreate
 * - idUserUpdate, dateUpdate
 */
@Entity
@Table(name = "petition")
@Getter
@Setter
public class Petition extends BaseEntity {

    /**
     * Clave primaria de la tabla `petition`.
     * Generada automáticamente con estrategia IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_petition")
    private Integer idPetition;

    /**
     * ID del tipo de petición.
     * Corresponde a la columna `id_type_petition` en la base de datos.
     */
    @Column(name = "id_type_petition")
    private Integer typePetitionId;

    /**
     * Descripción detallada de la petición.
     * Mapeada como tipo TEXT para permitir textos largos.
     */
    @Column(columnDefinition = "text")
    private String description;

    /**
     * Fecha de inicio de la petición.
     * Corresponde a la columna `date_since` (LocalDate).
     */
    @Column(name = "date_since")
    private LocalDate dateSince;

    /**
     * Fecha de fin de la petición.
     * Corresponde a la columna `date_until` (LocalDate).
     */
    @Column(name = "date_until")
    private LocalDate dateUntil;

    /**
     * Relación Many-to-One con la entidad Customer.
     * Cada petición está asociada a un cliente.
     * - insertable = false, updatable = false: la clave foránea
     *   se maneja directamente desde el campo typePetitionId u otro campo,
     *   evitando insertar/actualizar aquí.
     * - FetchType.LAZY retrasa la carga del cliente hasta su uso.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_customer", insertable = false, updatable = false)
    private Customer customer;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public Petition() {
    }
}
