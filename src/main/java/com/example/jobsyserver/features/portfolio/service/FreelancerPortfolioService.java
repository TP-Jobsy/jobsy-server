package com.example.jobsyserver.features.portfolio.service;

import com.example.jobsyserver.features.portfolio.dto.FreelancerPortfolioCreateDto;
import com.example.jobsyserver.features.portfolio.dto.FreelancerPortfolioDto;
import com.example.jobsyserver.features.portfolio.dto.FreelancerPortfolioUpdateDto;

import java.util.List;

public interface FreelancerPortfolioService {
    List<FreelancerPortfolioDto> getMyPortfolio();
    FreelancerPortfolioDto createPortfolio(FreelancerPortfolioCreateDto dto);
    FreelancerPortfolioDto updatePortfolio(Long id, FreelancerPortfolioUpdateDto dto);
    void deletePortfolio(Long id);
    List<FreelancerPortfolioDto> getByFreelancerProfileId(Long freelancerProfileId);
}