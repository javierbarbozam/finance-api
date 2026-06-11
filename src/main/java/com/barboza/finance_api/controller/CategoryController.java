package com.barboza.finance_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;

import com.barboza.finance_api.dto.category.CategoryRequest;
import com.barboza.finance_api.dto.category.CategoryResponse;
import com.barboza.finance_api.service.CategoryService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class CategoryController {
    private final CategoryService service;

    CategoryController(CategoryService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<CategoryResponse> create(
        @Valid @RequestBody CategoryRequest request,
        @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(service.create(request, email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(
        @PathVariable Long id, 
        @Valid @RequestBody CategoryRequest request,
        @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(service.update(id, request, email));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll(@AuthenticationPrincipal String email) {
        return ResponseEntity.ok(service.getCategoriesByUser(email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @PathVariable Long id,
        @AuthenticationPrincipal String email) {
        service.delete(id, email);
        return ResponseEntity.noContent().build();
    }
}
