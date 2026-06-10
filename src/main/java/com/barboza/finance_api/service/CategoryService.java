package com.barboza.finance_api.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.barboza.finance_api.entity.Category;
import com.barboza.finance_api.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired private CategoryRepository repository;

    public Optional<Category> findById(Long id) {
        return repository.findById(id);
    }

    public Category findByIdOrThrow(Long id) {
        return repository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, 
                    "La categoría no se encuentra."
                )
            );
    }
}
