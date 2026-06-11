package com.barboza.finance_api.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.barboza.finance_api.dto.category.CategoryRequest;
import com.barboza.finance_api.dto.category.CategoryResponse;
import com.barboza.finance_api.entity.Category;
import com.barboza.finance_api.entity.User;
import com.barboza.finance_api.repository.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    private final UserService userService;

    CategoryService(CategoryRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public CategoryResponse create(CategoryRequest request, String email) {
        var user = userService.findByEmailOrThrow(email);
        var optional = repository.findByNameIgnoreCase(request.getName());

        if(optional.isPresent()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "La categoría ya existe"
            );
        }
    
        var category = new Category(
            request.getName(),
            request.getIcon(),
            user
        );

        repository.save(category);

        return toCategoryResponse(category);
    }

    public CategoryResponse update(Long id, CategoryRequest request, String email) {

        var user = userService.findByEmailOrThrow(email);
        var category = findByIdOrThrow(id);
        var isSameUser = isSameUser(category, user);

        if(!isSameUser) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "No tiene permiso para modificar esta categoría"
            );
        }

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
        var user = userService.findByEmailOrThrow(email);
        var category = findByIdOrThrow(id);
        var isSameUser = isSameUser(category, user);

        if(!isSameUser) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, 
                "No tiene permiso para eliminar esta transacción");
        }

        repository.delete(category);
    }

    public List<CategoryResponse> getCategoriesByUser(String email) {
        var user = userService.findByEmailOrThrow(email);
        var categories = repository.findByUserOrderByNameAsc(user);

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

    private boolean isSameUser(Category legacy, User user) {
        return legacy.getUser().getId().equals(user.getId());
    }

    private CategoryResponse toCategoryResponse(Category entity) {
        return new CategoryResponse(
            entity.getId(),
            entity.getName(),
            entity.getIcon()
        );
    }
}
