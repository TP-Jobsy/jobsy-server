package com.example.jobsyserver.features.dashboard.service;

import com.example.jobsyserver.features.project.dto.ProjectApplicationDto;
import com.example.jobsyserver.features.project.dto.ProjectDetailDto;
import com.example.jobsyserver.features.project.dto.ProjectDto;
import com.example.jobsyserver.features.common.enums.ProjectApplicationStatus;
import com.example.jobsyserver.features.common.enums.ProjectStatus;

import java.util.List;

public interface DashboardService {
    List<ProjectDto> getClientProjects(Long clientProfileId, ProjectStatus status);
    ProjectDetailDto getClientProjectDetail(Long clientProfileId, Long projectId);
    List<ProjectDto> getFreelancerProjects(Long freelancerProfileId, ProjectStatus status);
    List<ProjectApplicationDto> getMyResponses(Long freelancerProfileId, ProjectApplicationStatus status);
    List<ProjectApplicationDto> getMyInvitations(Long freelancerProfileId, ProjectApplicationStatus status);
}
