package com.barboza.finance_api.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.barboza.finance_api.dto.auth.AuthRequest;
import com.barboza.finance_api.dto.auth.AuthResponse;
import com.barboza.finance_api.entity.User;
import com.barboza.finance_api.enums.Role;
import com.barboza.finance_api.security.JwtUtils;

@Service
public class AuthService {

    private UserService userService;

    private PasswordEncoder passwordEncoder;

    private JwtUtils jwtUtils;

    public AuthResponse register(AuthRequest request) {

        var optional = userService.findByEmail(request.getEmail());

        if(optional.isPresent()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, 
                "El correo electrónico ya está registrado."
            );
        }

        // Encriptar contraseña
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        var user = new User(
            request.getName(),
            request.getEmail(),
            hashedPassword,
            Role.USER
        );

        userService.save(user);

        // Generar token con el email para que Spring pueda validarlo en requests futuros
        var token = jwtUtils.generateToken(user.getEmail());
        return new AuthResponse(token);
    }

    public AuthResponse login(AuthRequest request) {
        var user = userService.findByEmailOrThrow(request.getEmail());

        // Comparar contraseña recibida con el hash en base de datos
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, 
                "La contraseña es incorrecta."
            );
        }

        var token = jwtUtils.generateToken(user.getEmail());
        return new AuthResponse(token);
    } 
    
}
