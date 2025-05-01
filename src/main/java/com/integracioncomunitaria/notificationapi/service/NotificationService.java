package com.integracioncomunitaria.notificationapi.service;
import java.time.LocalDateTime;
import java.util.List;
import com.integracioncomunitaria.notificationapi.dto.NotificationCreateDTO;
import com.integracioncomunitaria.notificationapi.dto.NotificationDTO;
import com.integracioncomunitaria.notificationapi.dto.NotificationHistoryDTO;



public interface NotificationService {
    NotificationDTO create(NotificationCreateDTO dto, Integer currentUserId);
    List<NotificationDTO> list(Integer customerId, Integer providerId, Boolean viewed,
                               LocalDateTime from, LocalDateTime to);
    NotificationDTO getById(Integer id);
    void delete(Integer id);
    NotificationDTO markAsViewed(Integer id, Integer currentUserId);
    List<NotificationHistoryDTO> history(Integer notificationId);
}
