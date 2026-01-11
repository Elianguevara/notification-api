# 🔔 Notification API - Integración Comunitaria

![Java](https://img.shields.io/badge/Java-17-ed8b00?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.0+-6db33f?style=for-the-badge&logo=springboot)
![JWT](https://img.shields.io/badge/Security-JWT-black?style=for-the-badge&logo=jsonwebtokens)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36?style=for-the-badge&logo=apachemaven)

## 📖 Descripción

La **Notification API** es un microservicio backend diseñado para gestionar la comunicación y el flujo de notificaciones dentro de la plataforma de **Integración Comunitaria**. Su objetivo principal es orquestar el envío de alertas, registrar el historial de comunicaciones y gestionar la autenticación de usuarios y proveedores.

El sistema maneja entidades clave como **Peticiones (Petitions)** y **Postulaciones (Postulations)**, sirviendo como nexo entre los Proveedores (`Provider`) y los Clientes (`Customer`).

## 🚀 Características Principales

### 🔐 Seguridad y Autenticación
* **JWT (JSON Web Token):** Implementación robusta con `JwtAuthenticationFilter` y `JwtTokenUtil`.
* **Roles y Permisos:** Gestión de acceso basada en `RoleType` y `UserProfile`.
* **Registro de Usuarios:** Endpoints dedicados en `RegistrationController` y `AuthController`.

### 📩 Gestión de Notificaciones
* **Envío y Recepción:** Control total sobre el ciclo de vida de una `Notification`.
* **Historial de Auditoría:** Registro detallado de eventos mediante `NotificationHistory`.
* **Categorización:** Clasificación de alertas mediante la entidad `Category`.

### 🏘️ Dominio de Negocio
* **Peticiones y Postulaciones:** Soporte para el flujo de trabajo de solicitudes comunitarias (`Petition`, `Postulation`).
* **Perfiles de Usuario:** Distinción clara entre entidades de negocio (`Provider`) y usuarios finales (`Customer`).

## 🛠️ Stack Tecnológico

* **Lenguaje:** Java 17
* **Framework:** Spring Boot 3
* **Seguridad:** Spring Security + JWT
* **Persistencia:** Spring Data JPA (Hibernate)
* **Base de Datos:** MySQL / PostgreSQL (Configurable en `DataSourceConfig`)
* **Build Tool:** Maven

## ⚙️ Instalación y Configuración

### Prerrequisitos
* Java JDK 17+
* Maven 3.8+
* Base de datos (MySQL o PostgreSQL)

### 1. Clonar el repositorio
```bash
git clone [https://github.com/elianguevara/notification-api.git](https://github.com/elianguevara/notification-api.git)
cd notification-api
