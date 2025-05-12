package com.example.jobsyserver.features.search.specification;

import com.example.jobsyserver.features.skill.model.Skill;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class SkillSpecifications {

    public static Specification<Skill> randomOrder() {
        return (Root<Skill> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            query.orderBy(cb.asc(cb.function("random", Double.class)));
            return cb.conjunction();
        };
    }

    public static Specification<Skill> nameContains(String term) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + term.toLowerCase() + "%");
    }
}