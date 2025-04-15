package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.project.ProjectApplicationDto;
import com.example.jobsyserver.dto.project.ProjectApplicationRequestDto;
import com.example.jobsyserver.enums.ProjectApplicationStatus;
import com.example.jobsyserver.mapper.ProjectApplicationMapper;
import com.example.jobsyserver.model.Project;
import com.example.jobsyserver.model.ProjectApplication;
import com.example.jobsyserver.model.User;
import com.example.jobsyserver.repository.ProjectApplicationRepository;
import com.example.jobsyserver.repository.ProjectRepository;
import com.example.jobsyserver.repository.UserRepository;
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
    private ProjectApplicationRepository applicationRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectApplicationMapper applicationMapper;

    @InjectMocks
    private ProjectApplicationServiceImpl applicationService;

    private final LocalDateTime testTime = LocalDateTime.now();

    @Test
    void createApplication_ValidData_ReturnsCreatedApplication() {
        ProjectApplicationRequestDto requestDto = new ProjectApplicationRequestDto();
        requestDto.setProjectId(1L);
        requestDto.setFreelancerId(1L);
        Project project = new Project();
        project.setId(1L);
        User freelancer = new User();
        freelancer.setId(1L);
        ProjectApplication application = createTestApplication(1L, ProjectApplicationStatus.PENDING);
        ProjectApplicationDto responseDto = mapToResponseDto(application);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userRepository.findById(1L)).thenReturn(Optional.of(freelancer));
        when(applicationMapper.toEntity(requestDto)).thenReturn(application);
        when(applicationRepository.save(application)).thenReturn(application);
        when(applicationMapper.toDto(application)).thenReturn(responseDto);
        ProjectApplicationDto result = applicationService.createApplication(requestDto);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(ProjectApplicationStatus.PENDING, result.getStatus());
        verify(applicationRepository, times(1)).save(application);
    }

    @Test
    void createApplication_ProjectNotFound_ThrowsException() {
        ProjectApplicationRequestDto requestDto = new ProjectApplicationRequestDto();
        requestDto.setProjectId(1L);
        requestDto.setFreelancerId(1L);
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () ->
                        applicationService.createApplication(requestDto),
                "Проект не найден");
    }

    @Test
    void getById_ExistingId_ReturnsApplication() {
        Long applicationId = 1L;
        ProjectApplication application = createTestApplication(applicationId, ProjectApplicationStatus.PENDING);
        ProjectApplicationDto responseDto = mapToResponseDto(application);
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(application));
        when(applicationMapper.toDto(application)).thenReturn(responseDto);
        ProjectApplicationDto result = applicationService.getById(applicationId);
        assertNotNull(result);
        assertEquals(applicationId, result.getId());
        verify(applicationRepository, times(1)).findById(applicationId);
    }

    @Test
    void getById_NonExistingId_ThrowsException() {
        Long applicationId = 999L;
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () ->
                        applicationService.getById(applicationId),
                "Заявка не найдена");
    }

    @Test
    void updateStatus_ValidStatus_UpdatesApplication() {
        Long applicationId = 1L;
        ProjectApplicationStatus newStatus = ProjectApplicationStatus.APPROVED;
        ProjectApplication application = createTestApplication(applicationId, ProjectApplicationStatus.PENDING);
        ProjectApplication updatedApplication = createTestApplication(applicationId, newStatus);
        ProjectApplicationDto responseDto = mapToResponseDto(updatedApplication);
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(application));
        when(applicationRepository.save(application)).thenReturn(updatedApplication);
        when(applicationMapper.toDto(updatedApplication)).thenReturn(responseDto);
        ProjectApplicationDto result = applicationService.updateStatus(applicationId, newStatus);
        assertNotNull(result);
        assertEquals(newStatus, result.getStatus());
        verify(applicationRepository, times(1)).save(application);
    }

    @Test
    void deleteApplication_ExistingId_DeletesApplication() {
        Long applicationId = 1L;
        ProjectApplication application = createTestApplication(applicationId, ProjectApplicationStatus.PENDING);
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(application));
        doNothing().when(applicationRepository).delete(application);
        applicationService.deleteApplication(applicationId);
        verify(applicationRepository, times(1)).delete(application);
    }

    @Test
    void deleteApplication_NonExistingId_ThrowsException() {
        Long applicationId = 999L;
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () ->
                        applicationService.deleteApplication(applicationId),
                "Заявка не найдена");
    }

    private ProjectApplication createTestApplication(Long id, ProjectApplicationStatus status) {
        return ProjectApplication.builder()
                .id(id)
                .project(new Project())
                .freelancer(new User())
                .status(status)
                .createdAt(testTime)
                .build();
    }

    private ProjectApplicationDto mapToResponseDto(ProjectApplication application) {
        return ProjectApplicationDto.builder()
                .id(application.getId())
                .projectId(application.getProject().getId())
                .freelancerId(application.getFreelancer().getId())
                .status(application.getStatus())
                .createdAt(application.getCreatedAt())
                .build();
    }
}