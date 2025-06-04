package com.example.jobsyserver.features.admin.service.impl;

import com.example.jobsyserver.features.admin.service.PortfolioAdminService;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.freelancer.repository.FreelancerProfileRepository;
import com.example.jobsyserver.features.portfolio.dto.FreelancerPortfolioDto;
import com.example.jobsyserver.features.portfolio.mapper.FreelancerPortfolioMapper;
import com.example.jobsyserver.features.portfolio.model.FreelancerPortfolio;
import com.example.jobsyserver.features.portfolio.projection.PortfolioAdminListItem;
import com.example.jobsyserver.features.portfolio.repository.FreelancerPortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.jobsyserver.features.search.specification.PortfolioSpecification.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PortfolioAdminServiceImpl implements PortfolioAdminService {

    private final FreelancerPortfolioRepository repo;
    private final FreelancerPortfolioMapper mapper;
    private final FreelancerProfileRepository freelancerProfileRepo;

    @Override
    public List<FreelancerPortfolioDto> getByFreelancer(Long userId) {
        FreelancerProfile profile = freelancerProfileRepo
                .findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("FreelancerProfile", userId));
        return repo.findByFreelancerId(profile.getId()).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public void delete(Long profileId, Long portfolioId) {
        FreelancerProfile profile = freelancerProfileRepo
                .findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("FreelancerProfile", profileId));
        FreelancerPortfolio p = repo.findByIdAndFreelancerId(portfolioId, profile.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Портфолио", portfolioId));

        repo.delete(p);
    }

    @Override
    public FreelancerPortfolioDto getById(Long portfolioId) {
        FreelancerPortfolio p = repo.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Портфолио", portfolioId));
        return mapper.toDto(p);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PortfolioAdminListItem> pageAll(Pageable pageable) {
        return repo.findAllProjected(null, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PortfolioAdminListItem> search(
            String term, String freelancerName,
            LocalDateTime from, LocalDateTime to,
            Pageable pageable
    ) {
        Specification<FreelancerPortfolio> spec = Specification
                .where(textSearchTitle(term))
                .or(textSearchFreelancer(freelancerName))
                .and(createdBetween(from, to));
        return repo.findAllProjected(spec, pageable);
    }
}