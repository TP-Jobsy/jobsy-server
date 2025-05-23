package com.example.jobsyserver.features.portfolio.repository;

import com.example.jobsyserver.features.portfolio.model.FreelancerPortfolio;
import com.example.jobsyserver.features.portfolio.projection.PortfolioAdminListItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FreelancerPortfolioRepository extends JpaRepository<FreelancerPortfolio, Long> {
    @EntityGraph(attributePaths = {"freelancer", "skills"})
    List<FreelancerPortfolio> findByFreelancerId(Long freelancerId);

    @EntityGraph(attributePaths = {"freelancer", "skills"})
    Optional<FreelancerPortfolio> findByIdAndFreelancerId(Long portfolioId, Long freelancerId);

    @Query("""
            SELECT
              p.id         AS id,
              p.title      AS title,
              p.createdAt  AS createdAt,
              u.firstName  AS firstName,
              u.lastName   AS lastName
            FROM FreelancerPortfolio p
            JOIN p.freelancer f
            JOIN f.user u
            """)
    Page<PortfolioAdminListItem> findAllProjectedByAdmin(Pageable pageable);
}