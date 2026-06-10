package com.barboza.finance_api.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.barboza.finance_api.dto.category.CategoryRequest;
import com.barboza.finance_api.dto.category.CategoryResponse;
import com.barboza.finance_api.entity.Category;
import com.barboza.finance_api.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired private CategoryRepository repository;

    @Autowired private UserService userService;

    public Optional<Category> findById(Long id) {
        return repository.findById(id);
    }

    public CategoryResponse create(CategoryRequest request, String email) {
        userService.findByEmailOrThrow(email);

        var optional = repository.findByNameIgnoreCase(request.getName());

        if(optional.isPresent()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "La categoría ya existe"
            );
        }
    
        var category = new Category(
            request.getName(),
            request.getIcon()
        );

        repository.save(category);

        return toCategoryResponse(category);
    }

    public CategoryResponse update(Long id, CategoryRequest request, String email) {

        userService.findByEmailOrThrow(email);
        var category = findByIdOrThrow(id);

        if(request.getName() != null) {
            category.setName(request.getName());
        }

        if(request.getIcon() != null) {
            category.setIcon(request.getIcon());
        }

        var saved = repository.save(category);

        return toCategoryResponse(saved);
    }

    public void delete(Long id, String email) {
        userService.findByEmailOrThrow(email);
        var category = findByIdOrThrow(id);

        repository.delete(category);
    }

    public List<CategoryResponse> getAll(String email) {
        userService.findByEmailOrThrow(email);

        var categories = repository.findAllByOrderByNameAsc();

        return categories
            .stream()
            .map(this::toCategoryResponse)
            .toList();
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

    // PRIVATE METHODS

    private CategoryResponse toCategoryResponse(Category entity) {
        return new CategoryResponse(
            entity.getId(),
            entity.getName(),
            entity.getColor(),
            entity.getIcon()
        );
    }
}
