package com.example.jobsyserver.features.search.specification;

import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.project.model.Project;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SearchSpecifications {

    public static Specification<FreelancerProfile> hasAnySkill(List<Long> skillIds) {
        return FreelancerProfileSpecification.hasAnySkill(skillIds);
    }

    public static Specification<FreelancerProfile> textSearchFreelancer(String term) {
        return FreelancerProfileSpecification.textSearch(term);
    }

    public static Specification<Project> hasAnySkillProject(List<Long> skillIds) {
        return ProjectSpecification.hasAnySkill(skillIds);
    }

    public static Specification<Project> textSearchProject(String term) {
        return ProjectSpecification.textSearch(term);
    }
}