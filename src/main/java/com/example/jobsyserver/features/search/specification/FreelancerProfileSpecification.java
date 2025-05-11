package com.example.jobsyserver.features.search.specification;

import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.freelancer.model.FreelancerSkill;
import com.example.jobsyserver.features.user.model.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public final class FreelancerProfileSpecification {
    private FreelancerProfileSpecification() {}

    public static Specification<FreelancerProfile> hasAnySkill(List<Long> skillIds) {
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
            Join<FreelancerProfile, FreelancerSkill> skills =
                    root.join("freelancerSkills", JoinType.INNER);
            return skills.get("skill").get("id").in(skillIds);
        };
    }

    public static Specification<FreelancerProfile> textSearch(String term) {
        return (root, query, cb) -> {
            if (StringUtils.isBlank(term)) {
                return cb.conjunction();
            }
            if (query == null) {
                return cb.conjunction();
            }
            Class<?> resultType = query.getResultType();
            if (resultType != null && !Long.class.equals(resultType)) {
                query.distinct(true);
            }
            String pattern = "%" + term.toLowerCase() + "%";
            Join<FreelancerProfile, User> user = root.join("user", JoinType.INNER);
            Predicate byFirst   = cb.like(cb.lower(user.get("firstName")), pattern);
            Predicate byLast    = cb.like(cb.lower(user.get("lastName")),  pattern);
            Predicate byAbout   = cb.like(cb.lower(root.get("aboutMe")),   pattern);
            Predicate byCity    = cb.like(cb.lower(root.get("city")),      pattern);
            Predicate byCountry = cb.like(cb.lower(root.get("country")),   pattern);
            return cb.or(byFirst, byLast, byAbout, byCity, byCountry);
        };
    }
}