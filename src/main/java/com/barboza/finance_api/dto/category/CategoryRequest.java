package com.barboza.finance_api.dto.category;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Valid
public class CategoryRequest {

    @NotBlank(message = "Debe especificar un nombre")
    private String name;
    private String icon;
}
