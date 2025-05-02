// src/main/java/com/integracioncomunitaria/notificationapi/controller/NotificationController.java
package com.integracioncomunitaria.notificationapi.controller;

import com.integracioncomunitaria.notificationapi.dto.NotificationCreateDTO;
import com.integracioncomunitaria.notificationapi.dto.NotificationDTO;
import com.integracioncomunitaria.notificationapi.dto.NotificationHistoryDTO;
import com.integracioncomunitaria.notificationapi.entity.Customer;
import com.integracioncomunitaria.notificationapi.entity.Provider;
import com.integracioncomunitaria.notificationapi.repository.CustomerRepository;
import com.integracioncomunitaria.notificationapi.repository.ProviderRepository;
import com.integracioncomunitaria.notificationapi.service.NotificationService;

import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService svc;
    private final CustomerRepository customerRepo;
    private final ProviderRepository providerRepo;

    public NotificationController(NotificationService svc,
                                  CustomerRepository customerRepo,
                                  ProviderRepository providerRepo) {
        this.svc = svc;
        this.customerRepo = customerRepo;
        this.providerRepo = providerRepo;
    }

    @PostMapping
    public ResponseEntity<NotificationDTO> create(
            @RequestBody @Valid NotificationCreateDTO dto) {
        Integer uid = getCurrentUserId();
        NotificationDTO result = svc.create(dto, uid);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /** Listado “propio” de notificaciones según quién esté logueado */
    @GetMapping
    public List<NotificationDTO> list(
            @RequestParam(required = false) Boolean viewed,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to
    ) {
        Integer userId = getCurrentUserId();

        Integer customerId = customerRepo
            .findByUser_IdUser(userId)
            .map(Customer::getIdCustomer)
            .orElse(null);

        Integer providerId = providerRepo
            .findByUser_IdUser(userId)
            .map(Provider::getIdProvider)
            .orElse(null);

        return svc.list(customerId, providerId, viewed, from, to);
    }

    /**
     * Obtiene una notificación por ID.
     * Si está marcada como borrada, responde 410 con un mensaje sencillo.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        NotificationDTO dto = svc.getById(id);
        if (dto.isDeleted()) {
            return ResponseEntity
                .status(HttpStatus.GONE)
                .body(Map.of("message", "Notificación borrada"));
        }
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/view")
    public NotificationDTO markAsViewed(@PathVariable Integer id) {
        Integer uid = getCurrentUserId();
        return svc.markAsViewed(id, uid);
    }

    @GetMapping("/{id}/history")
    public List<NotificationHistoryDTO> history(@PathVariable Integer id) {
        return svc.history(id);
    }

    private Integer getCurrentUserId() {
        return (Integer) SecurityContextHolder
                         .getContext()
                         .getAuthentication()
                         .getPrincipal();
    }
}
