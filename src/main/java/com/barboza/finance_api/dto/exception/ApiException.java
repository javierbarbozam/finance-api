package com.barboza.finance_api.dto.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class ApiException {
    private int status;
    private String message;
    private LocalDateTime timestamp;
}
