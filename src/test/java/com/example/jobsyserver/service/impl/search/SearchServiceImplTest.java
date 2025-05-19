package com.example.jobsyserver.service.impl.search;

import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileAboutDto;
import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileBasicDto;
import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileContactDto;
import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileDto;
import com.example.jobsyserver.features.project.dto.ProjectDto;
import com.example.jobsyserver.features.freelancer.mapper.FreelancerProfileMapper;
import com.example.jobsyserver.features.project.mapper.ProjectMapper;
import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.project.model.Project;
import com.example.jobsyserver.features.freelancer.repository.FreelancerProfileRepository;
import com.example.jobsyserver.features.project.repository.ProjectRepository;
import com.example.jobsyserver.features.search.service.impl.SearchServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SearchServiceImplTest {

    @Mock
    private FreelancerProfileRepository freelancerProfileRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private FreelancerProfileMapper freelancerProfileMapper;

    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private SearchServiceImpl searchService;

    private FreelancerProfile freelancerProfile;
    private Project project;
    private FreelancerProfileDto freelancerProfileDto;
    private ProjectDto projectDto;

    @BeforeEach
    void setup() {
        freelancerProfile = new FreelancerProfile();
        freelancerProfile.setId(1L);
        freelancerProfileDto = new FreelancerProfileDto();
        freelancerProfileDto.setId(1L);
        FreelancerProfileBasicDto basicDto = new FreelancerProfileBasicDto();
        basicDto.setCountry("Russia");
        basicDto.setCity("Moscow");
        freelancerProfileDto.setBasic(basicDto);

        FreelancerProfileContactDto contactDto = new FreelancerProfileContactDto();
        contactDto.setContactLink("https://linkedin.com/in/username");
        freelancerProfileDto.setContact(contactDto);

        FreelancerProfileAboutDto aboutDto = new FreelancerProfileAboutDto();
        aboutDto.setAboutMe("Experienced developer with 5 years of experience...");
        freelancerProfileDto.setAbout(aboutDto);

        project = new Project();
        project.setId(1L);
        project.setTitle("Project 1");
        projectDto = new ProjectDto();
        projectDto.setId(1L);
        projectDto.setTitle("Project 1");
    }

    @Test
    void searchFreelancers_ShouldReturnList_WhenMatchingFreelancersFound() {
        List<Long> skillIds = List.of(1L, 2L);
        String textTerm = "Java";
        when(freelancerProfileRepository.findAll(
                ArgumentMatchers.<Specification<FreelancerProfile>>any()
        )).thenReturn(List.of(freelancerProfile));
        when(freelancerProfileMapper.toDto(freelancerProfile))
                .thenReturn(freelancerProfileDto);

        List<FreelancerProfileDto> result = searchService.searchFreelancers(skillIds, textTerm);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Russia", result.get(0).getBasic().getCountry());
        assertEquals("Moscow", result.get(0).getBasic().getCity());
        assertEquals("https://linkedin.com/in/username",
                result.get(0).getContact().getContactLink());
        assertEquals("Experienced developer with 5 years of experience...",
                result.get(0).getAbout().getAboutMe());
        verify(freelancerProfileRepository, times(1)).findAll(
                ArgumentMatchers.<Specification<FreelancerProfile>>any()
        );
    }

    @Test
    void searchFreelancers_ShouldReturnEmptyList_WhenNoMatchingFreelancersFound() {
        List<Long> skillIds = List.of(1L, 2L);
        String textTerm = "NonExistentSkill";

        when(freelancerProfileRepository.findAll(
                ArgumentMatchers.<Specification<FreelancerProfile>>any()
        )).thenReturn(Collections.emptyList());

        List<FreelancerProfileDto> result = searchService.searchFreelancers(skillIds, textTerm);

        assertTrue(result.isEmpty());
        verify(freelancerProfileRepository, times(1)).findAll(
                ArgumentMatchers.<Specification<FreelancerProfile>>any()
        );
    }

    @Test
    void searchProjects_ShouldReturnList_WhenMatchingProjectsFound() {
        List<Long> skillIds = List.of(1L, 2L);
        String textTerm = "Project";

        when(projectRepository.findAll(
                ArgumentMatchers.<Specification<Project>>any()
        )).thenReturn(List.of(project));
        when(projectMapper.toDto(project)).thenReturn(projectDto);

        List<ProjectDto> result = searchService.searchProjects(skillIds, textTerm);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Project 1", result.get(0).getTitle());
        verify(projectRepository, times(1)).findAll(
                ArgumentMatchers.<Specification<Project>>any()
        );
    }

    @Test
    void searchProjects_ShouldReturnEmptyList_WhenNoMatchingProjectsFound() {
        List<Long> skillIds = List.of(1L, 2L);
        String textTerm = "NonExistentProject";
        when(projectRepository.findAll(
                ArgumentMatchers.<Specification<Project>>any()
        )).thenReturn(Collections.emptyList());
        List<ProjectDto> result = searchService.searchProjects(skillIds, textTerm);
        assertTrue(result.isEmpty());
        verify(projectRepository, times(1)).findAll(
                ArgumentMatchers.<Specification<Project>>any()
        );
    }
}
