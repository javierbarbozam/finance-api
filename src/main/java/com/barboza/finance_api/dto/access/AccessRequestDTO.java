package com.barboza.finance_api.dto.access;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class AccessRequestDTO {
    private String name;
    private String email;
    private String password;
}
