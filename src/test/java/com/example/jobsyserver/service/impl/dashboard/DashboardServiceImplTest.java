package com.example.jobsyserver.service.impl.dashboard;

import com.example.jobsyserver.features.dashboard.service.impl.DashboardServiceImpl;
import com.example.jobsyserver.features.project.dto.ProjectApplicationDto;
import com.example.jobsyserver.features.project.dto.ProjectDetailDto;
import com.example.jobsyserver.features.project.dto.ProjectDto;
import com.example.jobsyserver.features.common.enums.ProjectApplicationStatus;
import com.example.jobsyserver.features.common.enums.ProjectStatus;
import com.example.jobsyserver.features.project.service.ProjectApplicationService;
import com.example.jobsyserver.features.project.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceImplTest {

    @Mock
    private ProjectService projectService;
    @Mock
    private ProjectApplicationService appService;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    private final Long testClientId = 1L;
    private final Long testFreelancerId = 2L;
    private final Long testProjectId = 3L;
    private final ProjectStatus testStatus = ProjectStatus.OPEN;
    private final ProjectApplicationStatus testAppStatus = ProjectApplicationStatus.PENDING;

    private final ProjectDto testProjectDto = new ProjectDto();
    private final ProjectApplicationDto testAppDto = new ProjectApplicationDto();

    @Test
    void getClientProjects_shouldReturnProjects() {
        when(projectService.getProjectsByClient(testClientId, testStatus))
                .thenReturn(List.of(testProjectDto));

        List<ProjectDto> result = dashboardService.getClientProjects(testClientId, testStatus);

        assertEquals(1, result.size());
        assertEquals(testProjectDto, result.get(0));
        verify(projectService).getProjectsByClient(testClientId, testStatus);
    }

    @Test
    void getClientProjectDetail_shouldReturnProjectDetail() {
        when(projectService.getProjectByIdAndClient(testProjectId, testClientId))
                .thenReturn(testProjectDto);
        when(appService.getResponsesForProject(testProjectId, null))
                .thenReturn(List.of(testAppDto));
        when(appService.getInvitationsForProject(testProjectId, null))
                .thenReturn(List.of(testAppDto));

        ProjectDetailDto result = dashboardService.getClientProjectDetail(testClientId, testProjectId);

        assertNotNull(result);
        assertEquals(testProjectDto, result.getProject());
        assertEquals(1, result.getResponses().size());
        assertEquals(1, result.getInvitations().size());
        assertEquals(testAppDto, result.getResponses().get(0));
        assertEquals(testAppDto, result.getInvitations().get(0));
    }

    @Test
    void getFreelancerProjects_shouldReturnProjects() {
        when(projectService.getProjectsForFreelancer(testFreelancerId, testStatus))
                .thenReturn(List.of(testProjectDto));

        List<ProjectDto> result = dashboardService.getFreelancerProjects(testFreelancerId, testStatus);

        assertEquals(1, result.size());
        assertEquals(testProjectDto, result.get(0));
        verify(projectService).getProjectsForFreelancer(testFreelancerId, testStatus);
    }

    @Test
    void getMyResponses_shouldReturnResponses() {
        when(appService.getResponsesByFreelancer(testFreelancerId, testAppStatus))
                .thenReturn(List.of(testAppDto));

        List<ProjectApplicationDto> result = dashboardService.getMyResponses(testFreelancerId, testAppStatus);

        assertEquals(1, result.size());
        assertEquals(testAppDto, result.get(0));
        verify(appService).getResponsesByFreelancer(testFreelancerId, testAppStatus);
    }

    @Test
    void getMyInvitations_shouldReturnInvitations() {
        when(appService.getInvitationsByFreelancer(testFreelancerId, testAppStatus))
                .thenReturn(List.of(testAppDto));

        List<ProjectApplicationDto> result = dashboardService.getMyInvitations(testFreelancerId, testAppStatus);

        assertEquals(1, result.size());
        assertEquals(testAppDto, result.get(0));
        verify(appService).getInvitationsByFreelancer(testFreelancerId, testAppStatus);
    }

    @Test
    void getClientProjects_shouldReturnEmptyListWhenNoProjects() {
        when(projectService.getProjectsByClient(testClientId, testStatus))
                .thenReturn(List.of());

        List<ProjectDto> result = dashboardService.getClientProjects(testClientId, testStatus);

        assertTrue(result.isEmpty());
    }

    @Test
    void getClientProjectDetail_shouldHandleEmptyResponsesAndInvitations() {
        when(projectService.getProjectByIdAndClient(testProjectId, testClientId))
                .thenReturn(testProjectDto);
        when(appService.getResponsesForProject(testProjectId, null))
                .thenReturn(List.of());
        when(appService.getInvitationsForProject(testProjectId, null))
                .thenReturn(List.of());

        ProjectDetailDto result = dashboardService.getClientProjectDetail(testClientId, testProjectId);

        assertNotNull(result);
        assertEquals(testProjectDto, result.getProject());
        assertTrue(result.getResponses().isEmpty());
        assertTrue(result.getInvitations().isEmpty());
    }
}