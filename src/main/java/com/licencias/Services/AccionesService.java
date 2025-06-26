package com.licencias.Services;

import com.licencias.models.Licencias;

public interface LicenciaServices<T> {
    Licencias recuperar(T object);
}
