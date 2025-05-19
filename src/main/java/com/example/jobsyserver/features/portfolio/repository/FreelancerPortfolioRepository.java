package com.example.jobsyserver.features.portfolio.repository;

import com.example.jobsyserver.features.portfolio.model.FreelancerPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FreelancerPortfolioRepository extends JpaRepository<FreelancerPortfolio, Long> {
    List<FreelancerPortfolio> findByFreelancerId(Long freelancerId);

    Optional<FreelancerPortfolio> findByIdAndFreelancerId(Long portfolioId, Long freelancerId);
}