package com.example.jobsyserver.features.portfolio.service.impl;

import com.example.jobsyserver.features.portfolio.dto.FreelancerPortfolioCreateDto;
import com.example.jobsyserver.features.portfolio.dto.FreelancerPortfolioDto;
import com.example.jobsyserver.features.portfolio.dto.FreelancerPortfolioUpdateDto;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.portfolio.mapper.FreelancerPortfolioMapper;
import com.example.jobsyserver.features.portfolio.model.FreelancerPortfolio;
import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.portfolio.repository.FreelancerPortfolioRepository;
import com.example.jobsyserver.features.freelancer.repository.FreelancerProfileRepository;
import com.example.jobsyserver.features.portfolio.service.FreelancerPortfolioService;
import com.example.jobsyserver.features.auth.service.SecurityService;
import com.example.jobsyserver.features.skill.model.Skill;
import com.example.jobsyserver.features.skill.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FreelancerPortfolioServiceImpl implements FreelancerPortfolioService {

    private final FreelancerPortfolioRepository repo;
    private final FreelancerProfileRepository profileRepo;
    private final FreelancerPortfolioMapper mapper;
    private final SkillRepository skillRepo;
    private final SecurityService security;

    @Override
    public List<FreelancerPortfolioDto> getMyPortfolio() {
        Long frId = security.getCurrentFreelancerProfileId();
        return repo.findByFreelancerId(frId).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<FreelancerPortfolioDto> getByFreelancerProfileId(Long freelancerProfileId) {
        if (!profileRepo.existsById(freelancerProfileId)) {
            throw new ResourceNotFoundException("FreelancerProfile", freelancerProfileId);
        }
        return repo.findByFreelancerId(freelancerProfileId).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public FreelancerPortfolioDto createPortfolio(FreelancerPortfolioCreateDto dto) {
        Long frId = security.getCurrentFreelancerProfileId();
        FreelancerProfile profile = profileRepo.findById(frId)
                .orElseThrow(() -> new ResourceNotFoundException("FreelancerProfile", frId));
        FreelancerPortfolio entity = mapper.toEntity(dto);
        entity.setFreelancer(profile);
        if (dto.getSkillIds() != null && !dto.getSkillIds().isEmpty()) {
            List<Skill> skills = skillRepo.findAllById(dto.getSkillIds());
            entity.getSkills().addAll(skills);
        }
        FreelancerPortfolio saved = repo.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public FreelancerPortfolioDto updatePortfolio(Long id, FreelancerPortfolioUpdateDto dto) {
        Long frId = security.getCurrentFreelancerProfileId();
        FreelancerPortfolio entity = repo.findById(id)
                .filter(p -> p.getFreelancer().getId().equals(frId))
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", id));
        mapper.updateFromDto(dto, entity);
        if (dto.getSkillIds() != null) {
            entity.getSkills().clear();
            if (!dto.getSkillIds().isEmpty()) {
                List<Skill> skills = skillRepo.findAllById(dto.getSkillIds());
                entity.getSkills().addAll(skills);
            }
        }
        FreelancerPortfolio updated = repo.save(entity);
        return mapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deletePortfolio(Long id) {
        Long frId = security.getCurrentFreelancerProfileId();
        FreelancerPortfolio entity = repo.findById(id)
                .filter(p -> p.getFreelancer().getId().equals(frId))
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio", id));
        repo.delete(entity);
    }
}