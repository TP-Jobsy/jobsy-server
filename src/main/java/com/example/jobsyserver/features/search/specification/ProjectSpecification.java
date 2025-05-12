package com.example.jobsyserver.features.search.specification;

import com.example.jobsyserver.features.project.model.Project;
import com.example.jobsyserver.features.project.model.ProjectSkill;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public final class ProjectSpecification {
    private ProjectSpecification() {}

    public static Specification<Project> hasAnySkill(List<Long> skillIds) {
        return (root, query, cb) -> {
            if (skillIds == null || skillIds.isEmpty()) {
                return cb.conjunction();
            }
            if (query == null) {
                return cb.conjunction();
            }
            Class<?> resultType = query.getResultType();
            if (resultType != null && !Long.class.equals(resultType)) {
                query.distinct(true);
            }
            Join<Project, ProjectSkill> skills =
                    root.join("projectSkills", JoinType.INNER);
            return skills.get("skill").get("id").in(skillIds);
        };
    }

    public static Specification<Project> textSearch(String term) {
        return (root, query, cb) -> {
            if (StringUtils.isBlank(term)) {
                return cb.conjunction();
            }
            if (query != null) {
                Class<?> resultType = query.getResultType();
                if (resultType != null && !Long.class.equals(resultType)) {
                    query.distinct(true);
                }
            }
            String pattern = "%" + term.toLowerCase() + "%";
            Predicate byTitle = cb.like(cb.lower(root.get("title")), pattern);
            Predicate byDesc  = cb.like(cb.lower(root.get("description")), pattern);
            return cb.or(byTitle, byDesc);
        };
    }
}