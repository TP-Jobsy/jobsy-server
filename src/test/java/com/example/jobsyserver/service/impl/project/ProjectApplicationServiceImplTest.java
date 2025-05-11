package com.example.jobsyserver.service.impl.project;

import com.example.jobsyserver.features.project.dto.ProjectApplicationDto;
import com.example.jobsyserver.features.common.enums.ApplicationType;
import com.example.jobsyserver.features.common.enums.ProjectApplicationStatus;
import com.example.jobsyserver.features.project.mapper.ProjectApplicationMapper;
import com.example.jobsyserver.features.project.model.ProjectApplication;
import com.example.jobsyserver.features.project.repository.ProjectApplicationRepository;
import com.example.jobsyserver.features.project.service.impl.ProjectApplicationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectApplicationServiceImplTest {

    @InjectMocks
    private ProjectApplicationServiceImpl service;

    @Mock
    private ProjectApplicationRepository repo;
    @Mock
    private ProjectApplicationMapper mapper;

    private final Long testProjectId = 1L;
    private final Long testFreelancerId = 2L;
    private final ProjectApplicationStatus testStatus = ProjectApplicationStatus.PENDING;

    private ProjectApplication createMockApplication() {
        return mock(ProjectApplication.class);
    }

    @Test
    void getResponsesForProject_withStatus() {
        ProjectApplication entity = createMockApplication();
        ProjectApplicationDto dto = new ProjectApplicationDto();

        when(repo.findByProjectIdAndApplicationTypeAndStatus(testProjectId, ApplicationType.RESPONSE, testStatus))
                .thenReturn(List.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        List<ProjectApplicationDto> result = service.getResponsesForProject(testProjectId, testStatus);

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
        verify(repo).findByProjectIdAndApplicationTypeAndStatus(testProjectId, ApplicationType.RESPONSE, testStatus);
    }

    @Test
    void getResponsesForProject_withoutStatus() {
        ProjectApplication entity = createMockApplication();
        ProjectApplicationDto dto = new ProjectApplicationDto();

        when(repo.findByProjectIdAndApplicationType(testProjectId, ApplicationType.RESPONSE))
                .thenReturn(List.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        List<ProjectApplicationDto> result = service.getResponsesForProject(testProjectId, null);

        assertEquals(1, result.size());
        verify(repo).findByProjectIdAndApplicationType(testProjectId, ApplicationType.RESPONSE);
    }

    @Test
    void getInvitationsForProject_withStatus() {
        ProjectApplication entity = createMockApplication();
        ProjectApplicationDto dto = new ProjectApplicationDto();

        when(repo.findByProjectIdAndApplicationTypeAndStatus(testProjectId, ApplicationType.INVITATION, testStatus))
                .thenReturn(List.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        List<ProjectApplicationDto> result = service.getInvitationsForProject(testProjectId, testStatus);

        assertEquals(1, result.size());
        verify(repo).findByProjectIdAndApplicationTypeAndStatus(testProjectId, ApplicationType.INVITATION, testStatus);
    }

    @Test
    void getInvitationsForProject_withoutStatus() {
        ProjectApplication entity = createMockApplication();
        ProjectApplicationDto dto = new ProjectApplicationDto();

        when(repo.findByProjectIdAndApplicationType(testProjectId, ApplicationType.INVITATION))
                .thenReturn(List.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        List<ProjectApplicationDto> result = service.getInvitationsForProject(testProjectId, null);

        assertEquals(1, result.size());
        verify(repo).findByProjectIdAndApplicationType(testProjectId, ApplicationType.INVITATION);
    }

    @Test
    void getResponsesByFreelancer_withStatus() {
        ProjectApplication entity = createMockApplication();
        ProjectApplicationDto dto = new ProjectApplicationDto();

        when(repo.findByFreelancerIdAndApplicationTypeAndStatus(testFreelancerId, ApplicationType.RESPONSE, testStatus))
                .thenReturn(List.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        List<ProjectApplicationDto> result = service.getResponsesByFreelancer(testFreelancerId, testStatus);

        assertEquals(1, result.size());
        verify(repo).findByFreelancerIdAndApplicationTypeAndStatus(testFreelancerId, ApplicationType.RESPONSE, testStatus);
    }

    @Test
    void getResponsesByFreelancer_withoutStatus() {
        ProjectApplication entity = createMockApplication();
        ProjectApplicationDto dto = new ProjectApplicationDto();

        when(repo.findByFreelancerIdAndApplicationType(testFreelancerId, ApplicationType.RESPONSE))
                .thenReturn(List.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        List<ProjectApplicationDto> result = service.getResponsesByFreelancer(testFreelancerId, null);

        assertEquals(1, result.size());
        verify(repo).findByFreelancerIdAndApplicationType(testFreelancerId, ApplicationType.RESPONSE);
    }

    @Test
    void getInvitationsByFreelancer_withStatus() {
        ProjectApplication entity = createMockApplication();
        ProjectApplicationDto dto = new ProjectApplicationDto();

        when(repo.findByFreelancerIdAndApplicationTypeAndStatus(testFreelancerId, ApplicationType.INVITATION, testStatus))
                .thenReturn(List.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        List<ProjectApplicationDto> result = service.getInvitationsByFreelancer(testFreelancerId, testStatus);

        assertEquals(1, result.size());
        verify(repo).findByFreelancerIdAndApplicationTypeAndStatus(testFreelancerId, ApplicationType.INVITATION, testStatus);
    }

    @Test
    void getInvitationsByFreelancer_withoutStatus() {
        ProjectApplication entity = createMockApplication();
        ProjectApplicationDto dto = new ProjectApplicationDto();

        when(repo.findByFreelancerIdAndApplicationType(testFreelancerId, ApplicationType.INVITATION))
                .thenReturn(List.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        List<ProjectApplicationDto> result = service.getInvitationsByFreelancer(testFreelancerId, null);

        assertEquals(1, result.size());
        verify(repo).findByFreelancerIdAndApplicationType(testFreelancerId, ApplicationType.INVITATION);
    }
}