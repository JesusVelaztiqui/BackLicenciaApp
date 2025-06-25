package com.licencias.models;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Formatos {
    public <T> Response<T> getResponseDto(T obj){
        if(obj instanceof List){
            return new Response<>(200,obj,"",((List) obj).size());
        }else if(obj instanceof Respuestas) {
            if(((Respuestas) obj).isEstado()){
                return new Response<>(200,null,((Respuestas) obj).getResp(),1);
            }else{
                return new Response<>(404,null,((Respuestas) obj).getResp(),0);
            }
        }else{
            return new Response<>(200,obj,"",1);
        }
    }
}
