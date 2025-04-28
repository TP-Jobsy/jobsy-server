package com.example.jobsyserver.repository;

import com.example.jobsyserver.enums.ProjectStatus;
import com.example.jobsyserver.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {
    List<Project> findByStatus(ProjectStatus status);
    List<Project> findByClientId(Long clientId);
    List<Project> findByClientIdAndStatus(Long clientId, ProjectStatus status);
    List<Project> findByAssignedFreelancerIdAndStatus(Long freelancerProfileId, ProjectStatus status);
    List<Project> findByAssignedFreelancerId(Long freelancerProfileId);
}
