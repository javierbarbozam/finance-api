package com.barboza.finance_api.dto.budget;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @Valid
public class BudgetRequest {

    @NotNull(message = "Debe especificar un monto")
    @Positive(message = "El monto debe ser positivo")
    private BigDecimal amount;

    @NotNull(message = "Debe especificar un mes")
    @Range(min = 1, max = 12, message = "El rango válido para los meses es entre 1 y 12")
    private Integer month;

    @NotNull(message = "Debe especificar el año")
    private Integer year;
}
