package com.example.jobsyserver.features.project.repository;

import com.example.jobsyserver.features.common.enums.ProjectStatus;
import com.example.jobsyserver.features.project.model.Project;
import com.example.jobsyserver.features.project.projection.ProjectAdminListItem;
import com.example.jobsyserver.features.project.projection.ProjectListItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

    @Query("""
                SELECT
                    p.id                    AS id,
                    p.title                 AS title,
                    p.fixedPrice            AS fixedPrice,
                    p.projectComplexity     AS projectComplexity,
                    p.projectDuration       AS projectDuration,
                    p.status                AS status,
                    p.createdAt             AS createdAt,
                    c.companyName           AS clientCompanyName,
                    c.city                  AS clientCity,
                    c.country               AS clientCountry,
                    u.firstName             AS assignedFreelancerFirstName,
                    u.lastName              AS assignedFreelancerLastName
                FROM Project p
                    JOIN p.client c
                    JOIN c.user cu
                    LEFT JOIN p.assignedFreelancer af
                    LEFT JOIN af.user u
            """
    )
    Page<ProjectListItem> findAllProjectedBy(Pageable pageable);

    @Query("""
                SELECT
                    p.id                        AS id,
                    p.title                     AS title,
                    p.createdAt                 AS createdAt,
                    p.status                    AS status,
                    cu.firstName               AS clientFirstName,
                    cu.lastName                AS clientLastName
                FROM Project p
                    JOIN p.client c
                    JOIN c.user cu
            """)
    Page<ProjectAdminListItem> findAllProjectedByAdmin(Pageable pageable);

    @Query("""
                SELECT
                    p.id                        AS id,
                    p.title                     AS title,
                    p.fixedPrice                AS fixedPrice,
                    p.projectComplexity         AS projectComplexity,
                    p.projectDuration           AS projectDuration,
                    p.status                    AS status,
                    p.createdAt                 AS createdAt,
                    c.companyName               AS clientCompanyName,
                    c.city                      AS clientCity,
                    c.country                   AS clientCountry,
                    u.firstName                 AS assignedFreelancerFirstName,
                    u.lastName                  AS assignedFreelancerLastName
                FROM Project p
                    JOIN p.client c
                    JOIN c.user cu
                    LEFT JOIN p.assignedFreelancer af
                    LEFT JOIN af.user u
                WHERE p.status = :status
            """
    )
    Page<ProjectListItem> findAllProjectedByStatus(
            @Param("status") ProjectStatus status,
            Pageable pageable
    );

    @Query("""
                SELECT
                    p.id                        AS id,
                    p.title                     AS title,
                    p.fixedPrice                AS fixedPrice,
                    p.projectComplexity         AS projectComplexity,
                    p.projectDuration           AS projectDuration,
                    p.status                    AS status,
                    p.createdAt                 AS createdAt,
                    c.companyName               AS clientCompanyName,
                    c.city                      AS clientCity,
                    c.country                   AS clientCountry,
                    u.firstName                 AS assignedFreelancerFirstName,
                    u.lastName                  AS assignedFreelancerLastName
                FROM Project p
                    JOIN p.client c
                    JOIN c.user cu
                    LEFT JOIN p.assignedFreelancer af
                    LEFT JOIN af.user u
                WHERE
                    LOWER(p.title) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(p.description) LIKE LOWER(CONCAT('%', :term, '%'))
            """
    )
    Page<ProjectListItem> findByTerm(
            @Param("term") String term,
            Pageable pageable
    );

    @Query("""
                SELECT
                    p.id                        AS id,
                    p.title                     AS title,
                    p.fixedPrice                AS fixedPrice,
                    p.projectComplexity         AS projectComplexity,
                    p.projectDuration           AS projectDuration,
                    p.status                    AS status,
                    p.createdAt                 AS createdAt,
                    c.companyName               AS clientCompanyName,
                    c.city                      AS clientCity,
                    c.country                   AS clientCountry,
                    u.firstName                 AS assignedFreelancerFirstName,
                    u.lastName                  AS assignedFreelancerLastName
                FROM Project p
                    JOIN p.client c
                    JOIN c.user cu
                    LEFT JOIN p.assignedFreelancer af
                    LEFT JOIN af.user u
                    JOIN p.skills s
                WHERE s.id IN :skillIds
                GROUP BY
                    p.id, p.title, p.fixedPrice,
                    p.projectComplexity, p.projectDuration, p.status, p.createdAt,
                    c.companyName, c.city, c.country,
                    u.firstName, u.lastName
            """
    )
    Page<ProjectListItem> findBySkills(
            @Param("skillIds") List<Long> skillIds,
            Pageable pageable
    );

    @Query("""
                SELECT
                    p.id                        AS id,
                    p.title                     AS title,
                    p.fixedPrice                AS fixedPrice,
                    p.projectComplexity         AS projectComplexity,
                    p.projectDuration           AS projectDuration,
                    p.status                    AS status,
                    p.createdAt                 AS createdAt,
                    c.companyName               AS clientCompanyName,
                    c.city                      AS clientCity,
                    c.country                   AS clientCountry,
                    u.firstName                 AS assignedFreelancerFirstName,
                    u.lastName                  AS assignedFreelancerLastName
                FROM Project p
                    JOIN p.client c
                    JOIN c.user cu
                    LEFT JOIN p.assignedFreelancer af
                    LEFT JOIN af.user u
                    JOIN p.skills s
                WHERE s.id IN :skillIds
                  AND (
                    LOWER(p.title) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(p.description) LIKE LOWER(CONCAT('%', :term, '%'))
                  )
                GROUP BY
                    p.id, p.title, p.fixedPrice,
                    p.projectComplexity, p.projectDuration, p.status, p.createdAt,
                    c.companyName, c.city, c.country,
                    u.firstName, u.lastName
            """
    )
    Page<ProjectListItem> findBySkillsAndTerm(
            @Param("skillIds") List<Long> skillIds,
            @Param("term") String term,
            Pageable pageable
    );


    @Query("""
                SELECT
                    p.id                        AS id,
                    p.title                     AS title,
                    p.fixedPrice                AS fixedPrice,
                    p.projectComplexity         AS projectComplexity,
                    p.projectDuration           AS projectDuration,
                    p.status                    AS status,
                    p.createdAt                 AS createdAt,
                    c.companyName               AS clientCompanyName,
                    c.city                      AS clientCity,
                    c.country                   AS clientCountry,
                    u.firstName                 AS assignedFreelancerFirstName,
                    u.lastName                  AS assignedFreelancerLastName
                FROM Project p
                    JOIN p.client c
                    JOIN c.user cu
                    LEFT JOIN p.assignedFreelancer af
                    LEFT JOIN af.user u
                WHERE
                    LOWER(p.title) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(p.description) LIKE LOWER(CONCAT('%', :term, '%'))
                    AND p.status = :status
            """
    )
    Page<ProjectListItem> findByTermStatus(
            @Param("term") String term,
            Pageable pageable,
            @Param("status") ProjectStatus status

    );

    @Query("""
                SELECT
                    p.id                        AS id,
                    p.title                     AS title,
                    p.fixedPrice                AS fixedPrice,
                    p.projectComplexity         AS projectComplexity,
                    p.projectDuration           AS projectDuration,
                    p.status                    AS status,
                    p.createdAt                 AS createdAt,
                    c.companyName               AS clientCompanyName,
                    c.city                      AS clientCity,
                    c.country                   AS clientCountry,
                    u.firstName                 AS assignedFreelancerFirstName,
                    u.lastName                  AS assignedFreelancerLastName
                FROM Project p
                    JOIN p.client c
                    JOIN c.user cu
                    LEFT JOIN p.assignedFreelancer af
                    LEFT JOIN af.user u
                    JOIN p.skills s
                WHERE s.id IN :skillIds AND p.status = :status
                GROUP BY
                    p.id, p.title, p.fixedPrice,
                    p.projectComplexity, p.projectDuration, p.status, p.createdAt,
                    c.companyName, c.city, c.country,
                    u.firstName, u.lastName
            """
    )
    Page<ProjectListItem> findBySkillsStatus(
            @Param("skillIds") List<Long> skillIds,
            Pageable pageable,
            @Param("status") ProjectStatus status
    );

    @Query("""
                SELECT
                    p.id                        AS id,
                    p.title                     AS title,
                    p.fixedPrice                AS fixedPrice,
                    p.projectComplexity         AS projectComplexity,
                    p.projectDuration           AS projectDuration,
                    p.status                    AS status,
                    p.createdAt                 AS createdAt,
                    c.companyName               AS clientCompanyName,
                    c.city                      AS clientCity,
                    c.country                   AS clientCountry,
                    u.firstName                 AS assignedFreelancerFirstName,
                    u.lastName                  AS assignedFreelancerLastName
                FROM Project p
                    JOIN p.client c
                    JOIN c.user cu
                    LEFT JOIN p.assignedFreelancer af
                    LEFT JOIN af.user u
                    JOIN p.skills s
                WHERE s.id IN :skillIds
                  AND (
                    LOWER(p.title) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(p.description) LIKE LOWER(CONCAT('%', :term, '%'))
                  AND p.status = :status
                  )
                GROUP BY
                    p.id, p.title, p.fixedPrice,
                    p.projectComplexity, p.projectDuration, p.status, p.createdAt,
                    c.companyName, c.city, c.country,
                    u.firstName, u.lastName
            """
    )
    Page<ProjectListItem> findBySkillsAndTermStatus(
            @Param("skillIds") List<Long> skillIds,
            @Param("term") String term,
            Pageable pageable,
            @Param("status") ProjectStatus status
    );

    default Page<ProjectAdminListItem> findAllProjected(
            Specification<Project> spec, Pageable pg
    ) {
        return findAll(spec, pg).map(p -> new ProjectAdminListItem() {
            @Override
            public Long getId() {
                return p.getId();
            }

            @Override
            public String getTitle() {
                return p.getTitle();
            }

            @Override
            public LocalDateTime getCreatedAt() {
                return p.getCreatedAt();
            }

            @Override
            public String getStatus() {
                return p.getStatus().name();
            }

            @Override
            public String getClientFirstName() {
                return p.getClient().getUser().getFirstName();
            }

            @Override
            public String getClientLastName() {
                return p.getClient().getUser().getLastName();
            }
        });
    }

}
