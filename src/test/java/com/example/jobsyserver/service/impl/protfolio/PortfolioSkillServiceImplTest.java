package com.example.jobsyserver.service.impl.protfolio;

import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.portfolio.service.impl.PortfolioSkillServiceImpl;
import com.example.jobsyserver.features.skill.dto.SkillDto;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.portfolio.model.FreelancerPortfolio;
import com.example.jobsyserver.features.skill.model.Skill;
import com.example.jobsyserver.features.skill.mapper.SkillMapper;
import com.example.jobsyserver.features.portfolio.repository.FreelancerPortfolioRepository;
import com.example.jobsyserver.features.skill.repository.SkillRepository;
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
class PortfolioSkillServiceImplTest {

    @InjectMocks
    private PortfolioSkillServiceImpl service;

    @Mock
    private FreelancerPortfolioRepository portfolioRepo;

    @Mock
    private SkillRepository skillRepo;

    @Mock
    private SkillMapper skillMapper;

    @Mock
    private SecurityService security;

    private FreelancerPortfolio portfolio;
    private Skill skill;
    private SkillDto skillDto;
    private Long portfolioId = 1L;
    private Long skillId = 1L;
    private Long currentFr = 1L;

    @BeforeEach
    void setUp() {
        FreelancerProfile fr = new FreelancerProfile();
        fr.setId(currentFr);
        portfolio = new FreelancerPortfolio();
        portfolio.setId(portfolioId);
        portfolio.setFreelancer(fr);
        portfolio.setSkills(new java.util.HashSet<>());
        skill = new Skill();
        skill.setId(skillId);
        skill.setName("Java");

        skillDto = new SkillDto();
        skillDto.setId(skillId);
        skillDto.setName("Java");
    }

    @Test
    void addSkillToPortfolio_ShouldAddSkill_WhenValidData() {
        when(security.getCurrentFreelancerProfileId()).thenReturn(currentFr);
        when(portfolioRepo.findByIdAndFreelancerId(portfolioId, currentFr))
                .thenReturn(Optional.of(portfolio));
        when(skillRepo.findById(skillId)).thenReturn(Optional.of(skill));
        service.addSkillToPortfolio(portfolioId, skillId);
        assertTrue(portfolio.getSkills().contains(skill));
        verify(portfolioRepo, times(1)).save(portfolio);
    }

    @Test
    void addSkillToPortfolio_ShouldNotSaveAgain_WhenSkillAlreadyPresent() {
        portfolio.getSkills().add(skill);
        when(security.getCurrentFreelancerProfileId()).thenReturn(currentFr);
        when(portfolioRepo.findByIdAndFreelancerId(portfolioId, currentFr))
                .thenReturn(Optional.of(portfolio));
        when(skillRepo.findById(skillId)).thenReturn(Optional.of(skill));
        service.addSkillToPortfolio(portfolioId, skillId);
        verify(portfolioRepo, never()).save(portfolio);
    }

    @Test
    void addSkillToPortfolio_ShouldThrow_WhenPortfolioNotFound() {
        when(security.getCurrentFreelancerProfileId()).thenReturn(currentFr);
        when(portfolioRepo.findByIdAndFreelancerId(portfolioId, currentFr))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> service.addSkillToPortfolio(portfolioId, skillId));
    }

    @Test
    void addSkillToPortfolio_ShouldThrow_WhenSkillNotFound() {
        when(security.getCurrentFreelancerProfileId()).thenReturn(currentFr);
        when(portfolioRepo.findByIdAndFreelancerId(portfolioId, currentFr))
                .thenReturn(Optional.of(portfolio));
        when(skillRepo.findById(skillId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> service.addSkillToPortfolio(portfolioId, skillId));
    }

    @Test
    void removeSkillFromPortfolio_ShouldRemoveSkill_WhenValidData() {
        portfolio.getSkills().add(skill);
        when(security.getCurrentFreelancerProfileId()).thenReturn(currentFr);
        when(portfolioRepo.findByIdAndFreelancerId(portfolioId, currentFr))
                .thenReturn(Optional.of(portfolio));
        service.removeSkillFromPortfolio(portfolioId, skillId);
        assertFalse(portfolio.getSkills().contains(skill));
        verify(portfolioRepo, times(1)).save(portfolio);
    }

    @Test
    void removeSkillFromPortfolio_ShouldThrow_WhenPortfolioNotFound() {
        when(security.getCurrentFreelancerProfileId()).thenReturn(currentFr);
        when(portfolioRepo.findByIdAndFreelancerId(portfolioId, currentFr))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> service.removeSkillFromPortfolio(portfolioId, skillId));
    }

    @Test
    void removeSkillFromPortfolio_ShouldThrow_WhenSkillNotInPortfolio() {
        when(security.getCurrentFreelancerProfileId()).thenReturn(currentFr);
        when(portfolioRepo.findByIdAndFreelancerId(portfolioId, currentFr))
                .thenReturn(Optional.of(portfolio));
        assertThrows(ResourceNotFoundException.class,
                () -> service.removeSkillFromPortfolio(portfolioId, skillId));
    }

    @Test
    void getSkillsForPortfolio_ShouldReturnSkills_WhenValidPortfolioId() {
        portfolio.getSkills().add(skill);
        when(security.getCurrentFreelancerProfileId()).thenReturn(currentFr);
        when(portfolioRepo.findByIdAndFreelancerId(portfolioId, currentFr))
                .thenReturn(Optional.of(portfolio));
        when(skillMapper.toDto(skill)).thenReturn(skillDto);
        List<SkillDto> dtos = service.getSkillsForPortfolio(portfolioId);
        assertEquals(1, dtos.size());
        assertEquals(skillDto, dtos.get(0));
    }

    @Test
    void getSkillsForPortfolio_ShouldThrow_WhenPortfolioNotFound() {
        when(security.getCurrentFreelancerProfileId()).thenReturn(currentFr);
        when(portfolioRepo.findByIdAndFreelancerId(portfolioId, currentFr))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> service.getSkillsForPortfolio(portfolioId));
    }
}