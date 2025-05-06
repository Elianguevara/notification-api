package com.integracioncomunitaria.notificationapi.entity;

import com.integracioncomunitaria.notificationapi.entity.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad que representa un proveedor en el sistema.
 * Hereda de BaseEntity para incluir automáticamente los campos de auditoría:
 * - idUserCreate, dateCreate
 * - idUserUpdate, dateUpdate
 */
@Entity
@Table(name = "provider")
@Getter
@Setter
public class Provider extends BaseEntity {

    /**
     * Clave primaria de la tabla `provider`.
     * Generada automáticamente con estrategia IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_provider")
    private Integer idProvider;

    /**
     * Nombre del proveedor.
     * No puede ser nulo y admite hasta 255 caracteres.
     */
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    /**
     * Dirección postal del proveedor.
     * Hasta 255 caracteres.
     */
    @Column(name = "address", length = 255)
    private String address;

    /**
     * Latitud GPS del proveedor.
     * Valor en punto flotante.
     */
    @Column(name = "gps_lat")
    private Float gpsLat;

    /**
     * Longitud GPS del proveedor.
     * En la base de datos se denomina `gps_long`.
     */
    @Column(name = "gps_long")
    private Float gpsLong;

    /**
     * Clave foránea al tipo de proveedor.
     * Corresponde a `id_type_provider`.
     */
    @Column(name = "id_type_provider")
    private Integer typeProviderId;

    /**
     * Clave foránea a la categoría del proveedor.
     * Corresponde a `id_category`.
     */
    @Column(name = "id_category")
    private Integer categoryId;

    /**
     * Relación Many-to-One con la entidad Category.
     * Mappea la categoría a la que pertenece este proveedor.
     * - insertable = false, updatable = false: la clave foránea se gestiona desde categoryId.
     * - FetchType.LAZY retrasa la carga de la categoría hasta su uso.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category", insertable = false, updatable = false)
    private Category category;

    /**
     * Clave foránea al grado del proveedor.
     * Corresponde a `id_grade_provider`.
     */
    @Column(name = "id_grade_provider")
    private Integer gradeProviderId;

    /**
     * Clave foránea a la profesión del proveedor.
     * Corresponde a `id_profession`.
     */
    @Column(name = "id_profession")
    private Integer professionId;

    /**
     * Clave foránea a la oferta asociada al proveedor.
     * Corresponde a `id_offer`.
     */
    @Column(name = "id_offer")
    private Long offerId;

    /**
     * Relación Many-to-One con la entidad User.
     * Cada proveedor está asociado a un usuario que lo creó o lo gestiona.
     * - nullable = false: siempre debe existir un usuario vinculado.
     * - FetchType.LAZY retrasa la carga del usuario hasta su acceso.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public Provider() {}

    /**
     * Constructor de conveniencia para instanciar un proveedor proxy
     * con solo su ID, útil para referencias sin cargar toda la entidad.
     *
     * @param idProvider ID del proveedor.
     */
    public Provider(Integer idProvider) {
        this.idProvider = idProvider;
    }
}
