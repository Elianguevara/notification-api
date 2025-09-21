package com.integracioncomunitaria.notificationapi.service;

import java.time.LocalDateTime;
import java.util.List;

import com.integracioncomunitaria.notificationapi.dto.NotificationCreateDTO;
import com.integracioncomunitaria.notificationapi.dto.NotificationDTO;
import com.integracioncomunitaria.notificationapi.dto.NotificationHistoryDTO;
import com.integracioncomunitaria.notificationapi.entity.Notification;

/**
 * Interfaz que define las operaciones disponibles para gestionar notificaciones.
 * Su implementación contendrá la lógica de negocio asociada.
 */
public interface NotificationService {

    /**
     * Crea una nueva notificación.
     *
     * @param dto             Datos necesarios para crear la notificación:
     *                        - providerId (opcional): ID del proveedor asociado.
     *                        - customerId (opcional): ID del cliente asociado.
     *                        - message: contenido del mensaje.
     * @param currentUserId   ID del usuario autenticado que realiza la creación.
     * @return                DTO con la información de la notificación creada.
     */
    NotificationDTO create(NotificationCreateDTO dto, Integer currentUserId);

    /**
     * Lista notificaciones filtradas por varios criterios.
     *
     * @param customerId      ID de cliente para filtrar las notificaciones (null = no filtrar).
     * @param providerId      ID de proveedor para filtrar las notificaciones (null = no filtrar).
     * @param viewed          Estado de visualización (true = solo vistas, false = solo no vistas, null = ambos).
     * @param from            Fecha/hora mínima para el filtrado (null = desde el inicio).
     * @param to              Fecha/hora máxima para el filtrado (null = hasta ahora).
     * @return                Lista de DTOs que cumplen los criterios especificados.
     */
    List<NotificationDTO> list(Integer customerId,
                               Integer providerId,
                               Boolean viewed,
                               LocalDateTime from,
                               LocalDateTime to);

    /**
     * Recupera una notificación por su ID.
     *
     * @param id              Identificador único de la notificación.
     * @return                DTO con los datos de la notificación solicitada.
     * @throws EntityNotFoundException si no existe la notificación con el ID dado.
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
     * Obtiene el historial de eventos (creación, vistas, eliminación) de una notificación.
     *
     * @param notificationId  ID de la notificación cuyo historial se consulta.
     * @return                Lista de DTOs con cada evento registrado para esa notificación.
     */
    List<NotificationHistoryDTO> history(Integer notificationId);


}
