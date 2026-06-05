package com.barboza.finance_api.dto.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class TransactionRequest {

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a 0")
    private BigDecimal amount;

    @NotNull(message = "Debe colocar un detalle de la transacción")
    private String description;
    
    @NotNull(message = "Debe especificar el tipo de transacción")
    private String type;
    
    @NotNull(message = "Debe especificar la categoría de la transacción")
    private Long categoryId;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Debe especificar la fecha de la transacción")
    private LocalDate date;
}
