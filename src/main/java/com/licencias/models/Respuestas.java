package com.licencias.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data

public class Respuestas {
    public static String grabado = "Registro grabado con éxito";
    public static String eliminado = "Registro eliminado con éxito";
    public static String modificado = "Registro modificado con éxito";
    boolean estado;
    String resp;
    long id;
}


