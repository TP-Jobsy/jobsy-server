package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.portfolio.FreelancerPortfolioCreateDto;
import com.example.jobsyserver.dto.portfolio.FreelancerPortfolioDto;
import com.example.jobsyserver.dto.portfolio.FreelancerPortfolioUpdateDto;
import com.example.jobsyserver.exception.ResourceNotFoundException;
import com.example.jobsyserver.mapper.FreelancerPortfolioMapper;
import com.example.jobsyserver.model.FreelancerPortfolio;
import com.example.jobsyserver.model.FreelancerProfile;
import com.example.jobsyserver.repository.FreelancerPortfolioRepository;
import com.example.jobsyserver.repository.FreelancerProfileRepository;
import com.example.jobsyserver.service.FreelancerPortfolioService;
import com.example.jobsyserver.service.SecurityService;
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
    private final SecurityService security;

    @Override
    public List<FreelancerPortfolioDto> getMyPortfolio() {
        Long frId = security.getCurrentFreelancerProfileId();
        return repo.findByFreelancerId(frId).stream()
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