package com.example.jobsyserver.service.impl.search;

import com.example.jobsyserver.features.freelancer.projection.FreelancerListItem;
import com.example.jobsyserver.features.freelancer.repository.FreelancerProfileRepository;
import com.example.jobsyserver.features.project.projection.ProjectListItem;
import com.example.jobsyserver.features.project.repository.ProjectRepository;
import com.example.jobsyserver.features.search.service.impl.SearchServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceImplTest {

    @Mock
    private FreelancerProfileRepository freelancerRepo;

    @Mock
    private ProjectRepository projectRepo;

    @InjectMocks
    private SearchServiceImpl searchService;

    private List<Long> skillIds;
    private String term;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        skillIds = List.of(1L, 2L);
        term = "test";
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void searchFreelancers_ShouldReturnPage_WhenMatchesFound() {
        FreelancerListItem pli = mock(FreelancerListItem.class);
        when(pli.getId()).thenReturn(1L);
        when(pli.getFirstName()).thenReturn("Alice");
        when(pli.getLastName()).thenReturn("Smith");
        when(pli.getCountry()).thenReturn("USA");
        when(pli.getCity()).thenReturn("NYC");
        when(pli.getAvatarUrl()).thenReturn("https://avatar.url");
        when(pli.getAverageRating()).thenReturn(4.5);
        Page<FreelancerListItem> page = new PageImpl<>(List.of(pli), pageable, 1);
        when(freelancerRepo.findBySkillsAndTerm(skillIds, term, pageable))
                .thenReturn(page);
        Page<FreelancerListItem> result =
                searchService.searchFreelancers(skillIds, term, pageable);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        FreelancerListItem first = result.getContent().get(0);
        assertEquals(1L, first.getId());
        assertEquals("Alice", first.getFirstName());
        assertEquals("Smith", first.getLastName());
        assertEquals("USA", first.getCountry());
        assertEquals("NYC", first.getCity());
        assertEquals("https://avatar.url", first.getAvatarUrl());
        assertEquals(4.5, first.getAverageRating());
        verify(freelancerRepo, times(1))
                .findBySkillsAndTerm(skillIds, term, pageable);
    }

    @Test
    void searchFreelancers_ShouldReturnEmptyPage_WhenNoMatches() {
        Page<FreelancerListItem> emptyPage =
                new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(freelancerRepo.findBySkillsAndTerm(skillIds, term, pageable))
                .thenReturn(emptyPage);
        Page<FreelancerListItem> result =
                searchService.searchFreelancers(skillIds, term, pageable);
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
        verify(freelancerRepo, times(1))
                .findBySkillsAndTerm(skillIds, term, pageable);
    }

    @Test
    void searchProjects_ShouldReturnPage_WhenMatchesFound() {
        ProjectListItem pli = mock(ProjectListItem.class);
        when(pli.getId()).thenReturn(100L);
        when(pli.getTitle()).thenReturn("Project X");
        when(pli.getFixedPrice()).thenReturn(BigDecimal.valueOf(500));
        when(pli.getClientCompanyName()).thenReturn("Acme Corp");
        when(pli.getClientCity()).thenReturn("LA");
        when(pli.getClientCountry()).thenReturn("USA");
        when(pli.getAssignedFreelancerFirstName()).thenReturn("John");
        when(pli.getAssignedFreelancerLastName()).thenReturn("Doe");
        Page<ProjectListItem> page = new PageImpl<>(List.of(pli), pageable, 1);
        when(projectRepo.findBySkillsAndTerm(skillIds, term, pageable))
                .thenReturn(page);
        Page<ProjectListItem> result =
                searchService.searchProjects(skillIds, term, pageable);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        ProjectListItem first = result.getContent().get(0);
        assertEquals(100L, first.getId());
        assertEquals("Project X", first.getTitle());
        assertEquals(BigDecimal.valueOf(500), first.getFixedPrice());
        assertEquals("Acme Corp", first.getClientCompanyName());
        assertEquals("LA", first.getClientCity());
        assertEquals("USA", first.getClientCountry());
        assertEquals("John", first.getAssignedFreelancerFirstName());
        assertEquals("Doe", first.getAssignedFreelancerLastName());
        verify(projectRepo, times(1))
                .findBySkillsAndTerm(skillIds, term, pageable);
    }

    @Test
    void searchProjects_ShouldReturnEmptyPage_WhenNoMatches() {
        Page<ProjectListItem> emptyPage =
                new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(projectRepo.findBySkillsAndTerm(skillIds, term, pageable))
                .thenReturn(emptyPage);

        Page<ProjectListItem> result =
                searchService.searchProjects(skillIds, term, pageable);
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
        verify(projectRepo, times(1))
                .findBySkillsAndTerm(skillIds, term, pageable);
    }
}
