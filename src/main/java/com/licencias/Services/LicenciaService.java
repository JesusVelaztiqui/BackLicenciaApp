package com.licencias.Services;

import com.licencias.models.Licencias;
import com.licencias.models.Respuestas;
import com.licencias.models.Usuario;

public interface LicenciaService {
    Licencias recuperar(Usuario usuario);
    Respuestas createLicencia(Licencias licencia);
    Respuestas crearLicenciaPrueba(Licencias licencia);
    Respuestas deleteLicencia(Licencias licencia);
}
