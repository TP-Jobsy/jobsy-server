package com.example.jobsyserver.features.user.specification;

import com.example.jobsyserver.features.common.enums.UserRole;
import com.example.jobsyserver.features.user.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class UserSpecifications {

    public static Specification<User> hasRole(UserRole role) {
        return (root, query, cb) -> {
            if (role == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("role"), role);
        };
    }

    public static Specification<User> textSearch(String term) {
        return (root, query, cb) -> {
            if (term == null || term.isBlank()) {
                return cb.conjunction();
            }
            String p = "%" + term.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("firstName")), p),
                    cb.like(cb.lower(root.get("lastName")), p)
            );
        };
    }

    public static Specification<User> registeredBetween(LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) -> {
            if (from == null && to == null) {
                return cb.conjunction();
            } else if (from != null && to != null) {
                return cb.between(root.get("createdAt"), from, to);
            } else if (from != null) {
                return cb.greaterThanOrEqualTo(root.get("createdAt"), from);
            } else {
                return cb.lessThanOrEqualTo(root.get("createdAt"), to);
            }
        };
    }
}