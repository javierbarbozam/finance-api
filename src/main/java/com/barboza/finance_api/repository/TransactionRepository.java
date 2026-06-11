package com.barboza.finance_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.barboza.finance_api.entity.Transaction;
import com.barboza.finance_api.entity.User;

import java.util.List;


public interface TransactionRepository extends JpaRepository<Transaction, Long>{

    public List<Transaction> findByUserOrderByDateDesc(User user);
}
