package com.example.jobsyserver.service.impl.protfolio;

import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.skill.dto.SkillDto;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.portfolio.model.FreelancerPortfolio;
import com.example.jobsyserver.features.portfolio.service.impl.PortfolioSkillServiceImpl;
import com.example.jobsyserver.features.portfolio.model.PortfolioSkill;
import com.example.jobsyserver.features.portfolio.model.PortfolioSkillId;
import com.example.jobsyserver.features.skill.model.Skill;
import com.example.jobsyserver.features.skill.mapper.SkillMapper;
import com.example.jobsyserver.features.portfolio.repository.FreelancerPortfolioRepository;
import com.example.jobsyserver.features.portfolio.repository.PortfolioSkillRepository;
import com.example.jobsyserver.features.skill.repository.SkillRepository;
import com.example.jobsyserver.features.auth.service.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PortfolioSkillServiceImplTest {

    @InjectMocks
    private PortfolioSkillServiceImpl portfolioSkillService;

    @Mock
    private PortfolioSkillRepository skillLinkRepo;

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
    private PortfolioSkill portfolioSkill;
    private Long portfolioId = 1L;
    private Long skillId = 1L;

    @BeforeEach
    void setUp() {
        FreelancerProfile freelancer = new FreelancerProfile();
        freelancer.setId(1L);

        portfolio = new FreelancerPortfolio();
        portfolio.setId(portfolioId);
        portfolio.setFreelancer(freelancer);

        skill = new Skill();
        skill.setId(skillId);
        skill.setName("Java");

        skillDto = new SkillDto();
        skillDto.setId(skillId);
        skillDto.setName("Java");

        portfolioSkill = new PortfolioSkill();
        portfolioSkill.setId(new PortfolioSkillId(portfolioId, skillId));
        portfolioSkill.setPortfolio(portfolio);
        portfolioSkill.setSkill(skill);
    }


    @Test
    void addSkillToPortfolio_ShouldAddSkill_WhenValidData() {
        when(security.getCurrentFreelancerProfileId()).thenReturn(1L);
        when(portfolioRepo.findById(portfolioId)).thenReturn(java.util.Optional.of(portfolio));
        when(skillRepo.findById(skillId)).thenReturn(java.util.Optional.of(skill));
        portfolioSkillService.addSkillToPortfolio(portfolioId, skillId);
        verify(skillLinkRepo, times(1)).save(any(PortfolioSkill.class));
    }

    @Test
    void addSkillToPortfolio_ShouldThrowResourceNotFoundException_WhenPortfolioNotFound() {
        when(security.getCurrentFreelancerProfileId()).thenReturn(1L);
        when(portfolioRepo.findById(portfolioId)).thenReturn(java.util.Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> portfolioSkillService.addSkillToPortfolio(portfolioId, skillId));
    }

    @Test
    void addSkillToPortfolio_ShouldThrowResourceNotFoundException_WhenSkillNotFound() {
        when(security.getCurrentFreelancerProfileId()).thenReturn(1L);
        when(portfolioRepo.findById(portfolioId)).thenReturn(java.util.Optional.of(portfolio));
        when(skillRepo.findById(skillId)).thenReturn(java.util.Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> portfolioSkillService.addSkillToPortfolio(portfolioId, skillId));
    }

    @Test
    void removeSkillFromPortfolio_ShouldRemoveSkill_WhenValidData() {
        when(security.getCurrentFreelancerProfileId()).thenReturn(1L);
        when(portfolioRepo.findById(portfolioId)).thenReturn(java.util.Optional.of(portfolio));
        when(skillLinkRepo.findById(new PortfolioSkillId(portfolioId, skillId)))
                .thenReturn(java.util.Optional.of(portfolioSkill));
        portfolioSkillService.removeSkillFromPortfolio(portfolioId, skillId);
        verify(skillLinkRepo, times(1)).deleteById(any(PortfolioSkillId.class));
    }

    @Test
    void removeSkillFromPortfolio_ShouldThrowResourceNotFoundException_WhenPortfolioNotFound() {
        when(security.getCurrentFreelancerProfileId()).thenReturn(1L);
        when(portfolioRepo.findById(portfolioId)).thenReturn(java.util.Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> portfolioSkillService.removeSkillFromPortfolio(portfolioId, skillId));
    }

    @Test
    void removeSkillFromPortfolio_ShouldThrowResourceNotFoundException_WhenSkillLinkNotFound() {
        when(security.getCurrentFreelancerProfileId()).thenReturn(1L);
        when(portfolioRepo.findById(portfolioId)).thenReturn(java.util.Optional.of(portfolio));
        when(skillLinkRepo.findById(new PortfolioSkillId(portfolioId, skillId))).thenReturn(java.util.Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> portfolioSkillService.removeSkillFromPortfolio(portfolioId, skillId));
    }

    @Test
    void getSkillsForPortfolio_ShouldReturnSkills_WhenValidPortfolioId() {
        when(security.getCurrentFreelancerProfileId()).thenReturn(1L);
        when(portfolioRepo.findById(portfolioId)).thenReturn(java.util.Optional.of(portfolio));
        when(skillLinkRepo.findAll()).thenReturn(Collections.singletonList(portfolioSkill));
        when(skillMapper.toDto(skill)).thenReturn(skillDto);
        List<SkillDto> skills = portfolioSkillService.getSkillsForPortfolio(portfolioId);
        assertNotNull(skills);
        assertEquals(1, skills.size());
        assertEquals("Java", skills.get(0).getName());
    }

    @Test
    void getSkillsForPortfolio_ShouldThrowResourceNotFoundException_WhenPortfolioNotFound() {
        when(security.getCurrentFreelancerProfileId()).thenReturn(1L);
        when(portfolioRepo.findById(portfolioId)).thenReturn(java.util.Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> portfolioSkillService.getSkillsForPortfolio(portfolioId));
    }
}
