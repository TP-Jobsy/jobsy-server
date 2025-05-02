package com.example.jobsyserver.repository;

import com.example.jobsyserver.model.PortfolioSkill;
import com.example.jobsyserver.model.PortfolioSkillId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioSkillRepository extends JpaRepository<PortfolioSkill, PortfolioSkillId> {
}
