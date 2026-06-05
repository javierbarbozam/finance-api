package com.barboza.finance_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.barboza.finance_api.dto.transaction.TransactionRequest;
import com.barboza.finance_api.dto.transaction.TransactionResponse;
import com.barboza.finance_api.entity.Category;
import com.barboza.finance_api.entity.Transaction;
import com.barboza.finance_api.entity.User;
import com.barboza.finance_api.enums.TransactionType;
import com.barboza.finance_api.repository.TransactionRepository;

@Service
public class TransactionService {

    @Autowired private TransactionRepository repository;

    @Autowired private UserService userService;

    @Autowired private CategoryService categoryService;

    public TransactionResponse create(TransactionRequest request, String email) {
        
        var user = userService.findByEmailOrThrow(email);
        
        var category = categoryService
            .findById(request.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Categoría inválida."));

        var type = TransactionType.fromString(request.getType());

        if(type == null) {
            throw new RuntimeException("Tipo de transacción inválido.");
        }

        var transaction = new Transaction(
            request.getAmount(),
            request.getDescription(),
            type,
            request.getDate(),
            category,
            user
        );

        repository.save(transaction);

        return toTransactionResponse(transaction);
    }

    public List<TransactionResponse> getTransactionsByUser(String email) {

        var user = userService.findByEmailOrThrow(email);

        var transacciones = repository.findByUserOrderByDateDesc(user);

        return transacciones
            .stream()
            .map(this::toTransactionResponse)
            .toList();
    }

    public TransactionResponse updateTransaction(Long id, TransactionRequest request, String email) {
        
        var user = userService.findByEmailOrThrow(email);
        var transaction = findByIdOrThrow(id);
        var isSameUser = isSameUser(transaction, user);

        if(!isSameUser) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, 
            "No tienes permiso para modificar esta transacción");
        }
        
        if (request.getAmount() != null) {
            transaction.setAmount(request.getAmount());
        }
        if (request.getDescription() != null) {
            transaction.setDescription(request.getDescription());
        }
        if (request.getDate() != null) {
            transaction.setDate(request.getDate());
        }
        if (request.getCategoryId() != null) {
            Category category = categoryService.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            transaction.setCategory(category);
        }
        if(request.getType() != null) {
            var type = TransactionType.fromString(request.getType());

            if(type == null) {
                throw new RuntimeException("Tipo de transacción inválido.");
            }
            transaction.setType(type);
        }

        var saved = repository.save(transaction);
        return toTransactionResponse(saved);
        
    }

    public void deleteTransaction(Long id, String email) {

        var user = userService.findByEmailOrThrow(email);
        var transaction = findByIdOrThrow(id);
        var isSameUser = isSameUser(transaction, user);

        if(!isSameUser) {
            throw new RuntimeException("Usuario inválido");
        }

        repository.delete(transaction);
    }

    // PRIVATE METHODS

    private Transaction findByIdOrThrow(Long id) {
        return repository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Transacción no encontrada."));
    }

    private boolean isSameUser(Transaction legacy, User user) {
        return legacy.getUser().getId().equals(user.getId());
    }

    private TransactionResponse toTransactionResponse(Transaction entity) {
        return new TransactionResponse(
            entity.getAmount(),
            entity.getDescription(),
            entity.getType().name(),
            entity.getCategory() != null ? entity.getCategory().getName() : null,
            entity.getId(),
            entity.getCreatedAt()
        );
    }
}
