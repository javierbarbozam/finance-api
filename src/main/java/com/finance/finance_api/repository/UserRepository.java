package com.finance.finance_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finance.finance_api.model.User;

public interface UserRepository extends JpaRepository<Long, User> {
    Optional<User> findByEmail (String email);
}
