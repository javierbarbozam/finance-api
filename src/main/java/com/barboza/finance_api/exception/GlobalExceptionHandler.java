package com.barboza.finance_api.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import com.barboza.finance_api.dto.exception.ApiException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

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

    // TODO: Move to JwtUtils
    // @ExceptionHandler(JwtException.class)
    // public ResponseEntity<ApiException> handleJwtException(JwtException ex) {
        
    //     String message;

    //     if (ex instanceof ExpiredJwtException) {
    //         message = "La sesión ha expirado. Por favor ingrese nuevamente.";
    //     } else {
    //         message = "Ocurrió un error de autenticación. Por favor inice sesión nuevamente.";
    //     }

    //     ApiException error = new ApiException(
    //         HttpStatus.UNAUTHORIZED.value(),
    //         message,
    //         LocalDateTime.now()
    //     );

    //     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    // }

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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiException> handleNotReadableException(HttpMessageNotReadableException ex) {
        String message = "La solicitud tiene un campo inválido.";

        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException ife && !ife.getPath().isEmpty()) {
            String field = ife.getPath().get(0).getFieldName();
            message = String.format("El campo '%s' tiene un formato inválido.", field);

            // Tells the client expected format
            String type = ife.getTargetType() != null ? ife.getTargetType().getSimpleName() : null;
            if ("LocalDate".equals(type)) {
                message = String.format(
                    "El campo '%s' tiene un formato inválido. Formato esperado: yyyy-MM-dd (ej: 2026-06-16).",
                    field
                );
            } else {
                message = String.format(
                    "El campo '%s' tiene un formato inválido. Se esperaba un valor de tipo %s.",
                    field, type
                );
            }
        }

        ApiException error = new ApiException(
            HttpStatus.BAD_REQUEST.value(),
            message,
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiException> handleConstraintViolationException(ConstraintViolationException ex) {
        String messages = ex.getConstraintViolations()
            .stream()
            .map(this::buildViolationMessage)
            .collect(Collectors.joining(", "));

        ApiException error = new ApiException(
            HttpStatus.BAD_REQUEST.value(),
            messages,
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // PRIVATE METHODS

    private String buildViolationMessage(ConstraintViolation<?> violation) {
        String field = extractFieldName(violation.getPropertyPath().toString());
        Object invalidValue = violation.getInvalidValue();
        String expected = extractExpectedValue(violation);

        return String.format(
            "El parámetro '%s' tiene un valor inválido (%s). %s",
            field, invalidValue, expected
        );
    }

    private String extractFieldName(String propertyPath) {
        String[] parts = propertyPath.split("\\.");
        return parts[parts.length - 1];
    }

    private String extractExpectedValue(ConstraintViolation<?> violation) {
        var annotation = violation.getConstraintDescriptor().getAnnotation();

        if (annotation instanceof Min min) {
            return String.format("Debe ser mayor o igual a %d.", min.value());
        }
        if (annotation instanceof Max max) {
            return String.format("Debe ser menor o igual a %d.", max.value());
        }

        return violation.getMessage();
    }
}
