package com.licencias.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Licencias {
    private Long id,licruc,lictel;
    private String licnombre,licapellido,licdireccion,licemail,licpassword,licmotivofechafin;
    private boolean licestado;
    private LocalDate licfechaingreso,licfechafin;

}
