package com.example.jobsyserver.features.project.repository;

import com.example.jobsyserver.features.common.enums.ProjectStatus;
import com.example.jobsyserver.features.project.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {

    List<Project> findByClientId(Long clientId);

    List<Project> findByClientIdAndStatus(Long clientId, ProjectStatus status);

    List<Project> findByAssignedFreelancerIdAndStatus(Long freelancerProfileId, ProjectStatus status);

    List<Project> findByAssignedFreelancerId(Long freelancerProfileId);

    @Query("""
        SELECT DISTINCT p
        FROM Project p
        LEFT JOIN FETCH p.category
        LEFT JOIN FETCH p.specialization
        LEFT JOIN FETCH p.client cp
        LEFT JOIN FETCH cp.user
        LEFT JOIN FETCH p.projectSkills ps
        LEFT JOIN FETCH ps.skill
        LEFT JOIN FETCH p.assignedFreelancer af
        LEFT JOIN FETCH af.user
        LEFT JOIN FETCH af.freelancerSkills fs
        LEFT JOIN FETCH fs.skill
        WHERE (:status IS NULL OR p.status = :status)
        """)
    List<Project> findAllWithEverything(@Param("status") ProjectStatus status);
}
