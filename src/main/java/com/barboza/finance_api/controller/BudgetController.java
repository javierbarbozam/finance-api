package com.barboza.finance_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barboza.finance_api.dto.budget.BudgetRequest;
import com.barboza.finance_api.dto.budget.BudgetResponse;
import com.barboza.finance_api.service.BudgetService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/budget")
public class BudgetController {
    private final BudgetService service;

    BudgetController(BudgetService service) {
        this.service = service;
    }
    
    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getAllByUser(@AuthenticationPrincipal String email) {
        return ResponseEntity.ok(service.getAllByUser(email));
    }

    @PostMapping("/create")
    public ResponseEntity<BudgetResponse> create(
        @Valid @RequestBody BudgetRequest request,
        @AuthenticationPrincipal String email) {        
        return ResponseEntity.ok(service.create(request, email));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponse> update(
        @PathVariable Long id, 
        @Valid @RequestBody BudgetRequest request,
        @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(service.update(id, request, email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @PathVariable Long id,
        @AuthenticationPrincipal String email) {
        service.delete(id, email);
        return ResponseEntity.noContent().build();
    }
}
