package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.project.ProjectApplicationDto;
import com.example.jobsyserver.dto.project.ProjectApplicationRequestDto;
import com.example.jobsyserver.enums.ProjectApplicationStatus;
import com.example.jobsyserver.exception.ProjectApplicationNotFoundException;
import com.example.jobsyserver.exception.ProjectNotFoundException;
import com.example.jobsyserver.exception.UserNotFoundException;
import com.example.jobsyserver.mapper.ProjectApplicationMapper;
import com.example.jobsyserver.model.FreelancerProfile;
import com.example.jobsyserver.model.Project;
import com.example.jobsyserver.model.ProjectApplication;
import com.example.jobsyserver.repository.FreelancerProfileRepository;
import com.example.jobsyserver.repository.ProjectApplicationRepository;
import com.example.jobsyserver.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectApplicationServiceImplTest {

    @Mock
    private ProjectApplicationRepository repository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private FreelancerProfileRepository freelancerProfileRepository;
    @Mock
    private ProjectApplicationMapper mapper;

    @InjectMocks
    private ProjectApplicationServiceImpl service;

    private final LocalDateTime now = LocalDateTime.now();

    @Test
    void createApplication_Success() {
        ProjectApplicationRequestDto dto = new ProjectApplicationRequestDto();
        dto.setProjectId(1L);
        dto.setFreelancerId(2L);
        dto.setStatus(ProjectApplicationStatus.PENDING);
        Project project = new Project();
        project.setId(1L);
        FreelancerProfile freelancer = new FreelancerProfile();
        freelancer.setId(2L);

        ProjectApplication entity = ProjectApplication.builder().status(dto.getStatus()).build();
        ProjectApplication saved = ProjectApplication.builder()
                .id(10L).project(project).freelancer(freelancer)
                .status(dto.getStatus()).createdAt(now).build();
        ProjectApplicationDto responseDto = new ProjectApplicationDto();
        responseDto.setId(10L);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(freelancerProfileRepository.findById(2L)).thenReturn(Optional.of(freelancer));
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(responseDto);
        ProjectApplicationDto result = service.createApplication(dto);
        assertNotNull(result);
        assertEquals(10L, result.getId());
    }

    @Test
    void createApplication_ProjectNotFound() {
        ProjectApplicationRequestDto dto = new ProjectApplicationRequestDto();
        dto.setProjectId(1L);
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProjectNotFoundException.class, () -> service.createApplication(dto));
    }

    @Test
    void createApplication_FreelancerNotFound() {
        ProjectApplicationRequestDto dto = new ProjectApplicationRequestDto();
        dto.setProjectId(1L);
        dto.setFreelancerId(2L);
        Project project = new Project();
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(freelancerProfileRepository.findById(2L)).thenReturn(Optional.empty());
        when(mapper.toEntity(dto)).thenReturn(new ProjectApplication());
        assertThrows(UserNotFoundException.class, () -> service.createApplication(dto));
    }

    @Test
    void getById_Success() {
        ProjectApplication application = ProjectApplication.builder().id(1L).build();
        ProjectApplicationDto dto = new ProjectApplicationDto();
        dto.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(application));
        when(mapper.toDto(application)).thenReturn(dto);
        ProjectApplicationDto result = service.getById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getById_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProjectApplicationNotFoundException.class, () -> service.getById(1L));
    }

    @Test
    void updateStatus_Success() {
        ProjectApplication application = ProjectApplication.builder().id(1L).status(ProjectApplicationStatus.PENDING).build();
        ProjectApplication saved = ProjectApplication.builder().id(1L).status(ProjectApplicationStatus.APPROVED).build();
        ProjectApplicationDto dto = new ProjectApplicationDto();
        dto.setId(1L);
        dto.setStatus(ProjectApplicationStatus.APPROVED);
        when(repository.findById(1L)).thenReturn(Optional.of(application));
        when(repository.save(application)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(dto);
        ProjectApplicationDto result = service.updateStatus(1L, ProjectApplicationStatus.APPROVED);
        assertNotNull(result);
        assertEquals(ProjectApplicationStatus.APPROVED, result.getStatus());
    }

    @Test
    void updateStatus_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProjectApplicationNotFoundException.class, () -> service.updateStatus(1L, ProjectApplicationStatus.APPROVED));
    }

    @Test
    void deleteApplication_Success() {
        ProjectApplication application = ProjectApplication.builder().id(1L).build();
        when(repository.findById(1L)).thenReturn(Optional.of(application));
        doNothing().when(repository).delete(application);
        service.deleteApplication(1L);
        verify(repository, times(1)).delete(application);
    }

    @Test
    void deleteApplication_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProjectApplicationNotFoundException.class, () -> service.deleteApplication(1L));
    }
}