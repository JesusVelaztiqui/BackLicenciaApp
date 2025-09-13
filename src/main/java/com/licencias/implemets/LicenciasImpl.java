package com.licencias.implemets;

import com.licencias.Services.LicenciaService;
import com.licencias.connection.Conexion;
import com.licencias.models.Formatos;
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
import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class LicenciasImpl implements LicenciaService {
    private final Conexion utilConexion;
    private final Formatos formatos;

    @Override
    @SneakyThrows
    public Licencias recuperar(Usuario usuario) {
        try (Connection conexion = utilConexion.getConexion();
             PreparedStatement ps = conexion.prepareStatement(
                     "SELECT * FROM licencias WHERE licemail = ? AND licpassword = ?");
             PreparedStatement psEstado = conexion.prepareStatement("UPDATE licencias SET licestado = false WHERE id = ?")
             ) {

            ps.setString(1, formatos.decrypt(usuario.getEmail()));
            ps.setString(2, usuario.getPass());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    java.sql.Timestamp ts = rs.getTimestamp("licfechafin");
                    LocalDate fechaFin = (ts != null) ? ts.toLocalDateTime().toLocalDate() : null;
                    boolean prueba = rs.getBoolean("prueba");
                    if (prueba && LocalDate.now().isAfter(fechaFin)) {
                        psEstado.setLong(1, rs.getLong("id"));
                        psEstado.executeUpdate();
                        throw new ModelNotFoundException("Tu periodo gratuito terminó. Suscríbete y disfruta sin interrupciones.");
                    } else if (!prueba && LocalDate.now().isAfter(fechaFin)) {
                        psEstado.setLong(1, rs.getLong("id"));
                        psEstado.executeUpdate();
                    }
                    if(!rs.getBoolean("licestado")){
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
                            .licestado(rs.getBoolean("licestado"))
                            .prueba(prueba)
                            .licfechaingreso(rs.getDate("licfechaingreso").toLocalDate())
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
             PreparedStatement psCheck = conexion.prepareStatement("SELECT COUNT(*) FROM licencias WHERE licruc = ? OR licemail = ?");
             PreparedStatement ps = conexion.prepareStatement(insertSQL);
             Statement stmt = conexion.createStatement()) {

            psCheck.setString(1, licencia.getLicruc());
            psCheck.setString(2, licencia.getLicemail());

            var rs = psCheck.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return new Respuestas(false, "Ya existe una licencia con ese RUC o email", 0);
            }

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
    public Respuestas crearLicenciaPrueba(Licencias licencia) {
        try (Connection conexion = utilConexion.getConexion();
             PreparedStatement psCheck = conexion.prepareStatement("SELECT COUNT(*) FROM licencias WHERE licruc = ? OR licemail = ?")) {
            psCheck.setString(1, formatos.decrypt(licencia.getLicruc()));
            psCheck.setString(2, formatos.decrypt(licencia.getLicemail()));

            var rs = psCheck.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return new Respuestas(false, "Ya existe esa licencia", 0);
            }
        }

        String insertSQL = """
        INSERT INTO licencias(
            licruc, lictel, licport, licnombre, licapellido, licdireccion, 
            licemail, licpassword, licmotivofechafin, licip, licpasdatabase, 
            licestado, prueba, licfechaingreso, licfechafin
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
    """;

        String createBaseSQL = "CREATE DATABASE \"" + formatos.decrypt(licencia.getLicruc()) + "\"";

        try (Connection conexion = utilConexion.getConexion();
             PreparedStatement ps = conexion.prepareStatement(insertSQL);
             Statement stmt = conexion.createStatement()) {
            ps.setString(1, formatos.decrypt(licencia.getLicruc()));
            ps.setString(2, formatos.decrypt(licencia.getLictel()));
            ps.setLong(3, 5432);
            ps.setString(4, formatos.decrypt(licencia.getLicnombre()));
            ps.setString(5, formatos.decrypt(licencia.getLicapellido()));
            ps.setString(6, formatos.decrypt(licencia.getLicdireccion()));
            ps.setString(7, formatos.decrypt(licencia.getLicemail()));
            ps.setString(8, licencia.getLicpassword());
            ps.setString(9, formatos.decrypt(licencia.getLicmotivofechafin()));
            ps.setString(10, "localhost");
            ps.setString(11, "123");
            ps.setBoolean(12, true);
            ps.setBoolean(13, true);
            ps.setObject(14, licencia.getLicfechaingreso());
            ps.setObject(15, licencia.getLicfechafin());
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
