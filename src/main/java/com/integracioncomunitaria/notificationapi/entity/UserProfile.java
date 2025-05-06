package com.integracioncomunitaria.notificationapi.entity;

import com.integracioncomunitaria.notificationapi.entity.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad que almacena información adicional del usuario,
 * como rol y privilegios administrativos.
 * Hereda de BaseEntity para incluir auditoría automática
 * (quién crea/modifica y cuándo).
 */
@Entity
@Table(name = "user_profile")
@Getter
@Setter
public class UserProfile extends BaseEntity {

    /**
     * Clave primaria de la tabla `user_profile`.
     * Generada automáticamente con estrategia IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profile")
    private Integer idProfile;

    /**
     * Email asociado al perfil del usuario.
     * Útil para contactos o notificaciones.
     */
    @Column(name = "email", length = 150, nullable = false)
    private String email;

    /**
     * Indica si el usuario tiene permisos de administrador.
     * true = usuario administrador; false = usuario estándar.
     */
    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin;

    /**
     * Tipo de rol del usuario, definido por la enumeración RoleType.
     * Valores posibles: 'cliente', 'proveedor', 'ambos'.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", columnDefinition = "enum('cliente','proveedor','ambos')", nullable = false)
    private RoleType roleType;

    /**
     * Relación uno a uno con la entidad User.
     * Define a qué cuenta de usuario pertenece este perfil.
     * - fetch = LAZY: carga diferida hasta acceder al usuario.
     * - nullable = false: siempre debe existir un usuario asociado.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public UserProfile() {
    }
}