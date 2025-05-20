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
        select distinct p
        from Project p
        left join fetch p.category
        left join fetch p.specialization
        left join fetch p.client cp
        left join fetch cp.user
        left join fetch p.projectSkills ps
        left join fetch ps.skill
        left join fetch p.assignedFreelancer af
        left join fetch af.user
        """)
    List<Project> findAllWithSkillsAndFreelancer();

    @Query("""
        select distinct p
        from Project p
        left join fetch p.category
        left join fetch p.specialization
        left join fetch p.client cp
        left join fetch cp.user
        left join fetch p.projectSkills ps
        left join fetch ps.skill
        left join fetch p.assignedFreelancer af
        left join fetch af.user
        where p.status = :status
        """)
    List<Project> findAllWithSkillsAndFreelancerByStatus(@Param("status") ProjectStatus status);
}
