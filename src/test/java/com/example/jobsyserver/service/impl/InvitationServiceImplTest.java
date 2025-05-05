package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.project.ProjectApplicationDto;
import com.example.jobsyserver.enums.ApplicationType;
import com.example.jobsyserver.enums.ProjectApplicationStatus;
import com.example.jobsyserver.enums.ProjectStatus;
import com.example.jobsyserver.exception.ResourceNotFoundException;
import com.example.jobsyserver.mapper.ProjectApplicationMapper;
import com.example.jobsyserver.model.FreelancerProfile;
import com.example.jobsyserver.model.Project;
import com.example.jobsyserver.model.ProjectApplication;
import com.example.jobsyserver.repository.FreelancerProfileRepository;
import com.example.jobsyserver.repository.ProjectApplicationRepository;
import com.example.jobsyserver.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class InvitationServiceImplTest {

    @InjectMocks
    private InvitationServiceImpl invitationService;

    @Mock
    private ProjectApplicationRepository applicationRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private FreelancerProfileRepository freelancerProfileRepository;

    @Mock
    private ProjectApplicationMapper mapper;

    private Project project;
    private FreelancerProfile freelancer;
    private ProjectApplication projectApplication;

    @BeforeEach
    void setup() {
        project = new Project();
        project.setId(1L);
        project.setStatus(ProjectStatus.OPEN);

        freelancer = new FreelancerProfile();
        freelancer.setId(1L);

        projectApplication = new ProjectApplication();
        projectApplication.setId(1L);
        projectApplication.setProject(project);
        projectApplication.setFreelancer(freelancer);
        projectApplication.setApplicationType(ApplicationType.INVITATION);
        projectApplication.setStatus(ProjectApplicationStatus.PENDING);
    }

    @Test
    @Transactional
    void invite_ShouldReturnProjectApplicationDto_WhenValidData() {
        when(projectRepository.findById(1L)).thenReturn(java.util.Optional.of(project));
        when(freelancerProfileRepository.findById(1L)).thenReturn(java.util.Optional.of(freelancer));
        when(applicationRepository.save(any(ProjectApplication.class))).thenReturn(projectApplication);
        when(mapper.toDto(any(ProjectApplication.class))).thenReturn(new ProjectApplicationDto());
        ProjectApplicationDto response = invitationService.invite(1L, 1L);
        assertNotNull(response);
        verify(applicationRepository, times(1)).save(any(ProjectApplication.class));
    }

    @Test
    void invite_ShouldThrowResourceNotFoundException_WhenProjectNotFound() {
        when(projectRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> invitationService.invite(1L, 1L));
    }

    @Test
    void invite_ShouldThrowResourceNotFoundException_WhenFreelancerNotFound() {
        when(projectRepository.findById(1L)).thenReturn(java.util.Optional.of(project));
        when(freelancerProfileRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> invitationService.invite(1L, 1L));
    }

    @Test
    @Transactional
    void handleInvitationStatus_ShouldUpdateApplicationStatus_WhenValidStatus() {
        when(applicationRepository.findById(1L)).thenReturn(java.util.Optional.of(projectApplication));
        when(applicationRepository.save(any(ProjectApplication.class))).thenReturn(projectApplication);
        when(mapper.toDto(any(ProjectApplication.class))).thenReturn(new ProjectApplicationDto());
        ProjectApplicationDto response = invitationService.handleInvitationStatus(1L, ProjectApplicationStatus.APPROVED);
        assertNotNull(response);
        assertEquals(ProjectApplicationStatus.APPROVED, projectApplication.getStatus());
        verify(applicationRepository, times(1)).save(any(ProjectApplication.class));
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void handleInvitationStatus_ShouldThrowIllegalArgumentException_WhenNotInvitation() {
        projectApplication.setApplicationType(ApplicationType.RESPONSE);
        when(applicationRepository.findById(1L)).thenReturn(java.util.Optional.of(projectApplication));
        assertThrows(IllegalArgumentException.class, () -> invitationService.handleInvitationStatus(1L, ProjectApplicationStatus.APPROVED));
    }

    @Test
    void handleInvitationStatus_ShouldThrowResourceNotFoundException_WhenApplicationNotFound() {
        when(applicationRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> invitationService.handleInvitationStatus(1L, ProjectApplicationStatus.APPROVED));
    }
}
