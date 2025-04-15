package com.example.jobsyserver.repository;

import com.example.jobsyserver.enums.ProjectApplicationStatus;
import com.example.jobsyserver.model.ProjectApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectApplicationRepository extends JpaRepository<ProjectApplication, Long> {
    Long countByProjectIdAndStatus(Long projectId, ProjectApplicationStatus status);
}
