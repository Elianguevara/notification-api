package com.integracioncomunitaria.notificationapi.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    private static final String USER     = "ies9021_userdb";
    private static final String PASSWORD = "Xsw23edc.2025";
    private static final String DATABASE = "ies9021_coco";
    private static final String HOST     = "ies9021.edu.ar";
    private static final String PORT     = "3306";

    private static final String URL = 
        "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE +
        "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    @Bean
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(URL);
        ds.setUsername(USER);
        ds.setPassword(PASSWORD);
        // opcional: ds.setMaximumPoolSize(10);
        return ds;
    }
}
