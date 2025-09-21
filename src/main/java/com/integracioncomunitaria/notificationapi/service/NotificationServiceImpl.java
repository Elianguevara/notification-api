// src/main/java/com/integracioncomunitaria/notificationapi/service/NotificationServiceImpl.java
package com.integracioncomunitaria.notificationapi.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional; // Importar Optional

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
import com.integracioncomunitaria.notificationapi.repository.CustomerRepository; // Importar CustomerRepository
import com.integracioncomunitaria.notificationapi.repository.ProviderRepository; // Importar ProviderRepository


/**
 * Implementación del servicio que gestiona la lógica de negocio de las notificaciones.
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repo;
    private final NotificationHistoryRepository historyRepo;
    private final CustomerRepository customerRepo; // Inyectar CustomerRepository
    private final ProviderRepository providerRepo; // Inyectar ProviderRepository


    // Constructor con inyección de dependencias, incluyendo los nuevos repositorios
    public NotificationServiceImpl(NotificationRepository repo,
                                   NotificationHistoryRepository historyRepo,
                                   CustomerRepository customerRepo,
                                   ProviderRepository providerRepo) {
        this.repo = repo;
        this.historyRepo = historyRepo;
        this.customerRepo = customerRepo;
        this.providerRepo = providerRepo;
    }

    /**
     * Crea una nueva notificación asociada al usuario logueado y sus perfiles (cliente/proveedor).
     */
    @Override
    public NotificationDTO create(NotificationCreateDTO dto, Integer currentUserId) {
        Notification n = new Notification();

        // --- Obtener id_customer y/o id_provider a partir del id_user logueado ---

        // Buscar si el usuario logueado tiene un perfil de cliente
        Optional<Customer> customerOptional = customerRepo.findByUser_IdUser(currentUserId);
        // Si se encuentra, asigna la entidad Customer a la notificación
        customerOptional.ifPresent(n::setCustomer);

        // Buscar si el usuario logueado tiene un perfil de proveedor
        Optional<Provider> providerOptional = providerRepo.findByUser_IdUser(currentUserId);
        // Si se encuentra, asigna la entidad Provider a la notificación
        providerOptional.ifPresent(n::setProvider);

        // Nota: Si un usuario tiene ambos roles, la notificación se asociará tanto al cliente como al proveedor.
        // Si la lógica de negocio dicta que solo debe asociarse a uno si tiene ambos,
        // necesitarías añadir una condición aquí, quizás basada en el rol del usuario
        // (puedes obtenerlo del SecurityContextHolder o inyectando UserRepository y cargando el User con su Profile).


        // Información básica de la notificación (mensaje) - Viene del DTO
        n.setMessage(dto.getMessage());
        n.setViewed(false); // Nueva notificación no está vista por defecto
        n.setDeleted(false); // Nueva notificación no está eliminada por defecto

        // Auditoría - Spring Data JPA Auditing se encargará de llenar id_user_create y date_create
        // Si la auditoría está activa y configurada correctamente, las siguientes líneas son redundantes:
        // n.setIdUserCreate(currentUserId);
        // n.setDateCreate(LocalDateTime.now());

        // Guarda la notificación en la base de datos
        n = repo.save(n);

        // Guarda el historial con evento "CREATED", asociado al usuario logueado
        saveHistory(n, "CREATED");

        // Devuelve el DTO mapeado (que ahora contendrá los IDs de cliente/proveedor obtenidos)
        return map(n);
    }

    /**
     * Lista las notificaciones filtradas por cliente/proveedor (obtenidos del usuario logueado en el controlador),
     * estado (vistas o no) y fechas.
     */
    @Override
    public List<NotificationDTO> list(Integer customerId,
                                      Integer providerId,
                                      Boolean viewed,
                                      LocalDateTime from,
                                      LocalDateTime to) {

        LocalDateTime f = from != null ? from : LocalDateTime.of(1970,1,1,0,0);
        LocalDateTime t = to   != null ? to   : LocalDateTime.now();

        // La consulta en el repositorio utiliza los IDs de cliente/proveedor que
        // fueron obtenidos a partir del ID del usuario logueado en el controlador.
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
     * Marca una notificación como eliminada (soft delete) por el usuario actual.
     */
    @Override
    public void delete(Integer id) {
        Notification n = repo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Notificación no existe"));

        n.setDeleted(true);
        // Auditoría de modificación (se llena automáticamente si Auditing está activo)
        n.setIdUserUpdate(getCurrentUserId()); // Aunque Auditing lo haría, aquí lo asignas manualmente
        n.setDateUpdate(LocalDateTime.now()); // Aunque Auditing lo haría, aquí lo asignas manualmente

        repo.save(n);

        // Guarda el historial con evento "DELETED", asociado al usuario logueado
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
        // Auditoría de modificación (se llena automáticamente si Auditing está activo)
        n.setIdUserUpdate(currentUserId); // Aunque Auditing lo haría, aquí lo asignas manualmente
        n.setDateUpdate(LocalDateTime.now()); // Aunque Auditing lo haría, aquí lo asignas manualmente

        Notification updated = repo.save(n);

        // Guarda el historial con evento "VIEWED", asociado al usuario logueado
        saveHistory(updated, "VIEWED");

        return map(updated);
    }

    /**
     * Obtiene el historial completo de una notificación específica.
     */
    @Override
    public List<NotificationHistoryDTO> history(Integer notificationId) {
        // La consulta en historyRepo.findByNotification_IdNotification ya obtiene el historial
        // La auditoría en NotificationHistory ya asocia el evento al usuario que realizó la acción
        return historyRepo.findByNotification_IdNotification(notificationId)
                          .stream()
                          .map(h -> new NotificationHistoryDTO(
                              h.getIdNotificationHistory(),
                              h.getNotification().getIdNotification(),
                              h.getEvent(),
                              h.getEventDate(),
                              h.getUser().getIdUser(), // Obtiene el ID del usuario del historial
                              h.getDateCreate()
                          ))
                          .toList();
    }

    // ——— Helper methods ———

    /** Inserta un registro de historial, Auditing rellena id_user_create y date_create (en la entidad History). */
    private void saveHistory(Notification notification, String event) {
        NotificationHistory h = new NotificationHistory();
        h.setNotification(notification);
        h.setEvent(event);
        h.setEventDate(LocalDateTime.now());
        // Asocia el historial directamente con el usuario logueado
        h.setUser(new User(getCurrentUserId())); // Esto utiliza el constructor proxy de User
        historyRepo.save(h);
    }

    private NotificationDTO map(Notification n) {
        return new NotificationDTO(
            n.getIdNotification(),
            // Al mapear a DTO, obtenemos los IDs de las entidades Provider/Customer asociadas
            n.getProvider()   != null ? n.getProvider().getIdProvider()  : null,
            n.getCustomer()   != null ? n.getCustomer().getIdCustomer()  : null,
            n.getMessage(),
            n.isViewed(),
            n.getDateCreate(),
            Boolean.TRUE.equals(n.getDeleted())      // ← mapear el flag
        );
    }

    /**
     * Obtiene el ID del usuario autenticado actualmente desde el contexto de seguridad.
     */
    private Integer getCurrentUserId() {
        // Este método ya funciona correctamente para obtener el ID del principal autenticado
        // que debe ser el Integer userId que pusiste en el token y en el SecurityContextHolder.
        // Si el usuario no está autenticado, lanzará una excepción (lo cual es deseable para endpoints protegidos).
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         if (principal instanceof Integer) {
            return (Integer) principal;
        }
        // Dependiendo de tu lógica de seguridad, podrías devolver null o lanzar una excepción diferente
        // si el principal no es un Integer cuando esperas que lo sea.
        throw new IllegalStateException("Usuario no autenticado o principal no es un Integer.");
    }


}