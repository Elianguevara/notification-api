package com.integracioncomunitaria.notificationapi.controller;

import com.integracioncomunitaria.notificationapi.dto.NotificationCreateDTO;
import com.integracioncomunitaria.notificationapi.dto.NotificationDTO;
import com.integracioncomunitaria.notificationapi.dto.NotificationHistoryDTO;
import com.integracioncomunitaria.notificationapi.service.NotificationService;

import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService svc;

    public NotificationController(NotificationService svc) {
        this.svc = svc;
    }

    @PostMapping
    public ResponseEntity<NotificationDTO> create(
        @RequestBody @Valid NotificationCreateDTO dto) {
        Integer uid = getCurrentUserId();
        NotificationDTO result = svc.create(dto, uid);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping
    public List<NotificationDTO> list(
        @RequestParam(required = false) Integer customerId,
        @RequestParam(required = false) Integer providerId,
        @RequestParam(required = false) Boolean viewed,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime from,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime to
    ) {
        return svc.list(customerId, providerId, viewed, from, to);
    }

    @GetMapping("/{id}")
    public NotificationDTO getById(@PathVariable Integer id) {
        return svc.getById(id);
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
