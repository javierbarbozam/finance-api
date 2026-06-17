package com.barboza.finance_api.dto.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Valid
public class TransactionResponse {
    private BigDecimal amount;
    private String description;
    private String type;
    private String category;
    private Long id;
    private LocalDate date;
    private LocalDateTime createdAt;
}
