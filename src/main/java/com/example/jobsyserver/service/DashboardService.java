package com.example.jobsyserver.service;

import com.example.jobsyserver.dto.project.ProjectApplicationDto;
import com.example.jobsyserver.dto.project.ProjectDetailDto;
import com.example.jobsyserver.dto.project.ProjectDto;
import com.example.jobsyserver.enums.ProjectApplicationStatus;
import com.example.jobsyserver.enums.ProjectStatus;

import java.util.List;

public interface DashboardService {
    List<ProjectDto> getClientProjects(Long clientProfileId, ProjectStatus status);
    ProjectDetailDto getClientProjectDetail(Long clientProfileId, Long projectId);
    List<ProjectDto> getFreelancerProjects(Long freelancerProfileId, ProjectStatus status);
    List<ProjectApplicationDto> getMyResponses(Long freelancerProfileId, ProjectApplicationStatus status);
    List<ProjectApplicationDto> getMyInvitations(Long freelancerProfileId, ProjectApplicationStatus status);
}
