package com.integracioncomunitaria.notificationapi.controller;

// DTOs y entidades usadas para gestionar notificaciones
import com.integracioncomunitaria.notificationapi.dto.NotificationCreateDTO;
import com.integracioncomunitaria.notificationapi.dto.NotificationDTO;
import com.integracioncomunitaria.notificationapi.dto.NotificationHistoryDTO;
import com.integracioncomunitaria.notificationapi.entity.Customer;
import com.integracioncomunitaria.notificationapi.entity.Notification;
import com.integracioncomunitaria.notificationapi.entity.Provider;

// Repositorios JPA para obtener información del usuario
import com.integracioncomunitaria.notificationapi.repository.CustomerRepository;
import com.integracioncomunitaria.notificationapi.repository.ProviderRepository;

// Servicio con la lógica de negocio de notificaciones
import com.integracioncomunitaria.notificationapi.service.NotificationService;

// Validación de entrada
import jakarta.validation.Valid;

// Anotaciones y tipos para gestionar peticiones REST en Spring
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST que maneja todas las operaciones relacionadas con notificaciones.
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService svc;
    private final CustomerRepository customerRepo;
    private final ProviderRepository providerRepo;

    // Constructor con inyección de dependencias necesarias
    public NotificationController(NotificationService svc,
                                  CustomerRepository customerRepo,
                                  ProviderRepository providerRepo) {
        this.svc = svc;
        this.customerRepo = customerRepo;
        this.providerRepo = providerRepo;
    }

    /**
     * Crea una nueva notificación.
     * Endpoint: POST /api/notifications
     */
    @PostMapping
    public ResponseEntity<NotificationDTO> create(
            @RequestBody @Valid NotificationCreateDTO dto) {
        Integer uid = getCurrentUserId();
        NotificationDTO result = svc.create(dto, uid);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * Lista las notificaciones del usuario autenticado.
     * Opcionalmente permite filtrar por estado (vistas/no vistas) y rango de fechas.
     * Endpoint: GET /api/notifications
     */
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

        // Obtiene el customerId y providerId asociados al usuario autenticado
        Integer customerId = customerRepo
            .findByUser_IdUser(userId)
            .map(Customer::getIdCustomer)
            .orElse(null);

        Integer providerId = providerRepo
            .findByUser_IdUser(userId)
            .map(Provider::getIdProvider)
            .orElse(null);

        // Lista notificaciones filtradas por usuario y parámetros opcionales
        return svc.list(customerId, providerId, viewed, from, to);
    }

    /**
     * Obtiene una notificación específica por su ID.
     * Devuelve HTTP 410 si la notificación está marcada como borrada.
     * Endpoint: GET /api/notifications/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        NotificationDTO dto = svc.getById(id);

        if (dto.isDeleted()) {
            // HTTP 410 indica que el recurso ya no está disponible
            return ResponseEntity
                .status(HttpStatus.GONE)
                .body(Map.of("message", "Notificación borrada"));
        }
        return ResponseEntity.ok(dto);
    }

    /**
     * Marca una notificación como borrada (soft delete).
     * Endpoint: DELETE /api/notifications/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        svc.delete(id);
        // HTTP 204 indica operación exitosa sin contenido
        return ResponseEntity.noContent().build();
    }

    /**
     * Marca una notificación específica como vista por el usuario autenticado.
     * Endpoint: PUT /api/notifications/{id}/view
     */
    @PatchMapping("/{id}/view") // Cambiado a PatchMapping
    public NotificationDTO markAsViewed(@PathVariable Integer id) {
        Integer uid = getCurrentUserId();
        return svc.markAsViewed(id, uid);
    }

    /**
     * Obtiene el historial completo de modificaciones de una notificación específica.
     * Endpoint: GET /api/notifications/{id}/history
     */
    @GetMapping("/{id}/history")
    public List<NotificationHistoryDTO> history(@PathVariable Integer id) {
        return svc.history(id);
    }

    /**
     * Método privado que obtiene el ID del usuario actualmente autenticado.
     * Este ID se guarda en SecurityContext durante la autenticación JWT.
     */
    private Integer getCurrentUserId() {
        return (Integer) SecurityContextHolder
                         .getContext()
                         .getAuthentication()
                         .getPrincipal();
    }

   
}
