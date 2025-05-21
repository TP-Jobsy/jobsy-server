package com.example.jobsyserver.features.project.repository;

import com.example.jobsyserver.features.common.enums.ProjectStatus;
import com.example.jobsyserver.features.project.model.Project;
import com.example.jobsyserver.features.project.projection.ProjectListItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.LOAD;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {
    @EntityGraph(value = "Project.full", type = LOAD)
    List<Project> findByClientId(Long clientId);
    @EntityGraph(value = "Project.full", type = LOAD)
    List<Project> findByClientIdAndStatus(Long clientId, ProjectStatus status);
    @EntityGraph(value = "Project.full", type = LOAD)
    List<Project> findByAssignedFreelancerIdAndStatus(Long freelancerProfileId, ProjectStatus status);
    @EntityGraph(value = "Project.full", type = LOAD)
    List<Project> findByAssignedFreelancerId(Long freelancerProfileId);

    @EntityGraph(value = "Project.full", type = LOAD)
    @Query("SELECT p FROM Project p")
    List<Project> findAllWithGraph();

    @EntityGraph(value = "Project.full", type = LOAD)
    @Query("SELECT p FROM Project p WHERE p.status = :status")
    List<Project> findAllWithGraphByStatus(@Param("status") ProjectStatus status);

    @Override
    @EntityGraph(value = "Project.full", type = LOAD)
    Optional<Project> findById(Long id);

    Page<ProjectListItem> findAllProjectedBy(Pageable pageable);

    Page<ProjectListItem> findAllProjectedByStatus(ProjectStatus status, Pageable pageable);

}
