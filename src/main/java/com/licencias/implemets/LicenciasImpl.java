package com.licencias.implemets;

import com.licencias.Services.LicenciaService;
import com.licencias.connection.Conexion;
import com.licencias.models.Licencias;
import com.licencias.models.Respuestas;
import com.licencias.exceptions.ModelNotFoundException;
import com.licencias.models.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@RequiredArgsConstructor
@Service
public class LicenciasImpl implements LicenciaService {
    private final Conexion utilConexion;

    @Override
    @SneakyThrows
    public Licencias recuperar(Usuario usuario) {
        try (Connection conexion = utilConexion.getConexion();
             PreparedStatement ps = conexion.prepareStatement(
                     "SELECT * FROM licencias WHERE licemail = ? AND licpassword = ?");
             PreparedStatement psEstado = conexion.prepareStatement("UPDATE licencias SET licestado = false WHERE id = ?")
             ) {

            ps.setString(1, usuario.getEmail());
            ps.setString(2, usuario.getPass());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    java.time.LocalDate fechaIngreso = rs.getObject("licfechaingreso", java.time.LocalDate.class);
                    java.time.LocalDate fechaFin = rs.getObject("licfechafin", java.time.LocalDate.class);

                    boolean estado = rs.getBoolean("licestado");
                    boolean prueba = rs.getBoolean("prueba");
                    boolean anual = rs.getBoolean("anual");
                    if (prueba) {
                        long periodoPrueba = java.time.temporal.ChronoUnit.DAYS.between(fechaIngreso, fechaFin);
                        if(periodoPrueba>=16){
                            psEstado.setLong(1, rs.getLong("id"));
                            psEstado.executeUpdate();
                            throw new ModelNotFoundException("Tu periodo gratuito terminó. Suscríbete y disfruta sin interrupciones.");
                        }
                    }else if(!prueba  && !anual){
                        long periodoMensual = java.time.temporal.ChronoUnit.DAYS.between(fechaIngreso, fechaFin);
                        if(periodoMensual>31){
                            psEstado.setLong(1, rs.getLong("id"));
                            psEstado.executeUpdate();

                        }
                    }else if(!prueba && anual){
                        long periodoAnual = java.time.temporal.ChronoUnit.DAYS.between(fechaIngreso, fechaFin);
                        if(periodoAnual>366){
                            psEstado.setLong(1, rs.getLong("id"));
                            psEstado.executeUpdate();

                        }
                    } if(!estado){
                        throw new ModelNotFoundException("Tu licencia ha caducado. Renueva para continuar.");
                    }
                    return Licencias.builder()
                            .id(rs.getLong("id"))
                            .licruc(rs.getString("licruc"))
                            .lictel(rs.getString("lictel"))
                            .licnombre(rs.getString("licnombre"))
                            .licapellido(rs.getString("licapellido"))
                            .licdireccion(rs.getString("licdireccion"))
                            .licemail(rs.getString("licemail"))
                            .licpassword(rs.getString("licpassword"))
                            .licmotivofechafin(rs.getString("licmotivofechafin"))
                            .licestado(estado)
                            .prueba(prueba)
                            .anual(anual)
                            .licfechaingreso(fechaIngreso)
                            .licfechafin(fechaFin)
                            .build();
                } else {
                    throw new ModelNotFoundException("Usuario no encontrado. Puedes registrarte y comenzar.");
                }
            }
        }
    }



    @Override
    @SneakyThrows
    public Respuestas createLicencia(Licencias licencia) {
        String insertSQL = """
        INSERT INTO licencias(
            licruc, lictel, licport, licnombre, licapellido, licdireccion, 
            licemail, licpassword, licmotivofechafin, licip, licpasdatabase, 
            licestado, licfechaingreso, licfechafin
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
    """;

        String createBaseSQL = "CREATE DATABASE \"" + licencia.getLicruc() + "\"";

        try (Connection conexion = utilConexion.getConexion();
             PreparedStatement ps = conexion.prepareStatement(insertSQL);
             Statement stmt = conexion.createStatement()) {
            ps.setString(1, licencia.getLicruc());
            ps.setString(2, licencia.getLictel());
            ps.setLong(3, licencia.getLicport());
            ps.setString(4, licencia.getLicnombre());
            ps.setString(5, licencia.getLicapellido());
            ps.setString(6, licencia.getLicdireccion());
            ps.setString(7, licencia.getLicemail());
            ps.setString(8, licencia.getLicpassword());
            ps.setString(9, licencia.getLicmotivofechafin());
            ps.setString(10, licencia.getLicip());
            ps.setString(11, licencia.getLicpasdatabase());
            ps.setBoolean(12, licencia.isLicestado());
            ps.setObject(13, licencia.getLicfechaingreso());
            ps.setObject(14, licencia.getLicfechafin());
            ps.execute();
            stmt.execute(createBaseSQL);

            return new Respuestas(true, Respuestas.grabado, 0);

        }
    }

    @Override
    @SneakyThrows
    public Respuestas deleteLicencia(Licencias licencia) {
        String deleteSQL = """
        DELETE FROM licencias WHERE licruc = ? AND id = ?
    """;
        String dropDatabaseSQL = "DROP DATABASE IF EXISTS \"" + licencia.getLicruc() + "\"";
        try (Connection conexion = utilConexion.getConexion();
             PreparedStatement ps = conexion.prepareStatement(deleteSQL);
             Statement stmt = conexion.createStatement()) {
            ps.setString(1, licencia.getLicruc());
            ps.setLong(2, licencia.getId());
            ps.execute();
            stmt.execute(dropDatabaseSQL);
            return new Respuestas(true, Respuestas.eliminado, 0);
        }
    }


}
