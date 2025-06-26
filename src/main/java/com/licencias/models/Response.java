package com.licencias.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Response<T> {
    private int status;
    private T data;
    private String mensaje;
    private int cantidad;
}

