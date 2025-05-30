package com.example.jobsyserver.features.admin.service;

import com.example.jobsyserver.features.portfolio.dto.FreelancerPortfolioDto;
import com.example.jobsyserver.features.portfolio.projection.PortfolioAdminListItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface PortfolioAdminService {
    List<FreelancerPortfolioDto> getByFreelancer(Long freelancerId);

    void delete(Long freelancerId, Long portfolioId);

    FreelancerPortfolioDto getById(Long portfolioId);

    Page<PortfolioAdminListItem> pageAll(Pageable pageable);

    Page<PortfolioAdminListItem> search(String term, String freelancerName,
                                        LocalDateTime from, LocalDateTime to,
                                        Pageable pageable);
}
