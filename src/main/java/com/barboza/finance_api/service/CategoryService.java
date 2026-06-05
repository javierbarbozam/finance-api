package com.barboza.finance_api.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.barboza.finance_api.entity.Category;
import com.barboza.finance_api.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired private CategoryRepository repository;

    public Optional<Category> findById(Long id) {
        return repository.findById(id);
    }
}
