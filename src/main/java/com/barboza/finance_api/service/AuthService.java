package com.barboza.finance_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.barboza.finance_api.dto.auth.AuthRequest;
import com.barboza.finance_api.dto.auth.AuthResponse;
import com.barboza.finance_api.entity.User;
import com.barboza.finance_api.enums.Role;
import com.barboza.finance_api.repository.UserRepository;
import com.barboza.finance_api.security.JwtUtils;

@Service
public class AuthService {

    @Autowired private UserRepository userRepository;

    @Autowired private PasswordEncoder passwordEncoder;

    @Autowired private JwtUtils jwtUtils;

    public AuthResponse register(AuthRequest request) {

        var optional = userRepository.findByEmail(request.getEmail());

        if(optional.isPresent()) {
            throw new RuntimeException("El correo electrónico ya está registrado.");
        }

        // Encriptar contraseña
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        var user = new User(
            request.getName(),
            request.getEmail(),
            hashedPassword,
            Role.USER
        );

        userRepository.save(user);

        // Generar token con el email para que Spring pueda validarlo en requests futuros
        var token = jwtUtils.generateToken(user.getEmail());
        return new AuthResponse(token);
    }

    public AuthResponse login(AuthRequest request) {
        var user = userRepository
            .findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Correo electrónico inválido."));

        // Comparar contraseña recibida con el hash en base de datos
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña inválida");
        }

        var token = jwtUtils.generateToken(user.getEmail());
        return new AuthResponse(token);
    } 
    
}
