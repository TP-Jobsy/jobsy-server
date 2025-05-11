package com.example.jobsyserver.service.impl.protfolio;

import com.example.jobsyserver.features.portfolio.dto.FreelancerPortfolioCreateDto;
import com.example.jobsyserver.features.portfolio.dto.FreelancerPortfolioDto;
import com.example.jobsyserver.features.portfolio.dto.FreelancerPortfolioUpdateDto;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.portfolio.mapper.FreelancerPortfolioMapper;
import com.example.jobsyserver.features.portfolio.model.FreelancerPortfolio;
import com.example.jobsyserver.features.portfolio.service.impl.FreelancerPortfolioServiceImpl;
import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.portfolio.repository.FreelancerPortfolioRepository;
import com.example.jobsyserver.features.freelancer.repository.FreelancerProfileRepository;
import com.example.jobsyserver.features.auth.service.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FreelancerPortfolioServiceImplTest {

    @Mock
    private FreelancerPortfolioRepository portfolioRepository;

    @Mock
    private FreelancerProfileRepository profileRepository;

    @Mock
    private FreelancerPortfolioMapper portfolioMapper;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private FreelancerPortfolioServiceImpl portfolioService;

    private FreelancerProfile freelancerProfile;
    private FreelancerPortfolio freelancerPortfolio;

    @BeforeEach
    void setup() {
        freelancerProfile = FreelancerProfile.builder()
                .id(1L)
                .build();
        freelancerPortfolio = FreelancerPortfolio.builder()
                .id(1L)
                .freelancer(freelancerProfile)
                .title("Portfolio Title")
                .description("Portfolio Description")
                .projectLink("http://example.com")
                .build();
    }

    @Test
    void getMyPortfolio_ShouldReturnPortfolioList() {
        Long freelancerId = 1L;
        when(securityService.getCurrentFreelancerProfileId()).thenReturn(freelancerId);
        when(portfolioRepository.findByFreelancerId(freelancerId)).thenReturn(List.of(freelancerPortfolio));
        when(portfolioMapper.toDto(any(FreelancerPortfolio.class))).thenReturn(new FreelancerPortfolioDto());
        List<FreelancerPortfolioDto> result = portfolioService.getMyPortfolio();
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(portfolioRepository).findByFreelancerId(freelancerId);
        verify(portfolioMapper).toDto(freelancerPortfolio);
    }

    @Test
    void createPortfolio_ShouldReturnCreatedPortfolioDto() {
        FreelancerPortfolioCreateDto createDto = new FreelancerPortfolioCreateDto("Portfolio Title", "Portfolio Description", "Frontend Developer", "http://example.com", null);
        when(securityService.getCurrentFreelancerProfileId()).thenReturn(1L);
        when(profileRepository.findById(1L)).thenReturn(Optional.of(freelancerProfile));
        when(portfolioMapper.toEntity(createDto)).thenReturn(freelancerPortfolio);
        when(portfolioRepository.save(freelancerPortfolio)).thenReturn(freelancerPortfolio);
        when(portfolioMapper.toDto(freelancerPortfolio)).thenReturn(new FreelancerPortfolioDto());
        FreelancerPortfolioDto result = portfolioService.createPortfolio(createDto);
        assertNotNull(result);
        verify(portfolioRepository).save(freelancerPortfolio);
    }

    @Test
    void createPortfolio_ShouldThrowException_WhenFreelancerNotFound() {
        FreelancerPortfolioCreateDto createDto = new FreelancerPortfolioCreateDto("Portfolio Title", "Portfolio Description", "Frontend Developer", "http://example.com", null);
        when(securityService.getCurrentFreelancerProfileId()).thenReturn(1L);
        when(profileRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> portfolioService.createPortfolio(createDto));
    }

    @Test
    void updatePortfolio_ShouldReturnUpdatedPortfolioDto() {
        FreelancerPortfolioUpdateDto updateDto = new FreelancerPortfolioUpdateDto("Updated Title", "Updated Description", "Updated Role", "http://updated.com", null);
        when(securityService.getCurrentFreelancerProfileId()).thenReturn(1L);
        when(portfolioRepository.findById(1L)).thenReturn(Optional.of(freelancerPortfolio));
        doNothing().when(portfolioMapper).updateFromDto(updateDto, freelancerPortfolio);
        when(portfolioRepository.save(freelancerPortfolio)).thenReturn(freelancerPortfolio);
        when(portfolioMapper.toDto(freelancerPortfolio)).thenReturn(new FreelancerPortfolioDto());
        FreelancerPortfolioDto result = portfolioService.updatePortfolio(1L, updateDto);
        assertNotNull(result);
        verify(portfolioRepository).save(freelancerPortfolio);
    }

    @Test
    void updatePortfolio_ShouldThrowException_WhenPortfolioNotFound() {
        FreelancerPortfolioUpdateDto updateDto = new FreelancerPortfolioUpdateDto("Updated Title", "Updated Description", "Updated Role", "http://updated.com", null);
        when(securityService.getCurrentFreelancerProfileId()).thenReturn(1L);
        when(portfolioRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> portfolioService.updatePortfolio(1L, updateDto));
    }

    @Test
    void deletePortfolio_ShouldDeletePortfolio() {
        when(securityService.getCurrentFreelancerProfileId()).thenReturn(1L);
        when(portfolioRepository.findById(1L)).thenReturn(Optional.of(freelancerPortfolio));
        portfolioService.deletePortfolio(1L);
        verify(portfolioRepository).delete(freelancerPortfolio);
    }

    @Test
    void deletePortfolio_ShouldThrowException_WhenPortfolioNotFound() {
        when(securityService.getCurrentFreelancerProfileId()).thenReturn(1L);
        when(portfolioRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> portfolioService.deletePortfolio(1L));
    }
}
