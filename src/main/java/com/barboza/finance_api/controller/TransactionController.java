package com.barboza.finance_api.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;

import com.barboza.finance_api.dto.transaction.TransactionRequest;
import com.barboza.finance_api.dto.transaction.TransactionResponse;
import com.barboza.finance_api.enums.TransactionType;
import com.barboza.finance_api.service.TransactionService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/transaction")
public class TransactionController {
    
    private final TransactionService service;

    TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<TransactionResponse> create(
        @Valid @RequestBody TransactionRequest request, 
        @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(service.create(request, email));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllBy(
        @AuthenticationPrincipal String email,
        @RequestParam(required = false) TransactionType type,
        @RequestParam(required = false) Long categoryId,
        @RequestParam(required = false) LocalDate startDate,
        @RequestParam(required = false) LocalDate endDate
    ) {
        return ResponseEntity.ok(service.getAllBy(email, type, categoryId, endDate, endDate));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> update(
        @PathVariable Long id,
        @Valid @RequestBody TransactionRequest request,
        @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(service.updateTransaction(id, request, email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @PathVariable Long id,
        @AuthenticationPrincipal String email) {
        service.deleteTransaction(id, email);
        return ResponseEntity.noContent().build();
    }
}
