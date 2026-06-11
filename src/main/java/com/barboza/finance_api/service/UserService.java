package com.barboza.finance_api.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.barboza.finance_api.entity.User;
import com.barboza.finance_api.repository.UserRepository;

@Service
public class UserService {
    
    private UserRepository repository;

    UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public User findByEmailOrThrow(String email) {
        return repository
            .findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, 
                    "El correo electrónico no está registrado."
                )
            );
    }

    public User save(User user) {
        return repository.save(user);
    }
}
