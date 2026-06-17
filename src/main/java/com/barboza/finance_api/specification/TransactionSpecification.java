package com.barboza.finance_api.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.barboza.finance_api.entity.Transaction;
import com.barboza.finance_api.entity.User;
import com.barboza.finance_api.enums.TransactionType;

/* IMPORTANT TO KNOW

    Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb);

    1. Root: Represents the entity for the SQL query
    2. Query: Represents the SQL query that is being built. At the moment it is not used because there is no need
    to modify it. But some of the ways it can be used are: query.distinct(true) or groupBy.
    3. CriteriaBuilder(CB): Builds all conditions(Predicate). Example: cb.equal(a,b) in SQL represents A = B
*/
public class TransactionSpecification {
    public static Specification<Transaction> hasUser(User user) {
        return (root, query, cb) -> cb.equal(root.get("user"), user);
    }

    public static Specification<Transaction> hasType(TransactionType type) {
        return (root, query, cb) -> {
            if (type == null) return cb.conjunction();
            return cb.equal(root.get("type"), type); // returns WHERE transaction.user_id = :userId
        };
    }

    public static Specification<Transaction> hasCategory(Long categoryId) {
        return (root, query, cb) -> {
            if (categoryId == null) return cb.conjunction();
            return cb.equal(root.get("category").get("id"), categoryId);
        };
    }

    public static Specification<Transaction> dateBetween(LocalDate start, LocalDate end) {
        return (root, query, cb) -> {
            if (start == null && end == null) return cb.conjunction();
            if (start != null && end != null) return cb.between(root.get("date"), start, end);
            if (start != null) return cb.greaterThanOrEqualTo(root.get("date"), start);
            return cb.lessThanOrEqualTo(root.get("date"), end);
        };
    }
}
