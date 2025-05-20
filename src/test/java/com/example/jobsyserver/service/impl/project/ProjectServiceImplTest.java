package com.example.jobsyserver.service.impl.project;

import com.example.jobsyserver.features.project.dto.ProjectCreateDto;
import com.example.jobsyserver.features.project.dto.ProjectDto;
import com.example.jobsyserver.features.project.dto.ProjectUpdateDto;
import com.example.jobsyserver.features.common.enums.ProjectStatus;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.project.mapper.ProjectMapper;
import com.example.jobsyserver.features.client.model.ClientProfile;
import com.example.jobsyserver.features.project.model.Project;
import com.example.jobsyserver.features.project.service.impl.ProjectServiceImpl;
import com.example.jobsyserver.features.user.model.User;
import com.example.jobsyserver.features.client.repository.ClientProfileRepository;
import com.example.jobsyserver.features.project.repository.ProjectRepository;
import com.example.jobsyserver.features.project.service.ProjectSkillService;
import com.example.jobsyserver.features.auth.service.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @InjectMocks
    private ProjectServiceImpl service;

    @Mock private ProjectRepository projectRepository;
    @Mock private ClientProfileRepository clientProfileRepository;
    @Mock private ProjectMapper projectMapper;
    @Mock private SecurityService securityService;
    @Mock private ProjectSkillService projectSkillService;

    private User user;
    private ClientProfile profile;
    private Project draft;
    private Project openProj;
    private ProjectCreateDto createDto;
    private ProjectUpdateDto updateDto;

    @BeforeEach
    void setUp() {
        user      = User.builder().id(1L).build();
        profile   = ClientProfile.builder().id(1L).user(user).build();
        draft     = Project.builder().id(10L).client(profile).status(ProjectStatus.DRAFT).build();
        openProj  = Project.builder().id(20L).client(profile).status(ProjectStatus.OPEN).build();
        createDto = new ProjectCreateDto();
        updateDto = new ProjectUpdateDto();
        lenient().when(securityService.getCurrentUser())
                .thenReturn(user);
        lenient().when(projectSkillService.getSkillsByProject(anyLong()))
                .thenReturn(List.of());
    }

    @Test
    void getAllProjects_withAndWithoutStatus() {
        when(projectRepository.findAllWithEverything()).thenReturn(List.of(openProj));
        when(projectRepository.findAllWithEverythingByStatus(ProjectStatus.OPEN))
                .thenReturn(List.of(openProj));
        when(projectMapper.toDto(openProj)).thenReturn(new ProjectDto());
        var all = service.getAllProjects(null);
        assertEquals(1, all.size());
        var filtered = service.getAllProjects(ProjectStatus.OPEN);
        assertEquals(1, filtered.size());
        verify(projectRepository).findAllWithEverything();
        verify(projectRepository).findAllWithEverythingByStatus(ProjectStatus.OPEN);
    }

    @Test
    void getProjectById_foundAndNotFound() {
        when(projectRepository.findById(5L)).thenReturn(Optional.of(openProj));
        when(projectMapper.toDto(openProj)).thenReturn(new ProjectDto());
        assertNotNull(service.getProjectById(5L));
        when(projectRepository.findById(6L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getProjectById(6L));
    }

    @Test
    void getProjectByIdAndClient_correctAndWrongClient() {
        when(projectRepository.findById(7L)).thenReturn(Optional.of(openProj));
        when(projectMapper.toDto(openProj)).thenReturn(new ProjectDto());
        assertNotNull(service.getProjectByIdAndClient(7L, profile.getId()));
        assertThrows(ResourceNotFoundException.class,
                () -> service.getProjectByIdAndClient(7L, 999L));
    }

    @Test
    void getProjectsByClient_and_forFreelancer() {
        when(projectRepository.findByClientIdAndStatus(profile.getId(), ProjectStatus.OPEN))
                .thenReturn(List.of(openProj));
        when(projectRepository.findByAssignedFreelancerIdAndStatus(profile.getId(), ProjectStatus.OPEN))
                .thenReturn(List.of(openProj));
        when(projectMapper.toDto(openProj)).thenReturn(new ProjectDto());
        var byClient     = service.getProjectsByClient(profile.getId(), ProjectStatus.OPEN);
        var byFreelancer = service.getProjectsForFreelancer(profile.getId(), ProjectStatus.OPEN);
        assertEquals(1, byClient.size());
        assertEquals(1, byFreelancer.size());
    }

    @Test
    void createDraft_successAndProfileMissing() {
        when(clientProfileRepository.findByUser(user))
                .thenReturn(Optional.of(profile));
        when(projectMapper.toEntity(createDto)).thenReturn(draft);
        when(projectRepository.save(draft)).thenReturn(draft);
        when(projectMapper.toDto(draft)).thenReturn(new ProjectDto());
        ProjectDto dto = service.createDraft(createDto);
        assertNotNull(dto);
        verify(projectRepository).save(draft);
        when(clientProfileRepository.findByUser(user)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> service.createDraft(createDto));
    }

    @Test
    void updateDraft_successAndWrongStatus() {
        when(projectRepository.findById(10L)).thenReturn(Optional.of(draft));
        when(projectMapper.toDto(draft)).thenReturn(new ProjectDto());
        when(projectRepository.save(draft)).thenReturn(draft);
        ProjectDto dto = service.updateDraft(10L, updateDto);
        assertNotNull(dto);
        verify(projectMapper).toEntity(updateDto, draft);
        draft.setStatus(ProjectStatus.OPEN);
        assertThrows(IllegalStateException.class,
                () -> service.updateDraft(10L, updateDto));
    }

    @Test
    void updateProject_successAndErrors() {
        when(projectRepository.findById(20L)).thenReturn(Optional.of(openProj));
        when(projectMapper.toDto(openProj)).thenReturn(new ProjectDto());
        when(projectRepository.save(openProj)).thenReturn(openProj);
        ProjectDto dto = service.updateProject(20L, updateDto);
        assertNotNull(dto);
        verify(projectMapper).toEntity(updateDto, openProj);
        User other = User.builder().id(2L).build();
        when(securityService.getCurrentUser()).thenReturn(other);
        assertThrows(AccessDeniedException.class,
                () -> service.updateProject(20L, updateDto));
        when(securityService.getCurrentUser()).thenReturn(user);
        openProj.setStatus(ProjectStatus.DRAFT);
        assertThrows(IllegalStateException.class,
                () -> service.updateProject(20L, updateDto));
    }

    @Test
    void publish_changesStatusAndSaves() {
        when(projectRepository.findById(10L)).thenReturn(Optional.of(draft));
        when(projectRepository.getReferenceById(10L)).thenReturn(draft);
        when(projectMapper.toDto(draft)).thenReturn(new ProjectDto());
        when(projectRepository.save(draft)).thenReturn(draft);
        ProjectDto dto = service.publish(10L, updateDto);
        assertNotNull(dto);
        assertEquals(ProjectStatus.OPEN, draft.getStatus());
        verify(projectRepository, times(2)).save(draft);
    }

    @Test
    void deleteProject_successAndErrors() {
        when(projectRepository.findById(20L)).thenReturn(Optional.of(openProj));
        service.deleteProject(20L);
        verify(projectRepository).delete(openProj);
        User other = User.builder().id(3L).build();
        when(securityService.getCurrentUser()).thenReturn(other);
        assertThrows(AccessDeniedException.class,
                () -> service.deleteProject(20L));
        when(securityService.getCurrentUser()).thenReturn(user);
        openProj.setStatus(ProjectStatus.DRAFT);
        assertThrows(IllegalStateException.class,
                () -> service.deleteProject(20L));
    }
}