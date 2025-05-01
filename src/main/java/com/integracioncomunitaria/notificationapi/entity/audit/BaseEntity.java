// src/main/java/com/integracioncomunitaria/notificationapi/entity/audit/BaseEntity.java
package com.integracioncomunitaria.notificationapi.entity.audit;

import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

@MappedSuperclass
// Usamos el listener de Spring Data JPA en lugar del propio
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedBy
    @Column(name = "id_user_create", updatable = false)
    private Integer idUserCreate;

    @LastModifiedBy
    @Column(name = "id_user_update")
    private Integer idUserUpdate;

    @CreatedDate
    @Column(name = "date_create", updatable = false)
    private LocalDateTime dateCreate;

    @LastModifiedDate
    @Column(name = "date_update")
    private LocalDateTime dateUpdate;

    // getters/setters
    public Integer getIdUserCreate() { return idUserCreate; }
    public void setIdUserCreate(Integer idUserCreate) { this.idUserCreate = idUserCreate; }
    public Integer getIdUserUpdate() { return idUserUpdate; }
    public void setIdUserUpdate(Integer idUserUpdate) { this.idUserUpdate = idUserUpdate; }
    public LocalDateTime getDateCreate() { return dateCreate; }
    public void setDateCreate(LocalDateTime dateCreate) { this.dateCreate = dateCreate; }
    public LocalDateTime getDateUpdate() { return dateUpdate; }
    public void setDateUpdate(LocalDateTime dateUpdate) { this.dateUpdate = dateUpdate; }
}
