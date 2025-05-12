package com.example.jobsyserver.features.portfolio.service;

import com.example.jobsyserver.features.skill.dto.SkillDto;

import java.util.List;

public interface PortfolioSkillService {
    void addSkillToPortfolio(Long portfolioId, Long skillId);
    void removeSkillFromPortfolio(Long portfolioId, Long skillId);
    List<SkillDto> getSkillsForPortfolio(Long portfolioId);
}