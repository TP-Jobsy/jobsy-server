package com.example.jobsyserver.features.portfolio.service.impl;

import com.example.jobsyserver.features.skill.dto.SkillDto;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.skill.mapper.SkillMapper;
import com.example.jobsyserver.features.portfolio.model.FreelancerPortfolio;
import com.example.jobsyserver.features.portfolio.repository.FreelancerPortfolioRepository;
import com.example.jobsyserver.features.skill.model.Skill;
import com.example.jobsyserver.features.skill.repository.SkillRepository;
import com.example.jobsyserver.features.portfolio.service.PortfolioSkillService;
import com.example.jobsyserver.features.auth.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PortfolioSkillServiceImpl implements PortfolioSkillService {

    private final FreelancerPortfolioRepository portfolioRepo;
    private final SkillRepository skillRepo;
    private final SkillMapper skillMapper;
    private final SecurityService security;

    @Override
    public List<SkillDto> getSkillsForPortfolio(Long portfolioId) {
        Long currentFr = security.getCurrentFreelancerProfileId();
        FreelancerPortfolio portfolio = portfolioRepo.findByIdAndFreelancerId(portfolioId, currentFr)
                .orElseThrow(() -> new ResourceNotFoundException("Портфолио", portfolioId));

        return portfolio.getSkills().stream()
                .map(skillMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addSkillToPortfolio(Long portfolioId, Long skillId) {
        Long currentFr = security.getCurrentFreelancerProfileId();
        FreelancerPortfolio portfolio = portfolioRepo.findByIdAndFreelancerId(portfolioId, currentFr)
                .orElseThrow(() -> new ResourceNotFoundException("Портфолио", portfolioId));

        Skill skill = skillRepo.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Навык", skillId));
        if (portfolio.getSkills().add(skill)) {
            portfolioRepo.save(portfolio);
        }
    }

    @Override
    @Transactional
    public void removeSkillFromPortfolio(Long portfolioId, Long skillId) {
        Long currentFr = security.getCurrentFreelancerProfileId();
        FreelancerPortfolio portfolio = portfolioRepo.findByIdAndFreelancerId(portfolioId, currentFr)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", portfolioId));
        boolean removed = portfolio.getSkills().removeIf(s -> s.getId().equals(skillId));
        if (!removed) {
            throw new ResourceNotFoundException("Навык " + skillId + " не найден в портфолио");
        }
        portfolioRepo.save(portfolio);
    }
}