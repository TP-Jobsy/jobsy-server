package com.example.jobsyserver.repository;

import com.example.jobsyserver.enums.ApplicationType;
import com.example.jobsyserver.enums.ProjectApplicationStatus;
import com.example.jobsyserver.model.ProjectApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectApplicationRepository extends JpaRepository<ProjectApplication, Long> {
    List<ProjectApplication> findByProjectIdAndApplicationTypeAndStatus(
            Long projectId,
            ApplicationType applicationType,
            ProjectApplicationStatus status
    );
    default List<ProjectApplication> findByProjectIdAndApplicationType(
            Long projectId,
            ApplicationType applicationType
    ) {
        return findByProjectIdAndApplicationTypeAndStatus(projectId, applicationType, null);
    }
    List<ProjectApplication> findByFreelancerIdAndApplicationTypeAndStatus(
            Long freelancerId,
            ApplicationType applicationType,
            ProjectApplicationStatus status
    );
    default List<ProjectApplication> findByFreelancerIdAndApplicationType(
            Long freelancerId,
            ApplicationType applicationType
    ) {
        return findByFreelancerIdAndApplicationTypeAndStatus(freelancerId, applicationType, null);
    }
}
