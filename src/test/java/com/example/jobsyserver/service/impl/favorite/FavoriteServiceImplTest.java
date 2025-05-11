package com.example.jobsyserver.service.impl.favorite;

import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileDto;
import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.freelancer.repository.FreelancerProfileRepository;
import com.example.jobsyserver.features.project.dto.ProjectDto;
import com.example.jobsyserver.features.client.model.ClientProfile;
import com.example.jobsyserver.features.client.repository.ClientProfileRepository;
import com.example.jobsyserver.features.favorites.model.FavoriteFreelancer;
import com.example.jobsyserver.features.favorites.model.FavoriteProject;
import com.example.jobsyserver.features.favorites.repository.FavoriteFreelancerRepository;
import com.example.jobsyserver.features.favorites.repository.FavoriteProjectRepository;
import com.example.jobsyserver.features.favorites.service.impl.FavoriteServiceImpl;
import com.example.jobsyserver.features.project.model.Project;
import com.example.jobsyserver.features.project.repository.ProjectRepository;
import com.example.jobsyserver.features.freelancer.mapper.FreelancerProfileMapper;
import com.example.jobsyserver.features.project.mapper.ProjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceImplTest {

    @Mock
    private FavoriteProjectRepository favProjRepo;
    @Mock
    private FavoriteFreelancerRepository favFrRepo;
    @Mock
    private ProjectMapper projectMapper;
    @Mock
    private FreelancerProfileMapper freelancerMapper;
    @Mock
    private FreelancerProfileRepository freelancerProfileRepo;
    @Mock
    private ClientProfileRepository clientProfileRepo;
    @Mock
    private ProjectRepository projectRepo;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    private final Long testFreelancerId = 1L;
    private final Long testClientId = 2L;
    private final Long testProjectId = 3L;
    private final Long testFavoriteFreelancerId = 4L;

    @Test
    void addProjectToFavorites_shouldCreateAndSaveFavoriteProject() {
        FreelancerProfile freelancer = new FreelancerProfile();
        freelancer.setId(testFreelancerId);
        Project project = new Project();
        project.setId(testProjectId);

        when(freelancerProfileRepo.findById(testFreelancerId)).thenReturn(Optional.of(freelancer));
        when(projectRepo.findById(testProjectId)).thenReturn(Optional.of(project));
        when(favProjRepo.existsByFreelancerIdAndProjectId(testFreelancerId, testProjectId)).thenReturn(false);

        favoriteService.addProjectToFavorites(testFreelancerId, testProjectId);

        verify(favProjRepo).save(argThat(fp ->
                fp.getFreelancer().getId().equals(testFreelancerId) &&
                        fp.getProject().getId().equals(testProjectId) &&
                        fp.getCreatedAt() != null
        ));
    }


    @Test
    void removeProjectFromFavorites_shouldCallDeleteWithCorrectParameters() {
        favoriteService.removeProjectFromFavorites(testFreelancerId, testProjectId);
        verify(favProjRepo).deleteByFreelancerIdAndProjectId(testFreelancerId, testProjectId);
    }

    @Test
    void getFavoriteProjects_shouldReturnMappedProjects() {
        Project project = new Project();
        project.setId(testProjectId);

        FavoriteProject fp = new FavoriteProject();
        fp.setProject(project);
        fp.setCreatedAt(LocalDateTime.now());

        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(testProjectId);
        when(favProjRepo.findByFreelancerId(testFreelancerId)).thenReturn(List.of(fp));
        when(projectMapper.toDto(project)).thenReturn(projectDto);
        List<ProjectDto> result = favoriteService.getFavoriteProjects(testFreelancerId);
        assertEquals(1, result.size());
        assertEquals(testProjectId, result.get(0).getId());
        verify(projectMapper).toDto(project);
    }

    @Test
    void addFreelancerToFavorites_shouldCreateAndSaveFavoriteFreelancer() {
        ClientProfile client = new ClientProfile();
        client.setId(testClientId);
        FreelancerProfile freelancer = new FreelancerProfile();
        freelancer.setId(testFavoriteFreelancerId);

        when(clientProfileRepo.findById(testClientId)).thenReturn(Optional.of(client));
        when(freelancerProfileRepo.findById(testFavoriteFreelancerId)).thenReturn(Optional.of(freelancer));
        when(favFrRepo.existsByClientIdAndFreelancerId(testClientId, testFavoriteFreelancerId)).thenReturn(false);

        favoriteService.addFreelancerToFavorites(testClientId, testFavoriteFreelancerId);

        verify(favFrRepo).save(argThat(ff ->
                ff.getClient().getId().equals(testClientId) &&
                        ff.getFreelancer().getId().equals(testFavoriteFreelancerId) &&
                        ff.getCreatedAt() != null
        ));
    }


    @Test
    void removeFreelancerFromFavorites_shouldCallDeleteWithCorrectParameters() {
        favoriteService.removeFreelancerFromFavorites(testClientId, testFavoriteFreelancerId);
        verify(favFrRepo).deleteByClientIdAndFreelancerId(testClientId, testFavoriteFreelancerId);
    }

    @Test
    void getFavoriteFreelancers_shouldReturnMappedFreelancers() {
        FreelancerProfile freelancer = new FreelancerProfile();
        freelancer.setId(testFavoriteFreelancerId);

        FavoriteFreelancer ff = new FavoriteFreelancer();
        ff.setFreelancer(freelancer);
        ff.setCreatedAt(LocalDateTime.now());

        FreelancerProfileDto freelancerDto = new FreelancerProfileDto();
        freelancerDto.setId(testFavoriteFreelancerId);

        when(favFrRepo.findByClientId(testClientId)).thenReturn(List.of(ff));
        when(freelancerMapper.toDto(freelancer)).thenReturn(freelancerDto);

        List<FreelancerProfileDto> result = favoriteService.getFavoriteFreelancers(testClientId);
        assertEquals(1, result.size());
        assertEquals(testFavoriteFreelancerId, result.get(0).getId());
        verify(freelancerMapper).toDto(freelancer);
    }

    @Test
    void getFavoriteProjects_shouldReturnEmptyListWhenNoFavorites() {
        when(favProjRepo.findByFreelancerId(testFreelancerId)).thenReturn(List.of());

        List<ProjectDto> result = favoriteService.getFavoriteProjects(testFreelancerId);

        assertTrue(result.isEmpty());
    }

    @Test
    void getFavoriteFreelancers_shouldReturnEmptyListWhenNoFavorites() {
        when(favFrRepo.findByClientId(testClientId)).thenReturn(List.of());

        List<FreelancerProfileDto> result = favoriteService.getFavoriteFreelancers(testClientId);

        assertTrue(result.isEmpty());
    }
}