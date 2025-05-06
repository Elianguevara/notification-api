package com.integracioncomunitaria.notificationapi.entity;

import com.integracioncomunitaria.notificationapi.entity.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Entidad que representa un usuario en el sistema.
 * Hereda de BaseEntity para auditoría (creación y modificación automática).
 * Implementa UserDetails para integrarse con Spring Security.
 */
@Entity
@Table(name = "`user`")
@Getter
@Setter
public class User extends BaseEntity implements UserDetails {

    /**
     * Clave primaria autogenerada para el usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Integer idUser;

    /**
     * Nombre del usuario.
     */
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    /**
     * Apellido del usuario.
     */
    @Column(name = "last_name", length = 50)
    private String lastName;

    /**
     * Email único, usado también para recuperar al usuario.
     */
    @Column(name = "email", length = 100, unique = true, nullable = false)
    private String email;

    /**
     * Identificador de grupo (si aplica).
     */
    @Column(name="`group`")
    private Integer groupId;

    /**
     * Nombre de usuario único para login.
     */
    @Column(name = "username", length = 55, unique = true, nullable = false)
    private String username;

    /**
     * Contraseña cifrada del usuario.
     */
    @Column(name = "password", length = 255, nullable = false)
    private String password;

    /**
     * Token JWT activo (opcional) guardado para validaciones.
     */
    @Column(name = "token", length = 255)
    private String token;

    /**
     * Fecha de expiración del token o última actualización.
     */
    @Column(name = "date_token")
    private LocalDateTime dateToken;

    /**
     * Indicador de si la cuenta está habilitada.
     */
    @Column(name = "enabled", columnDefinition = "TINYINT(1)")
    private Boolean enabled;

    /**
     * Relación uno a uno con UserProfile.
     * El perfil contiene rol y datos adicionales del usuario.
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserProfile profile;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public User() {
    }

    /**
     * Constructor de conveniencia con ID, útil para auditoría y relaciones.
     */
    public User(Integer idUser) {
        this.idUser = idUser;
    }

    /**
     * Define las autoridades (roles) del usuario basado en su UserProfile.
     * @return Lista con una SimpleGrantedAuthority del rol.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.profile == null || this.profile.getRoleType() == null) {
            throw new IllegalStateException(
              "El usuario " + this.getEmail() + " no tiene rol asignado."
            );
        }
        // Formatea el rol a "ROLE_{ROL}" para Spring Security
        String roleName = "ROLE_" + profile.getRoleType().name().toUpperCase();
        return List.of(new SimpleGrantedAuthority(roleName));
    }

    /* Métodos de UserDetails para controlar estado de la cuenta */

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        // Se podría agregar lógica de expiración aquí
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Usa campo enabled como bloqueo de cuenta
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Se podría verificar fechaToken para expiración de credenciales
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
