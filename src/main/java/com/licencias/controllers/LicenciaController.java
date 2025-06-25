package com.licencias.controllers;

import com.licencias.Services.LicenciaServices;
import com.licencias.models.Formatos;
import com.licencias.models.Licencias;
import com.licencias.models.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/licencias")
public class LicenciaController {
    private final Formatos formatos;
    private final LicenciaServices licenciaServices;

    @GetMapping("/recuperar")
    public Response<Licencias> recuperar() {
        return formatos.getResponseDto(licenciaServices.recuperar());
    }
}
