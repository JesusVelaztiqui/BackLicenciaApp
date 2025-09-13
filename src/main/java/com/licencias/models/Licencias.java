package com.licencias.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Licencias {
    private String licruc,lictel;
    private Long id,licport;
    private String licnombre,licapellido,licdireccion,licemail,licpassword,licmotivofechafin,licip,licpasdatabase;
    private boolean licestado ,prueba,anual;
    private LocalDate licfechaingreso,licfechafin;

}
