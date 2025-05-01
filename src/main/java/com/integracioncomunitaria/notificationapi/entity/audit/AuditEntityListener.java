// src/main/java/com/integracioncomunitaria/notificationapi/entity/audit/AuditEntityListener.java
package com.integracioncomunitaria.notificationapi.entity.audit;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;

public class AuditEntityListener {

    @PrePersist
    public void prePersist(BaseEntity entity) {
        LocalDateTime now = LocalDateTime.now();
        entity.setDateCreate(now);
        entity.setDateUpdate(now);
        // aquí podrías obtener el usuario actual si lo tienes en el contexto de seguridad:
        // entity.setIdUserCreate(currentUserId);
        // entity.setIdUserUpdate(currentUserId);
    }

    @PreUpdate
    public void preUpdate(BaseEntity entity) {
        entity.setDateUpdate(LocalDateTime.now());
        //entity.setIdUserUpdate(currentUserId);
    }
}
