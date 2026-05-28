package com.barboza.finance_api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class AuthResponse {
    private String token;
    private String email;
    private String name;

    public AuthResponse(String token) {
        this.token = token;
    }
}
