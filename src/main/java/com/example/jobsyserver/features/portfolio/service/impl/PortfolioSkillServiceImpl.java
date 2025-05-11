package com.example.jobsyserver.features.portfolio.service.impl;

import com.example.jobsyserver.features.skill.dto.SkillDto;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.skill.mapper.SkillMapper;
import com.example.jobsyserver.features.portfolio.model.FreelancerPortfolio;
import com.example.jobsyserver.features.portfolio.model.PortfolioSkill;
import com.example.jobsyserver.features.portfolio.model.PortfolioSkillId;
import com.example.jobsyserver.features.skill.model.Skill;
import com.example.jobsyserver.features.portfolio.repository.FreelancerPortfolioRepository;
import com.example.jobsyserver.features.portfolio.repository.PortfolioSkillRepository;
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

    private final PortfolioSkillRepository skillLinkRepo;
    private final FreelancerPortfolioRepository portfolioRepo;
    private final SkillRepository skillRepo;
    private final SkillMapper skillMapper;
    private final SecurityService security;

    @Override
    @Transactional
    public void addSkillToPortfolio(Long portfolioId, Long skillId) {
        Long currentFr = security.getCurrentFreelancerProfileId();
        FreelancerPortfolio portfolio = portfolioRepo.findById(portfolioId)
                .filter(p -> p.getFreelancer().getId().equals(currentFr))
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", portfolioId));

        Skill skill = skillRepo.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", skillId));
        PortfolioSkill link = new PortfolioSkill();
        link.setId(new PortfolioSkillId(portfolioId, skillId));
        link.setPortfolio(portfolio);
        link.setSkill(skill);
        skillLinkRepo.save(link);
    }

    @Override
    @Transactional
    public void removeSkillFromPortfolio(Long portfolioId, Long skillId) {
        Long currentFr = security.getCurrentFreelancerProfileId();
        portfolioRepo.findById(portfolioId)
                .filter(p -> p.getFreelancer().getId().equals(currentFr))
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", portfolioId));

        PortfolioSkillId id = new PortfolioSkillId(portfolioId, skillId);
        skillLinkRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PortfolioSkill", id))
                .getId();

        skillLinkRepo.deleteById(id);
    }

    @Override
    public List<SkillDto> getSkillsForPortfolio(Long portfolioId) {
        Long currentFr = security.getCurrentFreelancerProfileId();
        portfolioRepo.findById(portfolioId)
                .filter(p -> p.getFreelancer().getId().equals(currentFr))
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", portfolioId));

        return skillLinkRepo.findAll().stream()
                .filter(link -> link.getId().getPortfolioId().equals(portfolioId))
                .map(link -> skillMapper.toDto(link.getSkill()))
                .collect(Collectors.toList());
    }
}