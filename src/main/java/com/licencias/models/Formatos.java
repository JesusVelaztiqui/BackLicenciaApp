package com.licencias.models;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
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

    private static final String KEY = "1234567890123456";
    private static final String IV  = "abcdef1234567890";

    public static String encrypt(String valor) throws Exception {
        if (valor == null || valor.isEmpty()) return "";

        IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes("UTF-8"));
        SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        byte[] encrypted = cipher.doFinal(valor.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String encrypted) throws Exception {
        if (encrypted == null || encrypted.isEmpty()) return "";

        IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes("UTF-8"));
        SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        byte[] decodedBytes = Base64.getDecoder().decode(encrypted);
        byte[] decrypted = cipher.doFinal(decodedBytes);

        return new String(decrypted, "UTF-8");
    }
}
