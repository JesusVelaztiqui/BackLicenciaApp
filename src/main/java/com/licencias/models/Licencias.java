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
    private Long id,licruc,lictel,licport;
    private String licnombre,licapellido,licdireccion,licemail,licpassword,licmotivofechafin,licip,licpasdatabase;
    private boolean licestado;
    private LocalDate licfechaingreso,licfechafin;

}
