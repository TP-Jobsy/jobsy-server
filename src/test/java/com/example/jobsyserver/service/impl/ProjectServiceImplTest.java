package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.project.ProjectCreateDto;
import com.example.jobsyserver.dto.project.ProjectDto;
import com.example.jobsyserver.dto.project.ProjectUpdateDto;
import com.example.jobsyserver.enums.ProjectStatus;
import com.example.jobsyserver.exception.ResourceNotFoundException;
import com.example.jobsyserver.mapper.ProjectMapper;
import com.example.jobsyserver.model.ClientProfile;
import com.example.jobsyserver.model.Project;
import com.example.jobsyserver.model.User;
import com.example.jobsyserver.repository.ClientProfileRepository;
import com.example.jobsyserver.repository.ProjectRepository;
import com.example.jobsyserver.repository.UserRepository;
import com.example.jobsyserver.service.SecurityService;
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

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private ClientProfileRepository clientProfileRepository;
    @Mock
    private ProjectMapper projectMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SecurityService securityService;

    private User user;
    private ClientProfile profile;
    private Project project;
    private ProjectCreateDto createDto;
    private ProjectUpdateDto updateDto;

    @BeforeEach
    void setup() {
        user = User.builder().id(1L).email("test@mail.com").build();
        profile = ClientProfile.builder().id(1L).user(user).build();
        project = Project.builder().id(1L).client(profile).status(ProjectStatus.OPEN).build();
        createDto = new ProjectCreateDto();
        updateDto = new ProjectUpdateDto();
    }

    @Test
    void createProject_success() {
        when(securityService.getCurrentUserEmail()).thenReturn("test@mail.com");
        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));
        when(clientProfileRepository.findByUser(user))
                .thenReturn(Optional.of(profile));
        when(projectMapper.toEntity(createDto)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.toDto(project)).thenReturn(new ProjectDto());
        ProjectDto dto = service.createProject(createDto);
        assertNotNull(dto);
        verify(projectRepository).save(project);
    }

    @Test
    void createProject_userNotFound() {
        when(securityService.getCurrentUserEmail()).thenReturn("no@mail.com");
        when(userRepository.findByEmail("no@mail.com"))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> service.createProject(createDto));
    }

    @Test
    void createProject_profileNotFound() {
        when(securityService.getCurrentUserEmail()).thenReturn("test@mail.com");
        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));
        when(clientProfileRepository.findByUser(user))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> service.createProject(createDto));
    }

    @Test
    void updateProject_success() {
        when(securityService.getCurrentUserEmail()).thenReturn("test@mail.com");
        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));
        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.toDto(project)).thenReturn(new ProjectDto());
        ProjectDto dto = service.updateProject(1L, updateDto);
        assertNotNull(dto);
        verify(projectMapper).toEntity(updateDto, project);
        verify(projectRepository).save(project);
    }

    @Test
    void updateProject_notOwner_throwsAccessDenied() {
        User other = User.builder().id(2L).email("other@mail.com").build();
        when(securityService.getCurrentUserEmail()).thenReturn("other@mail.com");
        when(userRepository.findByEmail("other@mail.com"))
                .thenReturn(Optional.of(other));
        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        AccessDeniedException ex = assertThrows(
                AccessDeniedException.class,
                () -> service.updateProject(1L, updateDto)
        );
        assertEquals("Не ваш проект", ex.getMessage());
    }

    @Test
    void updateProject_notFound_throwsNotFound() {
        when(securityService.getCurrentUserEmail()).thenReturn("test@mail.com");
        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));
        when(projectRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.updateProject(1L, updateDto));
    }

    @Test
    void deleteProject_success() {
        when(securityService.getCurrentUserEmail()).thenReturn("test@mail.com");
        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));
        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        service.deleteProject(1L);

        verify(projectRepository).delete(project);
    }

    @Test
    void deleteProject_notOwner_throwsAccessDenied() {
        User other = User.builder().id(3L).email("other@mail.com").build();
        when(securityService.getCurrentUserEmail()).thenReturn("other@mail.com");
        when(userRepository.findByEmail("other@mail.com"))
                .thenReturn(Optional.of(other));
        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        AccessDeniedException ex = assertThrows(
                AccessDeniedException.class,
                () -> service.deleteProject(1L)
        );
        assertEquals("Не ваш проект", ex.getMessage());
    }

    @Test
    void getAllProjects_noStatus_returnsAll() {
        when(projectRepository.findAll()).thenReturn(List.of(project));
        when(projectMapper.toDto(project)).thenReturn(new ProjectDto());
        List<ProjectDto> all = service.getAllProjects(null);
        assertEquals(1, all.size());
    }

    @Test
    void getAllProjects_withStatus_returnsFiltered() {
        when(projectRepository.findByStatus(ProjectStatus.OPEN))
                .thenReturn(List.of(project));
        when(projectMapper.toDto(project)).thenReturn(new ProjectDto());
        List<ProjectDto> onlyOpen = service.getAllProjects(ProjectStatus.OPEN);
        assertEquals(1, onlyOpen.size());
    }

    @Test
    void getProjectById_success() {
        when(projectRepository.findById(99L))
                .thenReturn(Optional.of(project));
        when(projectMapper.toDto(project)).thenReturn(new ProjectDto());
        ProjectDto dto = service.getProjectById(99L);
        assertNotNull(dto);
    }

    @Test
    void getProjectById_notFound() {
        when(projectRepository.findById(77L))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> service.getProjectById(77L));
    }

    @Test
    void getProjectByIdAndClient_success() {
        when(projectRepository.findById(5L))
                .thenReturn(Optional.of(project));
        when(projectMapper.toDto(project)).thenReturn(new ProjectDto());
        ProjectDto dto = service.getProjectByIdAndClient(5L, profile.getId());
        assertNotNull(dto);
    }

    @Test
    void getProjectByIdAndClient_wrongClient_throwsNotFound() {
        when(projectRepository.findById(6L))
                .thenReturn(Optional.of(project));
        assertThrows(ResourceNotFoundException.class,
                () -> service.getProjectByIdAndClient(6L, 999L));
    }

    @Test
    void getProjectsByClient_andForFreelancer() {
        when(projectRepository.findByClientIdAndStatus(profile.getId(), ProjectStatus.OPEN))
                .thenReturn(List.of(project));
        when(projectRepository.findByAssignedFreelancerIdAndStatus(profile.getId(), ProjectStatus.OPEN))
                .thenReturn(List.of(project));
        when(projectMapper.toDto(project)).thenReturn(new ProjectDto());
        var byClient = service.getProjectsByClient(profile.getId(), ProjectStatus.OPEN);
        var byFreelancer = service.getProjectsForFreelancer(profile.getId(), ProjectStatus.OPEN);
        assertEquals(1, byClient.size());
        assertEquals(1, byFreelancer.size());
    }
}