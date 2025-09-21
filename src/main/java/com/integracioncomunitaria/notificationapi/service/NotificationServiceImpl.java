// src/main/java/com/integracioncomunitaria/notificationapi/service/NotificationServiceImpl.java
package com.integracioncomunitaria.notificationapi.service;

import java.time.LocalDateTime;
import java.util.Optional;

// CORRECCIÓN 1: Añadir imports para Page y Pageable
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import com.integracioncomunitaria.notificationapi.dto.NotificationCreateDTO;
import com.integracioncomunitaria.notificationapi.dto.NotificationDTO;
import com.integracioncomunitaria.notificationapi.dto.NotificationHistoryDTO;
import com.integracioncomunitaria.notificationapi.entity.Customer;
import com.integracioncomunitaria.notificationapi.entity.Notification;
import com.integracioncomunitaria.notificationapi.entity.NotificationHistory;
import com.integracioncomunitaria.notificationapi.entity.Provider;
import com.integracioncomunitaria.notificationapi.entity.User;
import com.integracioncomunitaria.notificationapi.repository.NotificationHistoryRepository;
import com.integracioncomunitaria.notificationapi.repository.NotificationRepository;
import com.integracioncomunitaria.notificationapi.repository.CustomerRepository;
import com.integracioncomunitaria.notificationapi.repository.ProviderRepository;


/**
 * Implementación del servicio que gestiona la lógica de negocio de las notificaciones.
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repo;
    private final NotificationHistoryRepository historyRepo;
    private final CustomerRepository customerRepo;
    private final ProviderRepository providerRepo;

    public NotificationServiceImpl(NotificationRepository repo,
                                   NotificationHistoryRepository historyRepo,
                                   CustomerRepository customerRepo,
                                   ProviderRepository providerRepo) {
        this.repo = repo;
        this.historyRepo = historyRepo;
        this.customerRepo = customerRepo;
        this.providerRepo = providerRepo;
    }

    // El método create() no necesita cambios
    @Override
    public NotificationDTO create(NotificationCreateDTO dto, Integer currentUserId) {
        Notification n = new Notification();

        Optional<Customer> customerOptional = customerRepo.findByUser_IdUser(currentUserId);
        customerOptional.ifPresent(n::setCustomer);

        Optional<Provider> providerOptional = providerRepo.findByUser_IdUser(currentUserId);
        providerOptional.ifPresent(n::setProvider);

        n.setMessage(dto.getMessage());
        n.setViewed(false);
        n.setDeleted(false);
        
        n = repo.save(n);
        saveHistory(n, "CREATED");
        return map(n);
    }

    /**
     * CORRECCIÓN 2: Modificar el método list para paginación.
     */
    @Override
    public Page<NotificationDTO> list(Integer customerId,
                                      Integer providerId,
                                      Boolean viewed,
                                      LocalDateTime from,
                                      LocalDateTime to,
                                      Pageable pageable) { // <- Añadir Pageable

        LocalDateTime f = from != null ? from : LocalDateTime.of(1970, 1, 1, 0, 0);
        LocalDateTime t = to   != null ? to   : LocalDateTime.now();

        // Llamar al método paginado del repositorio
        Page<Notification> notificationPage = repo.findFiltered(customerId, providerId, viewed, f, t, pageable);

        // Usar el método .map() de la clase Page para convertir los resultados a DTO
        return notificationPage.map(this::map);
    }

    // El método getById() no necesita cambios
    @Override
    public NotificationDTO getById(Integer id) {
        Notification n = repo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Notificación no existe"));
        return map(n);
    }

    // El método delete() no necesita cambios
    @Override
    public void delete(Integer id) {
        Notification n = repo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Notificación no existe"));

        n.setDeleted(true);
        n.setIdUserUpdate(getCurrentUserId());
        n.setDateUpdate(LocalDateTime.now());
        repo.save(n);
        saveHistory(n, "DELETED");
    }

    // El método markAsViewed() no necesita cambios
    @Override
    public NotificationDTO markAsViewed(Integer id, Integer currentUserId) {
        Notification n = repo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Notificación no existe"));

        n.setViewed(true);
        n.setIdUserUpdate(currentUserId);
        n.setDateUpdate(LocalDateTime.now());
        Notification updated = repo.save(n);
        saveHistory(updated, "VIEWED");
        return map(updated);
    }

    /**
     * CORRECCIÓN 3: Modificar el método history para paginación.
     */
    @Override
    public Page<NotificationHistoryDTO> history(Integer notificationId, Pageable pageable) {
        
        // Llamar al método paginado del repositorio
        Page<NotificationHistory> historyPage = historyRepo.findByNotification_IdNotification(notificationId, pageable);
        
        // Usar el método .map() de la clase Page para convertir los resultados a DTO
        return historyPage.map(h -> new NotificationHistoryDTO(
                              h.getIdNotificationHistory(),
                              h.getNotification().getIdNotification(),
                              h.getEvent(),
                              h.getEventDate(),
                              h.getUser().getIdUser(),
                              h.getDateCreate()
                          ));
    }

    // --- Los métodos privados no necesitan cambios ---

    private void saveHistory(Notification notification, String event) {
        NotificationHistory h = new NotificationHistory();
        h.setNotification(notification);
        h.setEvent(event);
        h.setEventDate(LocalDateTime.now());
        h.setUser(new User(getCurrentUserId()));
        historyRepo.save(h);
    }

    private NotificationDTO map(Notification n) {
        return new NotificationDTO(
            n.getIdNotification(),
            n.getProvider() != null ? n.getProvider().getIdProvider()  : null,
            n.getCustomer() != null ? n.getCustomer().getIdCustomer()  : null,
            n.getMessage(),
            n.isViewed(),
            n.getDateCreate(),
            Boolean.TRUE.equals(n.getDeleted())
        );
    }

    private Integer getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         if (principal instanceof Integer) {
            return (Integer) principal;
        }
        throw new IllegalStateException("Usuario no autenticado o principal no es un Integer.");
    }
}