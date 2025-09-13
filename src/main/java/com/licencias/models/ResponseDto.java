package com.licencias.models;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ResponseDto<T> (int status,
                              String mensaje,
                              List<T> data){
}
