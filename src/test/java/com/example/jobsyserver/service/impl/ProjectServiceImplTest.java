package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.project.ProjectCreateDto;
import com.example.jobsyserver.dto.project.ProjectDto;
import com.example.jobsyserver.dto.project.ProjectUpdateDto;
import com.example.jobsyserver.enums.Complexity;
import com.example.jobsyserver.enums.PaymentType;
import com.example.jobsyserver.enums.ProjectDuration;
import com.example.jobsyserver.enums.ProjectStatus;
import com.example.jobsyserver.mapper.ProjectMapper;
import com.example.jobsyserver.model.Project;
import com.example.jobsyserver.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private ProjectServiceImpl projectServiceImpl;

    private final LocalDateTime testTime = LocalDateTime.now();

    @Test
    void getAllProjects_WithoutStatus_ReturnsAllProjects() {
        Project project1 = createTestProject(1L, ProjectStatus.OPEN);
        Project project2 = createTestProject(2L, ProjectStatus.IN_PROGRESS);
        List<Project> projects = List.of(project1, project2);
        when(projectRepository.findAll()).thenReturn(projects);
        when(projectMapper.toDto(any(Project.class)))
                .thenAnswer(inv -> mapToDto(inv.getArgument(0)));
        List<ProjectDto> result = projectServiceImpl.getAllProjects(null);
        assertEquals(2, result.size());
        verify(projectRepository, times(1)).findAll();
        verify(projectRepository, never()).findByStatus(any());
    }

    @Test
    void createProject_ValidData_ReturnsCreatedProject() {
        ProjectCreateDto createDto = new ProjectCreateDto();
        createDto.setTitle("Test Project");
        createDto.setPaymentType(PaymentType.FIXED);
        createDto.setFixedPrice(BigDecimal.valueOf(1000));
        Project project = createTestProject(1L, ProjectStatus.OPEN);
        ProjectDto responseDto = mapToDto(project);
        when(projectMapper.toEntity(createDto)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.toDto(project)).thenReturn(responseDto);
        ProjectDto result = projectServiceImpl.createProject(createDto);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(ProjectStatus.OPEN, result.getStatus());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void updateProject_OpenStatus_UpdatesProject() {
        Long projectId = 1L;
        ProjectUpdateDto updateDto = new ProjectUpdateDto();
        updateDto.setTitle("Updated Title");
        updateDto.setDescription("Updated Desc");
        Project existingProject = createTestProject(projectId, ProjectStatus.OPEN);
        Project updatedProject = createTestProject(projectId, ProjectStatus.OPEN);
        updatedProject.setTitle("Updated Title");
        updatedProject.setDescription("Updated Desc");
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(existingProject)).thenReturn(updatedProject);
        when(projectMapper.toDto(updatedProject)).thenReturn(mapToDto(updatedProject));
        ProjectDto result = projectServiceImpl.updateProject(projectId, updateDto);
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Desc", result.getDescription());
        verify(projectMapper, times(1)).toEntity(updateDto, existingProject);
    }

    @Test
    void updateProject_NotOpenStatus_ThrowsException() {
        Long projectId = 1L;
        ProjectUpdateDto updateDto = new ProjectUpdateDto();
        Project existingProject = createTestProject(projectId, ProjectStatus.IN_PROGRESS);
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));
        assertThrows(RuntimeException.class, () ->
                        projectServiceImpl.updateProject(projectId, updateDto),
                "Можно обновлять только проекты со статусом \"open\".");

        verify(projectRepository, never()).save(any());
    }

    @Test
    void deleteProject_OpenStatus_DeletesProject() {
        Long projectId = 1L;
        Project project = createTestProject(projectId, ProjectStatus.OPEN);
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        projectServiceImpl.deleteProject(projectId);
        verify(projectRepository, times(1)).delete(project);
    }

    @Test
    void deleteProject_NotOpenStatus_ThrowsException() {
        Long projectId = 1L;
        Project project = createTestProject(projectId, ProjectStatus.COMPLETED);
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        assertThrows(RuntimeException.class, () ->
                        projectServiceImpl.deleteProject(projectId),
                "Можно удалить только проекты со статусом \"open\".");

        verify(projectRepository, never()).delete(any());
    }

    private Project createTestProject(Long id, ProjectStatus status) {
        return Project.builder()
                .id(id)
                .title("Test Project")
                .description("Test Description")
                .projectComplexity(Complexity.MEDIUM)
                .paymentType(PaymentType.FIXED)
                .fixedPrice(BigDecimal.valueOf(1000))
                .projectDuration(ProjectDuration.LESS_THAN_1_MONTH)
                .status(status)
                .createdAt(testTime)
                .updatedAt(testTime)
                .build();
    }

    private ProjectDto mapToDto(Project project) {
        return ProjectDto.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .complexity(project.getProjectComplexity())
                .paymentType(project.getPaymentType())
                .minRate(project.getMinRate())
                .maxRate(project.getMaxRate())
                .fixedPrice(project.getFixedPrice())
                .duration(project.getProjectDuration())
                .status(project.getStatus())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}