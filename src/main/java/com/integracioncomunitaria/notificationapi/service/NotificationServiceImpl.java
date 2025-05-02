package com.integracioncomunitaria.notificationapi.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;

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

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repo;
    private final NotificationHistoryRepository historyRepo;

    public NotificationServiceImpl(NotificationRepository repo,
                                   NotificationHistoryRepository historyRepo) {
        this.repo = repo;
        this.historyRepo = historyRepo;
    }

    @Override
    public NotificationDTO create(NotificationCreateDTO dto, Integer currentUserId) {
        Notification n = new Notification();
        if (dto.getProviderId() != null) {
            n.setProvider(new Provider(dto.getProviderId()));
        }
        if (dto.getCustomerId() != null) {
            n.setCustomer(new Customer(dto.getCustomerId()));
        }
        n.setType(dto.getType());
        n.setMessage(dto.getMessage());
        n.setViewed(false);
        n.setIdUserCreate(currentUserId);
        n.setDateCreate(LocalDateTime.now());
        n = repo.save(n);

        saveHistory(n.getIdNotification(), "CREATED", currentUserId);
        return map(n);
    }

    @Override
    public List<NotificationDTO> list(Integer customerId, Integer providerId,
                                      Boolean viewed, LocalDateTime from, LocalDateTime to) {
        LocalDateTime f = from != null ? from : LocalDateTime.of(1970, 1, 1, 0, 0);
        LocalDateTime t = to   != null ? to   : LocalDateTime.now();
        return repo.findFiltered(customerId, providerId, viewed, f, t)
                   .stream()
                   .map(this::map)
                   .toList();
    }

    @Override
    public NotificationDTO getById(Integer id) {
        Notification n = repo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Notificación no existe"));
        return map(n);
    }

    @Override
    public void delete(Integer id) {
        Integer userId = getCurrentUserId();
        saveHistory(id, "DELETED", userId);
        repo.deleteById(id);
    }

    @Override
    public NotificationDTO markAsViewed(Integer id, Integer currentUserId) {
        Notification n = repo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Notificación no existe"));
        n.setViewed(true);
        n.setIdUserUpdate(currentUserId);
        n.setDateUpdate(LocalDateTime.now());
        Notification updated = repo.save(n);
        saveHistory(id, "VIEWED", currentUserId);
        return map(updated);
    }

    @Override
    public List<NotificationHistoryDTO> history(Integer notificationId) {
        return historyRepo.findByNotification_IdNotification(notificationId)
                          .stream()
                          .map(h -> new NotificationHistoryDTO(
                              h.getIdNotificationHistory(),
                              h.getNotification().getIdNotification(),
                              h.getEvent(),
                              h.getEventDate(),
                              h.getUser().getIdUser()
                          ))
                          .toList();
    }

    private void saveHistory(Integer notifId, String event, Integer userId) {
        NotificationHistory h = new NotificationHistory();
        h.setNotification(new Notification(notifId));
        h.setEvent(event);
        h.setEventDate(LocalDateTime.now());
        User user = new User();
        user.setIdUser(userId);
        h.setUser(user);
        h.setDateCreate(LocalDateTime.now());
        historyRepo.save(h);
    }

    private NotificationDTO map(Notification n) {
        return new NotificationDTO(
            n.getIdNotification(),
            n.getProvider() != null ? n.getProvider().getIdProvider() : null,
            n.getCustomer() != null ? n.getCustomer().getIdCustomer() : null,
            n.getType(),
            n.getMessage(),
            n.isViewed(),
            n.getDateCreate()
        );
    }

    private Integer getCurrentUserId() {
        return (Integer) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
