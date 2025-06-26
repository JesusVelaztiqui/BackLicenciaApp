package com.licencias.connection;

import jakarta.annotation.PostConstruct;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class Conexion {

    @Value("${pServidor}")
    private String servidores;

    @Value("${pPuerto}")
    private int puerto;

    @Value("${pUsuario}")
    private String usuario;

    @Value("${pBaseDatos}")
    private String basedatos;

    @Value("${pClave}")
    private String clave;

    public static DataSource dataSource;

    @PostConstruct
    private void innit() {
        if (dataSource == null) {
            String url = "jdbc:postgresql://" + servidores.trim() + ":" + puerto + "/" + basedatos.trim();
            BasicDataSource basicDataSource = new BasicDataSource();
            basicDataSource.setValidationQuery("SELECT 1");
            basicDataSource.setTestOnBorrow(true);
            basicDataSource.setDriverClassName("org.postgresql.Driver");
            basicDataSource.setUsername(usuario);
            basicDataSource.setPassword(clave);
            basicDataSource.setUrl(url);
            basicDataSource.setMaxTotal(50);
            basicDataSource.setMaxIdle(40);
            basicDataSource.setInitialSize(25);
            basicDataSource.setMinIdle(10);
            basicDataSource.setMinEvictableIdleTimeMillis(900000);
            basicDataSource.setLogAbandoned(false);
            basicDataSource.setRemoveAbandonedOnBorrow(true);
            basicDataSource.setRemoveAbandonedTimeout(300);
            dataSource = basicDataSource;
        }
    }


    public Connection getConexion() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }
}
