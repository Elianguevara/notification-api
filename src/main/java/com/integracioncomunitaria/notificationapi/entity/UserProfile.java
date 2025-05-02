// src/main/java/com/integracioncomunitaria/notificationapi/entity/UserProfile.java
package com.integracioncomunitaria.notificationapi.entity;

import com.integracioncomunitaria.notificationapi.entity.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;



@Entity
@Table(name = "user_profile")
@Getter @Setter
public class UserProfile extends BaseEntity {

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    public UserProfile() {
    }
}
