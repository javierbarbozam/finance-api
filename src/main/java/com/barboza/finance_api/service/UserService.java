package com.barboza.finance_api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.barboza.finance_api.entity.User;
import com.barboza.finance_api.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired private UserRepository repository;

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public User findByEmailOrThrow(String email) {
        return repository
            .findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Correo electrónico inválido."));
    }

    public User save(User user) {
        return repository.save(user);
    }
}
