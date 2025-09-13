package com.licencias.controllers;

import com.licencias.Services.LicenciaService;
import com.licencias.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/licencias")
public class LicenciaController {
    private final Formatos formatos;
    private final LicenciaService licenciaServices;

    @PostMapping("/iniciarSesion")
    public Response<Licencias> recuperar(@RequestBody Usuario usuario) {
        return formatos.getResponseDto(licenciaServices.recuperar(usuario));
    }

    @PostMapping("/crear")
    public Response<Respuestas> grabar(@RequestBody Licencias licencia) {
        return formatos.getResponseDto(licenciaServices.createLicencia(licencia));
    }

    @PostMapping("/crearPrueba")
    public Response<Respuestas> grabarprueba(@RequestBody Licencias licencia) {
        return formatos.getResponseDto(licenciaServices.crearLicenciaPrueba(licencia));
    }

    @DeleteMapping("/delete")
    public Response<Respuestas> delete(@RequestBody Licencias licencia) {
        return formatos.getResponseDto(licenciaServices.deleteLicencia(licencia));
    }

}
