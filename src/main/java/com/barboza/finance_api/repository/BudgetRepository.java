package com.barboza.finance_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.barboza.finance_api.entity.Budget;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    
}
