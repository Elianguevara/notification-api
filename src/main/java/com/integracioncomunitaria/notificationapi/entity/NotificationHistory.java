package com.integracioncomunitaria.notificationapi.entity;

import com.integracioncomunitaria.notificationapi.entity.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidad que representa un evento en el historial de una notificación.
 * Hereda de BaseEntity para campos de auditoría:
 * - idUserCreate, dateCreate
 * - idUserUpdate, dateUpdate
 */
@Entity
@Table(name = "notification_history")
@Getter
@Setter
public class NotificationHistory extends BaseEntity {

    /**
     * Clave primaria de la tabla `notification_history`.
     * Generada automáticamente con estrategia IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notification_history")
    private Integer idNotificationHistory;

    /**
     * Tipo de evento registrado (por ejemplo: "CREATED", "VIEWED", "DELETED").
     */
    @Column(name = "event", length = 50, nullable = false)
    private String event;

    /**
     * Fecha y hora exacta en que ocurrió el evento.
     * Se usa precisión datetime(6) para incluir microsegundos.
     */
    @Column(name = "event_date", columnDefinition = "datetime(6)", nullable = false)
    private LocalDateTime eventDate;

    /**
     * Usuario que generó este evento.
     * Relación Many-to-One con la tabla `user`.
     * mandatory (nullable = false).
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    /**
     * Notificación asociada a este evento de historial.
     * Relación Many-to-One con la tabla `notification`.
     * mandatory (nullable = false).
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_notification", nullable = false)
    private Notification notification;

    /**
     * Constructor por defecto necesario para JPA.
     */
    public NotificationHistory() {
    }
}
