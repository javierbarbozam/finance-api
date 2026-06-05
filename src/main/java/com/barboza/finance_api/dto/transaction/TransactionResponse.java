package com.barboza.finance_api.dto.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class TransactionResponse {
    private BigDecimal amount;
    private String description;
    private String type;
    private String category;
    private Long id;
    private LocalDateTime createdAt;
}
