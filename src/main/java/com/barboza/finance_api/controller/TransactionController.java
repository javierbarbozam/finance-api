package com.barboza.finance_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;

import com.barboza.finance_api.dto.transaction.TransactionRequest;
import com.barboza.finance_api.dto.transaction.TransactionResponse;
import com.barboza.finance_api.service.TransactionService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/transaction")
public class TransactionController {
    
    @Autowired private TransactionService service;

    @PostMapping("/create")
    public ResponseEntity<TransactionResponse> create(
        @Valid @RequestBody TransactionRequest request, 
        @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(service.create(request, email));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAll(@AuthenticationPrincipal String email) {
        return ResponseEntity.ok(service.getTransactionsByUser(email));
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
