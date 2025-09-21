package com.integracioncomunitaria.notificationapi.entity;

import com.integracioncomunitaria.notificationapi.entity.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad que representa un cliente en el sistema.
 * Hereda de BaseEntity para incluir campos de auditoría automáticos:
 * - idUserCreate, dateCreate
 * - idUserUpdate, dateUpdate
 */
@Entity
@Table(name = "customer")
@Getter
@Setter
public class Customer extends BaseEntity {

    /**
     * Clave primaria de la tabla `customer`.
     * Se genera automáticamente con estrategia IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_customer")
    private Integer idCustomer;

    /**
     * Nombre completo del cliente.
     * Campo no nulo de hasta 100 caracteres.
     */
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    /**
     * Fecha de nacimiento o año del cliente.
     * Se mapea a una fecha (LocalDate).
     */
    @Column(name = "date_year")
    private LocalDate dateYear;

    /**
     * Documento de identidad del cliente (DNI).
     * Hasta 20 caracteres.
     */
    @Column(name = "dni", length = 20)
    private String dni;

    /**
     * Email del cliente.
     * Hasta 50 caracteres.
     */
    @Column(name = "email", length = 50)
    private String email;

    /**
     * Teléfono de contacto del cliente.
     * Hasta 20 caracteres.
     */
    @Column(name = "phone", length = 20)
    private String phone;

    /**
     * Dirección postal.
     * Nota: en la base de datos está mal escrito 'adress', por lo que se mapea así.
     */
    @Column(name = "adress", length = 50)
    private String address;

    /**
     * Clave foránea al tipo de género.
     * Guarda el ID de la tabla `gender_type`.
     */
    //@Column(name = "id_gender_type")
    //private Integer genderType;

    /**
     * Clave foránea a la tabla de ciudades.
     * Guarda el ID de la ciudad asociada al cliente.
     */
    @Column(name = "id_city")
    private Integer cityId;

    /**
     * Latitud GPS del cliente.
     * Precisión total 10 y 8 decimales.
     */
    @Column(name = "gps_lat", precision = 10, scale = 8)
    private BigDecimal gpsLat;

    /**
     * Longitud GPS del cliente.
     * Precisión total 11 y 8 decimales.
     */
    @Column(name = "gps_lon", precision = 11, scale = 8)
    private BigDecimal gpsLon;

    /**
     * Relación Many-to-One con la entidad User.
     * Cada cliente está asociado a un usuario en la tabla `user`.
     * FetchType.LAZY retrasa la carga del usuario hasta que se accede.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    /**
     * Constructor por defecto sin argumentos, requerido por JPA.
     */
    public Customer() {}

    /**
     * Constructor de conveniencia que crea un proxy de Customer
     * con solo el identificador. Útil para referenciar la entidad
     * sin cargar todos sus campos.
     *
     * @param idCustomer ID del cliente.
     */
    public Customer(Integer idCustomer) {
        this.idCustomer = idCustomer;
    }

    // Puedes añadir más métodos o lógica de negocio aquí si es necesario.
}
