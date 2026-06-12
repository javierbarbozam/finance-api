package com.barboza.finance_api.dto.budget;

import java.math.BigDecimal;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @Valid
public class BudgetResponse {
    private Long id;
    private BigDecimal amount;
    private Integer month;
    private Integer year;
}
