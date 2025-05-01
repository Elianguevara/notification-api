// src/main/java/com/integracioncomunitaria/notificationapi/entity/audit/BaseEntity.java
package com.integracioncomunitaria.notificationapi.entity.audit;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditEntityListener.class)
public abstract class BaseEntity {

    @Column(name = "id_user_create", updatable = false)
    private Integer idUserCreate;

    @Column(name = "id_user_update")
    private Integer idUserUpdate;

    @Column(name = "date_create", updatable = false)
    private LocalDateTime dateCreate;

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
