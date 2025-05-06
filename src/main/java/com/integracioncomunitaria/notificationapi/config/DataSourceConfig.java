package com.integracioncomunitaria.notificationapi.config;

// Importación de la fuente de datos HikariCP, un pool eficiente para conexiones a bases de datos
import com.zaxxer.hikari.HikariDataSource;

// Importaciones de Spring para crear beans y configurar la aplicación
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Configuración personalizada para establecer la conexión a la base de datos MySQL usando HikariCP.
 * Aquí se definen explícitamente los parámetros necesarios para conectarse a la base de datos.
 */
@Configuration
public class DataSourceConfig {

    // Usuario de la base de datos MySQL
    private static final String USER     = "ies9021_userdb";

    // Contraseña asociada al usuario de la base de datos
    private static final String PASSWORD = "Xsw23edc.2025";

    // Nombre de la base de datos a la que la aplicación se conectará
    private static final String DATABASE = "ies9021_coco";

    // Dirección del servidor donde está alojada la base de datos
    private static final String HOST     = "ies9021.edu.ar";

    // Puerto de conexión estándar para MySQL (por defecto es 3306)
    private static final String PORT     = "3306";

    // URL completa JDBC para establecer conexión con MySQL
    private static final String URL = 
        "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE +
        "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    /**
     * Bean que proporciona el DataSource configurado para HikariCP.
     * Spring utilizará este DataSource para gestionar todas las conexiones con la base de datos.
     */
    @Bean
    public DataSource dataSource() {
        // Instancia el DataSource basado en HikariCP
        HikariDataSource ds = new HikariDataSource();

        // Establece la URL JDBC para la conexión con la base de datos MySQL
        ds.setJdbcUrl(URL);

        // Usuario para la conexión a la base de datos
        ds.setUsername(USER);

        // Contraseña para la conexión a la base de datos
        ds.setPassword(PASSWORD);

        // Configuración opcional: máximo número de conexiones simultáneas en el pool
        // ds.setMaximumPoolSize(10);

        return ds;
    }
}