package com.integracioncomunitaria.notificationapi.entity;

import lombok.Getter;

/**
 * Enumeración que define los tipos de rol disponibles en la aplicación.
 * Cada valor determina los permisos y funcionalidades a las que
 * un usuario autenticado puede acceder.
 */
@Getter
public enum RoleType {

    /**
     * Cliente: usuario que solo puede realizar acciones de consumo,
     * como crear peticiones y recibir notificaciones.
     */
    cliente,

    /**
     * Proveedor: usuario que ofrece servicios,
     * puede postular ofertas y recibir peticiones.
     */
    proveedor,

    /**
     * Ambos: usuario que combina roles de cliente y proveedor,
     * permitiendo acceso a funcionalidades de ambos tipos.
     */
    ambos
}
