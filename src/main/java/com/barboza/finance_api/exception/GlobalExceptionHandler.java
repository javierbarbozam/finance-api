package com.barboza.finance_api.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import com.barboza.finance_api.dto.exception.ApiException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Excepciones en services
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiException> handleResponseStatusException(ResponseStatusException ex) {
        ApiException error = new ApiException(
            ex.getStatusCode().value(),
            ex.getReason(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(ex.getStatusCode()).body(error);
    }

    // Errores de @Valid en DTOs
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiException> handleValidationException(MethodArgumentNotValidException ex) {
        String mensajes = ex.getBindingResult().getFieldErrors()
            .stream()
            .map(field -> field.getDefaultMessage())
            // .map(field -> field.getField() + ": " + field.getDefaultMessage())
            .collect(Collectors.joining(", "));

        ApiException error = new ApiException(
            HttpStatus.BAD_REQUEST.value(),
            mensajes,
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Exception genérica
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiException> handleGenericException(Exception ex) {
        ApiException error = new ApiException(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Error en el servidor",
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiException> handleJwtException(JwtException ex) {
        
        String message;

        if (ex instanceof ExpiredJwtException) {
            message = "La sesión ha expirado. Por favor ingrese nuevamente.";
        } else {
            message = "Ocurrió un error de autenticación. Por favor inice sesión nuevamente.";
        }

        ApiException error = new ApiException(
            HttpStatus.UNAUTHORIZED.value(),
            message,
            LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiException> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String message;

        if (ex.getRequiredType() != null && ex.getRequiredType().isEnum()) {
            message = String.format(
                "Valor inválido para '%s'. Valores permitidos: %s",
                ex.getName(),
                java.util.Arrays.toString(ex.getRequiredType().getEnumConstants())
            );
        } else {
            message = String.format("Valor inválido para el parámetro '%s'", ex.getName());
        }

        ApiException error = new ApiException(
            HttpStatus.BAD_REQUEST.value(),
            message,
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
