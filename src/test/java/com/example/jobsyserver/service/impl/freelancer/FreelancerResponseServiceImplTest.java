package com.example.jobsyserver.service.impl.freelancer;

import com.example.jobsyserver.features.freelancer.service.impl.FreelancerResponseServiceImpl;
import com.example.jobsyserver.features.project.dto.ProjectApplicationDto;
import com.example.jobsyserver.features.common.enums.ApplicationType;
import com.example.jobsyserver.features.common.enums.ProjectApplicationStatus;
import com.example.jobsyserver.features.common.enums.ProjectStatus;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.project.mapper.ProjectApplicationMapper;
import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.project.model.Project;
import com.example.jobsyserver.features.project.model.ProjectApplication;
import com.example.jobsyserver.features.freelancer.repository.FreelancerProfileRepository;
import com.example.jobsyserver.features.project.repository.ProjectApplicationRepository;
import com.example.jobsyserver.features.project.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FreelancerResponseServiceImplTest {

    @Mock
    private ProjectApplicationRepository applicationRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private FreelancerProfileRepository freelancerProfileRepository;

    @Mock
    private ProjectApplicationMapper mapper;

    @InjectMocks
    private FreelancerResponseServiceImpl freelancerResponseService;

    private Project project;
    private FreelancerProfile freelancer;
    private ProjectApplication projectApplication;

    @BeforeEach
    void setup() {
        project = Project.builder()
                .id(1L)
                .status(ProjectStatus.OPEN)
                .build();
        freelancer = FreelancerProfile.builder()
                .id(1L)
                .build();
        projectApplication = ProjectApplication.builder()
                .id(1L)
                .project(project)
                .freelancer(freelancer)
                .applicationType(ApplicationType.RESPONSE)
                .status(ProjectApplicationStatus.PENDING)
                .build();
    }

    @Test
    void respond_ShouldCreateProjectApplication_WhenValidData() {
        when(projectRepository.findById(1L)).thenReturn(java.util.Optional.of(project));
        when(freelancerProfileRepository.findById(1L)).thenReturn(java.util.Optional.of(freelancer));
        when(applicationRepository.save(any(ProjectApplication.class))).thenReturn(projectApplication);
        when(mapper.toDto(any(ProjectApplication.class))).thenReturn(new ProjectApplicationDto());

        ProjectApplicationDto response = freelancerResponseService.respond(1L, 1L);
        verify(applicationRepository, times(1)).save(any(ProjectApplication.class));
        assertEquals(ApplicationType.RESPONSE, projectApplication.getApplicationType());
        assertEquals(ProjectApplicationStatus.PENDING, projectApplication.getStatus());
        assertNotNull(response);
    }

    @Test
    void respond_ShouldThrowResourceNotFoundException_WhenProjectNotFound() {
        when(projectRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> freelancerResponseService.respond(1L, 1L));
    }

    @Test
    void respond_ShouldThrowResourceNotFoundException_WhenFreelancerNotFound() {
        when(projectRepository.findById(1L)).thenReturn(java.util.Optional.of(project));
        when(freelancerProfileRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> freelancerResponseService.respond(1L, 1L));
    }

    @Test
    void handleResponseStatus_ShouldUpdateApplicationStatus_WhenValidStatus() {
        projectApplication.setStatus(ProjectApplicationStatus.PENDING);
        when(applicationRepository.findById(1L)).thenReturn(java.util.Optional.of(projectApplication));
        when(mapper.toDto(any(ProjectApplication.class))).thenReturn(new ProjectApplicationDto());
        when(applicationRepository.save(any(ProjectApplication.class))).thenReturn(projectApplication);
        ProjectApplicationDto response = freelancerResponseService.handleResponseStatus(1L, ProjectApplicationStatus.APPROVED);
        verify(applicationRepository, times(1)).save(any(ProjectApplication.class));
        assertEquals(ProjectApplicationStatus.APPROVED, projectApplication.getStatus());
        assertEquals(ProjectStatus.IN_PROGRESS, project.getStatus());
        assertEquals(freelancer, project.getAssignedFreelancer());
        assertNotNull(response);
    }


    @Test
    void handleResponseStatus_ShouldThrowIllegalArgumentException_WhenApplicationIsNotResponse() {
        projectApplication.setApplicationType(ApplicationType.INVITATION);
        when(applicationRepository.findById(1L)).thenReturn(java.util.Optional.of(projectApplication));
        assertThrows(IllegalArgumentException.class, () -> freelancerResponseService.handleResponseStatus(1L, ProjectApplicationStatus.APPROVED));
    }

    @Test
    void handleResponseStatus_ShouldThrowResourceNotFoundException_WhenApplicationNotFound() {
        when(applicationRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> freelancerResponseService.handleResponseStatus(1L, ProjectApplicationStatus.APPROVED));
    }
}
