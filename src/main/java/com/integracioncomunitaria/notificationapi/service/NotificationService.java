package com.integracioncomunitaria.notificationapi.service;

import java.time.LocalDateTime;

// CORRECCIÓN 1: Añadir los imports para Page y Pageable
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.integracioncomunitaria.notificationapi.dto.NotificationCreateDTO;
import com.integracioncomunitaria.notificationapi.dto.NotificationDTO;
import com.integracioncomunitaria.notificationapi.dto.NotificationHistoryDTO;

/**
 * Interfaz que define las operaciones disponibles para gestionar notificaciones.
 * Su implementación contendrá la lógica de negocio asociada.
 */
public interface NotificationService {

    /**
     * Crea una nueva notificación.
     *
     * @param dto             Datos necesarios para crear la notificación.
     * @param currentUserId   ID del usuario autenticado que realiza la creación.
     * @return                DTO con la información de la notificación creada.
     */
    NotificationDTO create(NotificationCreateDTO dto, Integer currentUserId);

    /**
     * Lista notificaciones filtradas y paginadas.
     *
     * @param customerId      ID de cliente para filtrar (null = no filtrar).
     * @param providerId      ID de proveedor para filtrar (null = no filtrar).
     * @param viewed          Estado de visualización (null = ambos).
     * @param from            Fecha/hora mínima para el filtrado.
     * @param to              Fecha/hora máxima para el filtrado.
     * @param pageable        Objeto con la información de paginación (página, tamaño, orden).
     * @return                Una página de DTOs que cumplen los criterios.
     */
    // CORRECCIÓN 2: Cambiar el método list
    Page<NotificationDTO> list(Integer customerId,
                               Integer providerId,
                               Boolean viewed,
                               LocalDateTime from,
                               LocalDateTime to,
                               Pageable pageable);

    /**
     * Recupera una notificación por su ID.
     *
     * @param id              Identificador único de la notificación.
     * @return                DTO con los datos de la notificación solicitada.
     */
    NotificationDTO getById(Integer id);

    /**
     * Realiza un borrado lógico de la notificación (soft delete).
     *
     * @param id              ID de la notificación a marcar como eliminada.
     */
    void delete(Integer id);

    /**
     * Marca una notificación como vista por el usuario autenticado.
     *
     * @param id              ID de la notificación a actualizar.
     * @param currentUserId   ID del usuario que marca la notificación como vista.
     * @return                DTO con la notificación actualizada.
     */
    NotificationDTO markAsViewed(Integer id, Integer currentUserId);

    /**
     * Obtiene el historial de eventos de una notificación de forma paginada.
     *
     * @param notificationId  ID de la notificación cuyo historial se consulta.
     * @param pageable        Objeto con la información de paginación.
     * @return                Una página de DTOs con cada evento registrado.
     */
    // CORRECCIÓN 3: Cambiar el método history
    Page<NotificationHistoryDTO> history(Integer notificationId, Pageable pageable);
}