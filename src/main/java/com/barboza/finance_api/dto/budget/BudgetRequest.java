package com.barboza.finance_api.dto.budget;

import java.math.BigDecimal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @Valid
public class BudgetRequest {

    @NotNull(message = "Debe especificar un monto")
    private BigDecimal amount;

    @NotNull(message = "Debe especificar un mes")
    private Integer month;

    @NotNull(message = "Debe especificar el año")
    private Integer year;
}
