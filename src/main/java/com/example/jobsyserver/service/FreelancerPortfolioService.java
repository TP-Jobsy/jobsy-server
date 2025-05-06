package com.example.jobsyserver.service;

import com.example.jobsyserver.dto.portfolio.FreelancerPortfolioCreateDto;
import com.example.jobsyserver.dto.portfolio.FreelancerPortfolioDto;
import com.example.jobsyserver.dto.portfolio.FreelancerPortfolioUpdateDto;

import java.util.List;

public interface FreelancerPortfolioService {
    List<FreelancerPortfolioDto> getMyPortfolio();
    FreelancerPortfolioDto createPortfolio(FreelancerPortfolioCreateDto dto);
    FreelancerPortfolioDto updatePortfolio(Long id, FreelancerPortfolioUpdateDto dto);
    void deletePortfolio(Long id);
}