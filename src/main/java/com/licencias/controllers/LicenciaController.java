package com.licencias.controllers;

import com.licencias.Services.LicenciaService;
import com.licencias.models.Formatos;
import com.licencias.models.Licencias;
import com.licencias.models.Response;
import com.licencias.models.Respuestas;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/licencias")
public class LicenciaController {
    private final Formatos formatos;
    private final LicenciaService licenciaServices;

    @GetMapping("/recuperar")
    public Response<Licencias> recuperar(@RequestParam String ruc) {
        return formatos.getResponseDto(licenciaServices.recuperar(ruc));
    }

    @PostMapping("/grabar")
    public Response<Respuestas> grabar(@RequestBody Licencias licencia) {
        return formatos.getResponseDto(licenciaServices.grabar(licencia));
    }

}
