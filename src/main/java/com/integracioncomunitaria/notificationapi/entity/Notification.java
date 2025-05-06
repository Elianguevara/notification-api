package com.integracioncomunitaria.notificationapi.entity;

import com.integracioncomunitaria.notificationapi.entity.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad que representa una notificación en el sistema.
 * Hereda de BaseEntity para incluir campos de auditoría automáticos:
 * - idUserCreate, dateCreate
 * - idUserUpdate, dateUpdate
 */
@Entity
@Table(name = "notification")
@Getter
@Setter
public class Notification extends BaseEntity {

    /**
     * Clave primaria de la tabla `notification`.
     * Se genera automáticamente con estrategia IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notification")
    private Integer idNotification;

    /**
     * Indicador de borrado lógico (soft delete).
     * Se almacena como TINYINT(1) en la base de datos.
     * Por defecto es false (no borrada).
     */
    @Column(name = "deleted", columnDefinition = "TINYINT(1)", nullable = false)
    private Boolean deleted = false;

    /**
     * Contenido del mensaje de la notificación.
     * Mapeado como tipo TEXT para permitir mensajes largos.
     */
    @Column(name = "message", columnDefinition = "text")
    private String message;

    /**
     * Indicador de si la notificación ha sido vista.
     * Se almacena como TINYINT(1) en la base de datos.
     */
    @Column(name = "viewed", columnDefinition = "TINYINT(1)")
    private Boolean viewed;

    /**
     * Relación Many-to-One con la entidad Provider.
     * Cada notificación puede estar asociada a un proveedor.
     * FetchType.LAZY retrasa la carga del proveedor hasta que se accede.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_provider")
    private Provider provider;

    /**
     * Relación Many-to-One con la entidad Customer.
     * Cada notificación puede estar asociada a un cliente.
     * FetchType.LAZY retrasa la carga del cliente hasta que se accede.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_customer")
    private Customer customer;

    /**
     * Getter personalizado que garantiza un valor booleano no nulo.
     * @return true si viewed es Boolean.TRUE, false en cualquier otro caso.
     */
    public boolean isViewed() {
        return Boolean.TRUE.equals(this.viewed);
    }

    /**
     * Constructor de conveniencia que crea una notificación proxy
     * con solo su ID, útil para asignar relaciones sin cargar la entidad completa.
     *
     * @param idNotification ID de la notificación.
     */
    public Notification(Integer idNotification) {
        this.idNotification = idNotification;
    }

    /**
     * Constructor por defecto requerido por JPA.
     */
    public Notification() {
    }
}
