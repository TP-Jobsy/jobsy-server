package com.example.jobsyserver.features.dashboard.service.impl;

import com.example.jobsyserver.features.project.dto.ProjectApplicationDto;
import com.example.jobsyserver.features.project.dto.ProjectDetailDto;
import com.example.jobsyserver.features.project.dto.ProjectDto;
import com.example.jobsyserver.features.common.enums.ProjectApplicationStatus;
import com.example.jobsyserver.features.common.enums.ProjectStatus;
import com.example.jobsyserver.features.dashboard.service.DashboardService;
import com.example.jobsyserver.features.project.service.ProjectApplicationService;
import com.example.jobsyserver.features.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {
    private final ProjectService projectService;
    private final ProjectApplicationService appService;

    @Override
    public List<ProjectDto> getClientProjects(Long clientId, ProjectStatus status) {
        return projectService.getProjectsByClient(clientId, status);
    }

    @Override
    public ProjectDetailDto getClientProjectDetail(Long clientId, Long projectId) {
        var projectDto = projectService.getProjectByIdAndClient(projectId, clientId);
        var responses = appService.getResponsesForProject(projectId, ProjectApplicationStatus.PENDING);
        var invites = appService.getInvitationsForProject(projectId, ProjectApplicationStatus.PENDING);
        return ProjectDetailDto.builder()
                .project(projectDto)
                .responses(responses)
                .invitations(invites)
                .clientCompleted(projectDto.isClientCompleted())
                .freelancerCompleted(projectDto.isFreelancerCompleted())
                .build();
    }

    @Override
    public List<ProjectDto> getFreelancerProjects(Long frId, ProjectStatus status) {
        return projectService.getProjectsForFreelancer(frId, status);
    }

    @Override
    public List<ProjectApplicationDto> getMyResponses(Long frId, ProjectApplicationStatus status) {
        return appService.getResponsesByFreelancer(frId, status);
    }

    @Override
    public List<ProjectApplicationDto> getMyInvitations(Long frId, ProjectApplicationStatus status) {
        return appService.getInvitationsByFreelancer(frId, status);
    }
}