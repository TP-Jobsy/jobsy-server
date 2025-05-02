package com.example.jobsyserver.specification;

import com.example.jobsyserver.model.Skill;
import com.example.jobsyserver.model.ProjectSkill;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class SkillSpecifications {

    public static Specification<Skill> popularInProjects() {
        return (root, query, cb) -> {
            query.distinct(true);
            Join<Skill, ProjectSkill> join = root.join("projectSkills", JoinType.LEFT);
            query.groupBy(root.get("id"));
            query.orderBy(cb.desc(cb.count(join)));
            return cb.conjunction();
        };
    }

    public static Specification<Skill> nameContains(String term) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + term.toLowerCase() + "%");
    }
}