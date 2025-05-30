package com.example.jobsyserver.features.search.specification;

import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.portfolio.model.FreelancerPortfolio;
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

    public static Specification<Project> hasAnySkillProject(List<Long> skillIds) {
        return ProjectSpecification.hasAnySkill(skillIds);
    }

    public static Specification<Project> textSearchProject(String term) {
        return ProjectSpecification.textSearchProject(term);
    }

    public static Specification<Project> hasStatus(String status) {
        return ProjectSpecification.hasStatus(status);
    }

    public static Specification<Project> textSearchClient(String clientName) {
        return ProjectSpecification.textSearchClient(clientName);
    }

    public static Specification<FreelancerPortfolio> textSearchTitle(String term) {
        return PortfolioSpecification.textSearchTitle(term);
    }

    public static Specification<FreelancerPortfolio> textSearchFreelancer(String name) {
        return PortfolioSpecification.textSearchFreelancer(name);
    }
}