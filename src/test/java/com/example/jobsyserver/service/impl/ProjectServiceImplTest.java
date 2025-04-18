package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.project.ProjectCreateDto;
import com.example.jobsyserver.dto.project.ProjectDto;
import com.example.jobsyserver.dto.project.ProjectUpdateDto;
import com.example.jobsyserver.enums.ProjectStatus;
import com.example.jobsyserver.exception.ProjectNotFoundException;
import com.example.jobsyserver.exception.UserNotFoundException;
import com.example.jobsyserver.mapper.ProjectMapper;
import com.example.jobsyserver.model.*;
import com.example.jobsyserver.repository.*;
import com.example.jobsyserver.service.SecurityService;
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
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(clientProfileRepository.findByUser(user)).thenReturn(Optional.of(profile));
        when(projectMapper.toEntity(createDto)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.toDto(project)).thenReturn(new ProjectDto());

        ProjectDto result = service.createProject(createDto);
        assertNotNull(result);
        verify(projectRepository).save(project);
    }

    @Test
    void createProject_userNotFound() {
        when(securityService.getCurrentUserEmail()).thenReturn("no@mail.com");
        when(userRepository.findByEmail("no@mail.com")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> service.createProject(createDto));
    }

    @Test
    void createProject_profileNotFound() {
        when(securityService.getCurrentUserEmail()).thenReturn("test@mail.com");
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(clientProfileRepository.findByUser(user)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.createProject(createDto));
    }

    @Test
    void updateProject_success() {
        when(securityService.getCurrentUserEmail()).thenReturn("test@mail.com");
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectMapper.toEntity(updateDto, project)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.toDto(project)).thenReturn(new ProjectDto());
        ProjectDto result = service.updateProject(1L, updateDto);
        assertNotNull(result);
    }

    @Test
    void updateProject_notOwner_throwsException() {
        User anotherUser = User.builder().id(999L).email("other@mail.com").build();
        when(securityService.getCurrentUserEmail()).thenReturn("other@mail.com");
        when(userRepository.findByEmail("other@mail.com")).thenReturn(Optional.of(anotherUser));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.updateProject(1L, updateDto));
        assertEquals("Вы не являетесь владельцем проекта", exception.getMessage());
    }

    @Test
    void updateProject_projectNotFound() {
        when(securityService.getCurrentUserEmail()).thenReturn("test@mail.com");
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProjectNotFoundException.class, () -> service.updateProject(1L, updateDto));
    }

    @Test
    void deleteProject_success() {
        when(securityService.getCurrentUserEmail()).thenReturn("test@mail.com");
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        service.deleteProject(1L);
        verify(projectRepository).delete(project);
    }

    @Test
    void deleteProject_notOwner_throwsException() {
        User anotherUser = User.builder().id(2L).email("other@mail.com").build();
        when(securityService.getCurrentUserEmail()).thenReturn("other@mail.com");
        when(userRepository.findByEmail("other@mail.com")).thenReturn(Optional.of(anotherUser));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.deleteProject(1L));
        assertEquals("Вы не являетесь владельцем проекта", ex.getMessage());
    }

    @Test
    void getAllProjects_shouldReturnAll_whenStatusNull() {
        when(projectRepository.findAll()).thenReturn(List.of(project));
        when(projectMapper.toDto(project)).thenReturn(new ProjectDto());
        List<ProjectDto> result = service.getAllProjects(null);
        assertEquals(1, result.size());
    }

    @Test
    void getAllProjects_shouldReturnByStatus() {
        when(projectRepository.findByStatus(ProjectStatus.OPEN)).thenReturn(List.of(project));
        when(projectMapper.toDto(project)).thenReturn(new ProjectDto());
        List<ProjectDto> result = service.getAllProjects(ProjectStatus.OPEN);
        assertEquals(1, result.size());
    }
}