package com.example.jobsyserver.features.search.specification;

import com.example.jobsyserver.features.portfolio.model.FreelancerPortfolio;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public final class PortfolioSpecification {
    private PortfolioSpecification() {}

    public static Specification<FreelancerPortfolio> textSearchTitle(String term) {
        return (root, query, cb) -> {
            if (StringUtils.isBlank(term)) return cb.conjunction();
            return cb.like(cb.lower(root.get("title")), "%" + term.toLowerCase() + "%");
        };
    }

    public static Specification<FreelancerPortfolio> textSearchFreelancer(String name) {
        return (root, query, cb) -> {
            if (StringUtils.isBlank(name)) return cb.conjunction();
            String pattern = "%" + name.toLowerCase() + "%";
            var join = root.join("freelancer").join("user");
            return cb.or(
                    cb.like(cb.lower(join.get("firstName")), pattern),
                    cb.like(cb.lower(join.get("lastName")), pattern)
            );
        };
    }

    public static Specification<FreelancerPortfolio> createdBetween(LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) -> {
            if (from == null && to == null) {
                return cb.conjunction();
            }
            if (from != null && to != null) {
                return cb.between(root.get("createdAt"), from, to);
            }
            if (from != null) {
                return cb.greaterThanOrEqualTo(root.get("createdAt"), from);
            }
            return cb.lessThanOrEqualTo(root.get("createdAt"), to);
        };
    }
}
