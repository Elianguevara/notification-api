package com.integracioncomunitaria.notificationapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_profile")
@Getter @Setter
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profile")
    private Integer idProfile;

    @Column(name = "email", length = 150, nullable = false)
    private String email;

    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", columnDefinition = "enum('cliente','proveedor','ambos')", nullable = false)
    private RoleType roleType;

    /**
     * Este es el único FK real: user_profile.user_id → user.id_user
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Auditoría
    @Column(name = "id_user_create")
    private Integer idUserCreate;

    @Column(name = "id_user_update")
    private Integer idUserUpdate;

    @Column(name = "date_create", updatable = false)
    private LocalDateTime dateCreate;

    @Column(name = "date_update")
    private LocalDateTime dateUpdate;
}
