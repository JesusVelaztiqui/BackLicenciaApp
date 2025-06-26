package com.licencias.implemets;

import com.licencias.Services.LicenciaService;
import com.licencias.connection.Conexion;
import com.licencias.connection.ConexionUsuario;
import com.licencias.models.Licencias;
import com.licencias.models.Respuestas;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@RequiredArgsConstructor
@Service
public class LicenciasImpl implements LicenciaService {
    private final Conexion utilConexion;
    private final ConexionUsuario utilConexionUsuario;

    @Override
    @SneakyThrows
    public Licencias recuperar(String ruc) {
        try (Connection conexion = utilConexion.getConexion();
             PreparedStatement ps = conexion.prepareStatement("SELECT * FROM licencias");
             ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                Licencias lic = Licencias.builder()
                        .id(rs.getLong("id"))
                        .licruc(rs.getLong("licruc"))
                        .lictel(rs.getLong("lictel"))
                        .licnombre(rs.getString("licnombre"))
                        .licapellido(rs.getString("licapellido"))
                        .licdireccion(rs.getString("licdireccion"))
                        .licemail(rs.getString("licemail"))
                        .licpassword(rs.getString("licpassword"))
                        .licmotivofechafin(rs.getString("licmotivofechafin"))
                        .licestado(rs.getBoolean("licestado"))
                        .licfechaingreso(rs.getObject("licfechaingreso", java.time.LocalDate.class))
                        .licfechafin(rs.getObject("licfechafin", java.time.LocalDate.class))
                        .build();
                return lic;
            }
        }

        return null;
    }

    @Override
    @SneakyThrows
    public Respuestas grabar(Licencias licencia) {
        try (Connection conexion = utilConexion.getConexion();
        PreparedStatement ps = conexion.prepareStatement("""
INSERT INTO public.licencias(
	 licruc, lictel, licport, licnombre, licapellido, licdireccion, licemail, licpassword, licmotivofechafin, licip, licpasdatabase,
                             licestado, licfechaingreso, licfechafin)
	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?);
""")){
            ps.setLong(1, licencia.getLicruc());
            ps.setLong(2, licencia.getLictel());
            ps.setLong(3, licencia.getLicport());
            ps.setString(4, licencia.getLicnombre());
            ps.setString(5, licencia.getLicapellido());
            ps.setString(6, licencia.getLicdireccion());
            ps.setString(7, licencia.getLicemail());
            ps.setString(8, licencia.getLicpassword());
            ps.setString(9, licencia.getLicmotivofechafin());
            ps.setString(10,licencia.getLicip());
            ps.setString(11, licencia.getLicpasdatabase());
            ps.setBoolean(12,licencia.isLicestado());
            ps.setObject(13,licencia.getLicfechaingreso());
            ps.setObject(14,licencia.getLicfechafin());
            String nombreDb = "Cliente" + licencia.getLicruc();
            String sql = "CREATE DATABASE \"" + nombreDb + "\"";

            ps.execute();
            try (Connection conexion2 = utilConexionUsuario.getConexion();
            PreparedStatement ps1 = conexion2.prepareStatement(sql)
            ){
                ps1.execute();

            }
            return new Respuestas(true,Respuestas.grabado,0);
        }

    }

}
