package com.licencias.Services;

import com.licencias.models.Licencias;
import com.licencias.models.Respuestas;

public interface LicenciaService {
    Licencias recuperar(String ruc);
    Respuestas grabar(Licencias licencia);
}
