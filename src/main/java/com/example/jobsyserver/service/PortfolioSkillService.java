package com.example.jobsyserver.service;

import com.example.jobsyserver.dto.common.SkillDto;

import java.util.List;

public interface PortfolioSkillService {
    void addSkillToPortfolio(Long portfolioId, Long skillId);
    void removeSkillFromPortfolio(Long portfolioId, Long skillId);
    List<SkillDto> getSkillsForPortfolio(Long portfolioId);
}