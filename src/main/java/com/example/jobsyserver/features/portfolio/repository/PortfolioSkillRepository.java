package com.example.jobsyserver.features.portfolio.repository;

import com.example.jobsyserver.features.portfolio.model.PortfolioSkill;
import com.example.jobsyserver.features.portfolio.model.PortfolioSkillId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioSkillRepository extends JpaRepository<PortfolioSkill, PortfolioSkillId> {
}
