package com.barboza.finance_api.dto.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class AuthRequest {
    private String name;
    private String email;
    private String password;
}
