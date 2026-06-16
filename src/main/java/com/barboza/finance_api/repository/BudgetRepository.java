package com.barboza.finance_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.barboza.finance_api.entity.Budget;
import com.barboza.finance_api.entity.User;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    
    List<Budget> findByUserOrderByMonthAscYearAsc(User user);

    Budget findByUserAndMonthAndYear(User user, Integer month, Integer year);
}
