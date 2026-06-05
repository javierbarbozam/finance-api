package com.barboza.finance_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.barboza.finance_api.entity.Transaction;
import com.barboza.finance_api.entity.User;

import java.util.List;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{

    public List<Transaction> findByUserOrderByDateDesc(User user);
}
