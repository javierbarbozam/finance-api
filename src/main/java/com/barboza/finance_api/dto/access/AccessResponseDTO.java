package com.barboza.finance_api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class AccessResponseDTO {
    private String token;
    private String email;
    private String name;
}
