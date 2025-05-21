package com.example.jobsyserver.features.freelancer.repository;

import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.freelancer.projection.FreelancerListItem;
import com.example.jobsyserver.features.user.model.User;
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

@Repository
public interface FreelancerProfileRepository extends JpaRepository<FreelancerProfile, Long>, JpaSpecificationExecutor<FreelancerProfile> {
    @EntityGraph(attributePaths = {"user", "skills"})
    Optional<FreelancerProfile> findByUser(User user);

    @EntityGraph(attributePaths = {"user", "skills"})
    Optional<FreelancerProfile> findByUserEmail(String email);

    @EntityGraph(attributePaths = {"user", "skills"})
    Optional<FreelancerProfile> findByUserId(Long userId);

    @Override
    @EntityGraph(attributePaths = {"user", "skills"})
    List<FreelancerProfile> findAll();

    @Query("""
            SELECT
              f.id              AS id,
              u.firstName       AS firstName,
              u.lastName        AS lastName,
              f.country         AS country,
              f.city            AS city,
              f.avatarUrl       AS avatarUrl,
              f.averageRating   AS averageRating
            FROM FreelancerProfile f
              JOIN f.user u
            """)
    Page<FreelancerListItem> findAllProjected(Pageable pageable);

    @Query("""
              SELECT f.id            AS id,
                     u.firstName     AS firstName,
                     u.lastName      AS lastName,
                     f.country       AS country,
                     f.city          AS city,
                     f.avatarUrl     AS avatarUrl,
                     f.averageRating AS averageRating
              FROM FreelancerProfile f
              JOIN f.user u
              WHERE :term IS NULL
                 OR LOWER(u.firstName)  LIKE LOWER(CONCAT('%', :term, '%'))
                 OR LOWER(u.lastName)   LIKE LOWER(CONCAT('%', :term, '%'))
                 OR LOWER(f.aboutMe)    LIKE LOWER(CONCAT('%', :term, '%'))
            """)
    Page<FreelancerListItem> findByTextTerm(
            @Param("term") String term,
            Pageable pageable
    );

    @Query("""
              SELECT f.id            AS id,
                     u.firstName     AS firstName,
                     u.lastName      AS lastName,
                     f.country       AS country,
                     f.city          AS city,
                     f.avatarUrl     AS avatarUrl,
                     f.averageRating AS averageRating
              FROM FreelancerProfile f
              JOIN f.user u
              JOIN f.skills s
              WHERE s.id IN :skillIds
                AND ( :term IS NULL
                   OR LOWER(u.firstName)  LIKE LOWER(CONCAT('%', :term, '%'))
                   OR LOWER(u.lastName)   LIKE LOWER(CONCAT('%', :term, '%'))
                   OR LOWER(f.aboutMe)    LIKE LOWER(CONCAT('%', :term, '%'))
                )
              GROUP BY f.id, u.firstName, u.lastName, f.country, f.city, f.avatarUrl, f.averageRating
            """)
    Page<FreelancerListItem> findBySkillsAndTerm(
            @Param("skillIds") List<Long> skillIds,
            @Param("term") String term,
            Pageable pageable
    );
}