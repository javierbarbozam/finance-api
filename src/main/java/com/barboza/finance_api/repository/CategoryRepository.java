package com.barboza.finance_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.barboza.finance_api.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
    
    Optional<Category> findByNameIgnoreCase(String name);

    List<Category> findAllByOrderByNameAsc();
}
