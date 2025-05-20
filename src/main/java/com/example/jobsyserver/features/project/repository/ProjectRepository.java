package com.example.jobsyserver.features.project.repository;

import com.example.jobsyserver.features.common.enums.ProjectStatus;
import com.example.jobsyserver.features.project.model.Project;
import org.springframework.data.jpa.repository.EntityGraph;
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


    @EntityGraph(value = "Project.full", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT DISTINCT p FROM Project p")
    List<Project> findAllWithGraph();

    @EntityGraph(value = "Project.full", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT DISTINCT p FROM Project p WHERE p.status = :status")
    List<Project> findAllWithGraphByStatus(@Param("status") ProjectStatus status);
}
