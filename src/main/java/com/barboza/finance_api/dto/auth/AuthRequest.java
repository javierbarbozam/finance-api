package com.barboza.finance_api.dto.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @Valid
public class AuthRequest {
    private String name;

    @NotNull(message = "El correo electrónico es requerido")
    private String email;
    
    @NotNull(message = "La contraseña es requerida")
    private String password;
}
