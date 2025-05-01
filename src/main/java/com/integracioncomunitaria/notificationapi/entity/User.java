package com.integracioncomunitaria.notificationapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "`user`")
@Getter @Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Integer idUser;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "email", length = 100, unique = true, nullable = false)
    private String email;

    @Column(name = "username", length = 55, unique = true, nullable = false)
    private String username;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "token", length = 255)
    private String token;

    @Column(name = "date_token")
    private LocalDateTime dateToken;

    @Column(name = "enabled", columnDefinition = "TINYINT(1)")
    private Boolean enabled;

    // Auditoría
    @Column(name = "id_user_create")
    private Integer idUserCreate;

    @Column(name = "id_user_update")
    private Integer idUserUpdate;

    @Column(name = "date_create", updatable = false)
    private LocalDateTime dateCreate;

    @Column(name = "date_update")
    private LocalDateTime dateUpdate;

    /**
     * Relación uno a uno: User ← user_profile.user_id
     * (el lado propietario es UserProfile)
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserProfile profile;

    // --- Métodos de UserDetails ---
    @Override
public Collection<? extends GrantedAuthority> getAuthorities() {
    if (this.profile == null || this.profile.getRoleType() == null) {
        // Mejor lanzar IllegalStateException que devolver null
        throw new IllegalStateException(
          "El usuario " + this.getEmail() + " no tiene rol asignado."
        );
    }
    String roleName = "ROLE_" + profile.getRoleType().name().toUpperCase();
    return List.of(new SimpleGrantedAuthority(roleName));
}

    
    
    @Override public String getUsername()        { return username; }
    @Override public String getPassword()        { return password; }
    @Override public boolean isAccountNonExpired()   { return true; }
    @Override public boolean isAccountNonLocked()    { return enabled; }
    @Override public boolean isCredentialsNonExpired(){ return true; }
    @Override public boolean isEnabled()             { return enabled; }
}
