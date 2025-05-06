package com.integracioncomunitaria.notificationapi.entity.audit;

import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

/**
 * Clase base para entidades que necesitan campos de auditoría automáticos:
 * - Quién creó el registro
 * - Quién lo modificó por última vez
 * - Cuándo se creó
 * - Cuándo se actualizó
 *
 * Se marca como @MappedSuperclass para que sus campos se hereden en las entidades hijas.
 * El listener de Spring Data JPA (@EntityListeners) se encarga de rellenar los campos.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)  // Activa el auditor automático
public abstract class BaseEntity {

    /**
     * ID del usuario que creó el registro.
     * Se rellena automáticamente justo antes de persistir la entidad.
     * Nunca se actualiza tras la creación.
     */
    @CreatedBy
    @Column(name = "id_user_create", updatable = false)
    private Integer idUserCreate;

    /**
     * ID del usuario que modificó el registro por última vez.
     * Se actualiza automáticamente en cada cambio de la entidad.
     */
    @LastModifiedBy
    @Column(name = "id_user_update")
    private Integer idUserUpdate;

    /**
     * Fecha y hora en que se creó el registro.
     * Se fija al momento de la primera inserción y no cambia.
     */
    @CreatedDate
    @Column(name = "date_create", updatable = false)
    private LocalDateTime dateCreate;

    /**
     * Fecha y hora de la última modificación del registro.
     * Se actualiza automáticamente cada vez que la entidad se guarda.
     */
    @LastModifiedDate
    @Column(name = "date_update")
    private LocalDateTime dateUpdate;

    // ——— Getters y Setters ———

    public Integer getIdUserCreate() {
        return idUserCreate;
    }

    public void setIdUserCreate(Integer idUserCreate) {
        this.idUserCreate = idUserCreate;
    }

    public Integer getIdUserUpdate() {
        return idUserUpdate;
    }

    public void setIdUserUpdate(Integer idUserUpdate) {
        this.idUserUpdate = idUserUpdate;
    }

    public LocalDateTime getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDateTime dateCreate) {
        this.dateCreate = dateCreate;
    }

    public LocalDateTime getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(LocalDateTime dateUpdate) {
        this.dateUpdate = dateUpdate;
    }
}
