package com.example.jobsyserver.features.project.repository;

import com.example.jobsyserver.features.common.enums.ApplicationType;
import com.example.jobsyserver.features.common.enums.ProjectApplicationStatus;
import com.example.jobsyserver.features.project.model.ProjectApplication;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectApplicationRepository extends JpaRepository<ProjectApplication, Long> {
    @EntityGraph(attributePaths = {
            "freelancer",
            "freelancer.user",
            "freelancer.skills"
    })
    List<ProjectApplication> findByProjectIdAndApplicationTypeAndStatus(
            Long projectId,
            ApplicationType applicationType,
            ProjectApplicationStatus status
    );

    @EntityGraph(attributePaths = {
            "freelancer",
            "freelancer.user",
            "freelancer.skills"
    })
    List<ProjectApplication> findByProjectIdAndApplicationType(
            Long projectId,
            ApplicationType applicationType
    );

    @EntityGraph(attributePaths = {
            "freelancer",
            "freelancer.user",
            "freelancer.skills"
    })
    List<ProjectApplication> findByFreelancerIdAndApplicationTypeAndStatus(
            Long freelancerId,
            ApplicationType applicationType,
            ProjectApplicationStatus status
    );

    @EntityGraph(attributePaths = {
            "freelancer",
            "freelancer.user",
            "freelancer.skills"
    })
    List<ProjectApplication> findByFreelancerIdAndApplicationType(
            Long freelancerId,
            ApplicationType applicationType
    );

    @EntityGraph(attributePaths = {
            "freelancer",
            "freelancer.user",
            "freelancer.skills",
            "project"
    })
    Optional<ProjectApplication> findByProjectIdAndFreelancerIdAndApplicationType(
            Long projectId,
            Long freelancerId,
            ApplicationType applicationType
    );
}
