package com.barboza.finance_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.barboza.finance_api.entity.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    
}
