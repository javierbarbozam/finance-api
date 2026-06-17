package com.barboza.finance_api.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.barboza.finance_api.dto.budget.BudgetRequest;
import com.barboza.finance_api.dto.budget.BudgetResponse;
import com.barboza.finance_api.entity.Budget;
import com.barboza.finance_api.entity.User;
import com.barboza.finance_api.repository.BudgetRepository;

@Service
public class BudgetService {
    
    private final BudgetRepository repository;

    private final UserService userService;

    BudgetService(BudgetRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public BudgetResponse create(BudgetRequest request, String email) {
        var user = userService.findByEmailOrThrow(email);

        var exists = repository.findByUserAndMonthAndYear(user, request.getMonth(), request.getYear());

        if(exists != null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Ya existe un presupuesto para la fecha seleccionada"
            );
        }

        var budget = new Budget(
            request.getAmount(),
            request.getMonth(),
            request.getYear(),
            user
        );

        var saved = repository.save(budget);

        return toBudgetResponse(saved);
    }

    // TODO: Allows to have 2 budgets for same month of the year
    public BudgetResponse update (Long id, BudgetRequest request, String email) {
        var user = userService.findByEmailOrThrow(email);
        var budget = findByIdOrThrow(id);
        var isSameUser = isSameUser(budget, user);

        if(!isSameUser) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "No tiene permiso para modificar el presupuesto"
            );
        }

        if(request.getAmount() != null) {
            budget.setAmount(request.getAmount());
        }
        if(request.getMonth() != null) {
            budget.setMonth(request.getMonth());
        }
        if(request.getYear() != null) {
            budget.setYear(request.getYear());
        }

        var saved = repository.save(budget);
        return toBudgetResponse(saved);
    }

    public void delete(Long id, String email) {
        var user = userService.findByEmailOrThrow(email);
        var budget = findByIdOrThrow(id);
        var isSameUser = isSameUser(budget, user);

        if(!isSameUser) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "No tiene permiso para eliminar este presupuesto"
            );
        }

        repository.delete(budget);
    }

    public List<BudgetResponse> getAllByUser(String email) {
        var user = userService.findByEmailOrThrow(email);
        var budgets = repository.findByUserOrderByMonthAscYearAsc(user);

        return budgets
            .stream()
            .map(this::toBudgetResponse)
            .toList();
    }

    // PRIVATE METHODS

    private Budget findByIdOrThrow(Long id) {
        return repository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Presupuesto no encontrado"
            ));
    }

    private boolean isSameUser(Budget entity, User user) {
        return entity.getUser().getId().equals(user.getId());
    }

    private BudgetResponse toBudgetResponse(Budget entity) {
        return new BudgetResponse(
            entity.getId(),
            entity.getAmount(),
            entity.getMonth(),
            entity.getYear()
        );
    }
}
