package com.integracioncomunitaria.notificationapi.controller;

// DTOs y entidades
import com.integracioncomunitaria.notificationapi.dto.NotificationCreateDTO;
import com.integracioncomunitaria.notificationapi.dto.NotificationDTO;
import com.integracioncomunitaria.notificationapi.dto.NotificationHistoryDTO;
import com.integracioncomunitaria.notificationapi.entity.Customer;
import com.integracioncomunitaria.notificationapi.entity.Provider;

// Repositorios
import com.integracioncomunitaria.notificationapi.repository.CustomerRepository;
import com.integracioncomunitaria.notificationapi.repository.ProviderRepository;

// Servicio
import com.integracioncomunitaria.notificationapi.service.NotificationService;

// Validación
import jakarta.validation.Valid;

// CORRECCIÓN 1: Añadir imports para Paginación
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

// Anotaciones y tipos de Spring
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
// Se elimina import java.util.List; ya que no se usará en los endpoints modificados

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

    // El método create() no necesita cambios
    @PostMapping
    public ResponseEntity<NotificationDTO> create(
            @RequestBody @Valid NotificationCreateDTO dto) {
        Integer uid = getCurrentUserId();
        NotificationDTO result = svc.create(dto, uid);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * CORRECCIÓN 2: Modificar el método list para paginación.
     * Lista las notificaciones del usuario de forma paginada y filtrada.
     */
    @GetMapping
    public Page<NotificationDTO> list(
            @RequestParam(required = false) Boolean viewed,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "0") int page, // <- Parámetro para el número de página
            @RequestParam(defaultValue = "10") int size  // <- Parámetro para el tamaño de página
    ) {
        Integer userId = getCurrentUserId();
        Integer customerId = customerRepo.findByUser_IdUser(userId).map(Customer::getIdCustomer).orElse(null);
        Integer providerId = providerRepo.findByUser_IdUser(userId).map(Provider::getIdProvider).orElse(null);

        // Crear objeto Pageable para enviar al servicio, ordenando por fecha de creación
        Pageable pageable = PageRequest.of(page, size, Sort.by("dateCreate").descending());

        return svc.list(customerId, providerId, viewed, from, to, pageable);
    }

    // El método getById() no necesita cambios
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        NotificationDTO dto = svc.getById(id);
        if (dto.isDeleted()) {
            return ResponseEntity.status(HttpStatus.GONE).body(Map.of("message", "Notificación borrada"));
        }
        return ResponseEntity.ok(dto);
    }

    // El método delete() no necesita cambios
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }

    // El método markAsViewed() no necesita cambios
    @PatchMapping("/{id}/view")
    public NotificationDTO markAsViewed(@PathVariable Integer id) {
        Integer uid = getCurrentUserId();
        return svc.markAsViewed(id, uid);
    }

    /**
     * CORRECCIÓN 3: Modificar el método history para paginación.
     * Obtiene el historial paginado de una notificación.
     */
    @GetMapping("/{id}/history")
    public Page<NotificationHistoryDTO> history(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // Crear objeto Pageable para enviar al servicio, ordenando por fecha del evento
        Pageable pageable = PageRequest.of(page, size, Sort.by("eventDate").descending());
        return svc.history(id, pageable);
    }

    // El método getCurrentUserId() no necesita cambios
    private Integer getCurrentUserId() {
        return (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}