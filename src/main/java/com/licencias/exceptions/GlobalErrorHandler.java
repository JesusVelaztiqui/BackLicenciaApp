package com.licencias.exceptions;
import com.licencias.models.ResponseDto;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseDto<CustomErrorResponse> handleDefaultException(Exception ex, WebRequest request) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseDto<>(500, "Error del servidor", Arrays.asList());
    }


    @ExceptionHandler(SQLException.class)
    public ResponseDto<CustomErrorResponse> handleSQLException(SQLException ex, WebRequest request) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseDto<>(23505, mensajeErrorSQL(ex.getMessage(),ex.getSQLState()), Arrays.asList());
    }

    @ExceptionHandler(ModelNotFoundException.class)
    public ResponseDto<CustomErrorResponse> handleModelNotFoundException(ModelNotFoundException ex, WebRequest request) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseDto<>(404, ex.getMessage(), Arrays.asList());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseDto<CustomErrorResponse> handleBadRequestException(BadRequestException ex, WebRequest request) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseDto<>(400, ex.getMessage(), Arrays.asList());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream().map(error -> error.getField() + ": " + error.getDefaultMessage()).collect(Collectors.joining(","));

        CustomErrorResponse errorResponse = new CustomErrorResponse(LocalDateTime.now(), message, request.getDescription(false));

        return new ResponseEntity<>(new ResponseDto<>(400, "Petición incorrecta",Arrays.asList()), HttpStatus.BAD_REQUEST);
    }


    private String mensajeErrorSQL(String message, String sqlError) {
        int codigo = Integer.parseInt(sqlError);
        switch (codigo) {
            case 23505:
                if(message.indexOf("usumail")>0){
                    return "El correo del usuario ya existe";
                }else if(message.indexOf("direccion")>0){
                    return "La direccion ya existe";
                }else if(message.indexOf("telefonopk")>0){
                    return "El telefono ya existe";
                }else{
                    return message;
                }
            case 23503:
                return "El registro esta en uso";
            default:
                return "Error de sql";
        }
    }


}
