package com.licencias.implemets;

import com.licencias.Services.LicenciaServices;
import com.licencias.connection.Conexion;
import com.licencias.models.Licencias;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@RequiredArgsConstructor
@Service
public class LicenciaImplemets implements LicenciaServices {
    private final Conexion utilConexion;

    @Override
    @SneakyThrows
    public Licencias recuperar(String ruc) {
        try (Connection conexion = utilConexion.getConexion();
             PreparedStatement ps = conexion.prepareStatement("SELECT * FROM licencias");
             ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                Licencias lic = new Licencias();
                lic.setId(rs.getLong("id"));
                lic.setLicruc(rs.getLong("licruc"));
                lic.setLictel(rs.getLong("lictel"));
                lic.setLicnombre(rs.getString("licnombre"));
                lic.setLicapellido(rs.getString("licapellido"));
                lic.setLicdireccion(rs.getString("licdireccion"));
                lic.setLicemail(rs.getString("licemail"));
                lic.setLicpassword(rs.getString("licpassword"));
                lic.setLicmotivofechafin(rs.getString("licmotivofechafin"));
                lic.setLicestado(rs.getBoolean("licestado"));
                lic.setLicfechaingreso(rs.getObject("licfechaingreso", java.time.LocalDate.class));
                lic.setLicfechafin(rs.getObject("licfechafin", java.time.LocalDate.class));
                return lic;
            }
        }
        return null;
    }

}
