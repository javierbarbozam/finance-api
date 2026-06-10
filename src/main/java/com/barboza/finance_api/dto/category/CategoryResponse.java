package com.barboza.finance_api.dto.category;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Valid
public class CategoryResponse {
    private Long id;
    private String name;
    private String color;
    private String icon;
}
