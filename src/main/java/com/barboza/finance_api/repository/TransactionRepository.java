package com.barboza.finance_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.barboza.finance_api.entity.Transaction;
import com.barboza.finance_api.entity.User;
import com.barboza.finance_api.projection.summary.MonthSummaryProjection;
import com.barboza.finance_api.projection.summary.YearSummaryProjection;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction>{

    @Query("""
        SELECT t.type AS type, SUM(t.amount) AS total
        FROM Transaction t
        WHERE t.user = :user AND MONTH(t.date) = :month AND YEAR(t.date) = :year
        GROUP BY t.type
    """)
    List<MonthSummaryProjection> getSummaryByUserAndMonthAndYear(User user, Integer month, Integer year);

    @Query("""
        SELECT MONTH(t.date) AS month, t.type AS type, SUM(t.amount) AS total
        FROM Transaction t
        WHERE t.user = :user AND YEAR(t.date) = :year
        GROUP BY MONTH(t.date), t.type
        ORDER BY MONTH(t.date)
    """)
    List<YearSummaryProjection> getSummaryByUserAndYear(User user, Integer year);
}
