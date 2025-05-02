package com.example.jobsyserver.repository;

import com.example.jobsyserver.model.FreelancerPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FreelancerPortfolioRepository extends JpaRepository<FreelancerPortfolio, Long> {
    List<FreelancerPortfolio> findByFreelancerId(Long freelancerId);
}