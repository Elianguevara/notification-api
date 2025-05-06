package com.integracioncomunitaria.notificationapi.service;

// Importaciones de Java y Spring necesarias
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import com.integracioncomunitaria.notificationapi.dto.NotificationCreateDTO;
import com.integracioncomunitaria.notificationapi.dto.NotificationDTO;
import com.integracioncomunitaria.notificationapi.dto.NotificationHistoryDTO;
import com.integracioncomunitaria.notificationapi.entity.*;
import com.integracioncomunitaria.notificationapi.repository.NotificationHistoryRepository;
import com.integracioncomunitaria.notificationapi.repository.NotificationRepository;

/**
 * Implementación del servicio que gestiona la lógica de negocio de las notificaciones.
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repo;
    private final NotificationHistoryRepository historyRepo;

    // Constructor con inyección de dependencias
    public NotificationServiceImpl(NotificationRepository repo,
                                   NotificationHistoryRepository historyRepo) {
        this.repo = repo;
        this.historyRepo = historyRepo;
    }

    /**
     * Crea una nueva notificación con la información recibida.
     */
    @Override
    public NotificationDTO create(NotificationCreateDTO dto, Integer currentUserId) {
        Notification n = new Notification();

        // Asigna proveedor si se indica
        if (dto.getProviderId() != null) {
            n.setProvider(new Provider(dto.getProviderId()));
        }

        // Asigna cliente si se indica
        if (dto.getCustomerId() != null) {
            n.setCustomer(new Customer(dto.getCustomerId()));
        }

        // Información básica
        n.setMessage(dto.getMessage());
        n.setViewed(false);
        n.setDeleted(false);

        // Auditoría
        n.setIdUserCreate(currentUserId);
        n.setDateCreate(LocalDateTime.now());

        // Guarda la notificación en la base de datos
        n = repo.save(n);

        // Guarda el historial con evento "CREATED"
        saveHistory(n, "CREATED");

        // Devuelve el DTO mapeado
        return map(n);
    }

    /**
     * Lista las notificaciones filtradas por cliente/proveedor, estado (vistas o no) y fechas.
     */
    @Override
    public List<NotificationDTO> list(Integer customerId,
                                      Integer providerId,
                                      Boolean viewed,
                                      LocalDateTime from,
                                      LocalDateTime to) {

        // Fechas por defecto si no se indican
        LocalDateTime f = from != null ? from : LocalDateTime.of(1970,1,1,0,0);
        LocalDateTime t = to   != null ? to   : LocalDateTime.now();

        // Obtiene y mapea notificaciones filtradas
        return repo.findFiltered(customerId, providerId, viewed, f, t)
                   .stream()
                   .map(this::map)
                   .toList();
    }

    /**
     * Obtiene una notificación específica por ID.
     */
    @Override
    public NotificationDTO getById(Integer id) {
        Notification n = repo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Notificación no existe"));
        return map(n);
    }

    /**
     * Marca una notificación como eliminada (soft delete).
     */
    @Override
    public void delete(Integer id) {
        Notification n = repo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Notificación no existe"));

        n.setDeleted(true);
        n.setIdUserUpdate(getCurrentUserId());
        n.setDateUpdate(LocalDateTime.now());

        repo.save(n);

        // Guarda el historial con evento "DELETED"
        saveHistory(n, "DELETED");
    }

    /**
     * Marca una notificación como vista por el usuario actual.
     */
    @Override
    public NotificationDTO markAsViewed(Integer id, Integer currentUserId) {
        Notification n = repo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Notificación no existe"));

        n.setViewed(true);
        n.setIdUserUpdate(currentUserId);
        n.setDateUpdate(LocalDateTime.now());

        Notification updated = repo.save(n);

        // Guarda el historial con evento "VIEWED"
        saveHistory(updated, "VIEWED");

        return map(updated);
    }

    /**
     * Obtiene el historial completo de una notificación específica.
     */
    @Override
    public List<NotificationHistoryDTO> history(Integer notificationId) {
        return historyRepo.findByNotification_IdNotification(notificationId)
                          .stream()
                          .map(h -> new NotificationHistoryDTO(
                              h.getIdNotificationHistory(),
                              h.getNotification().getIdNotification(),
                              h.getEvent(),
                              h.getEventDate(),
                              h.getUser().getIdUser(),
                              h.getDateCreate()
                          ))
                          .toList();
    }

    // Métodos auxiliares privados

    /**
     * Guarda un evento en el historial de la notificación.
     * La auditoría (id_user_create y date_create) se registra automáticamente.
     */
    private void saveHistory(Notification notification, String event) {
        NotificationHistory h = new NotificationHistory();
        h.setNotification(notification);
        h.setEvent(event);
        h.setEventDate(LocalDateTime.now());
        h.setUser(new User(getCurrentUserId())); // Registra quién realiza la acción
        historyRepo.save(h);
    }

    /**
     * Convierte una entidad Notification en un DTO NotificationDTO.
     */
    private NotificationDTO map(Notification n) {
        return new NotificationDTO(
            n.getIdNotification(),
            n.getProvider() != null ? n.getProvider().getIdProvider() : null,
            n.getCustomer() != null ? n.getCustomer().getIdCustomer() : null,
            n.getMessage(),
            n.isViewed(),
            n.getDateCreate(),
            Boolean.TRUE.equals(n.getDeleted()) // Gestiona el flag de borrado lógico
        );
    }

    /**
     * Obtiene el ID del usuario autenticado actualmente desde el contexto de seguridad.
     */
    private Integer getCurrentUserId() {
        return (Integer) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
