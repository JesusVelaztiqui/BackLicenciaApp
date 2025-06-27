package com.licencias.Services;

import com.licencias.models.Licencias;
import com.licencias.models.Respuestas;

public interface LicenciaService {
    Licencias recuperar(Long ruc);
    Respuestas createLicencia(Licencias licencia);
    Respuestas deleteLicencia(Licencias licencia);
}
