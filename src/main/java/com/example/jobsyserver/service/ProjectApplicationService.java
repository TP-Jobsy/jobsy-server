package com.example.jobsyserver.service;

import com.example.jobsyserver.dto.project.ProjectApplicationDto;
import com.example.jobsyserver.enums.ProjectApplicationStatus;

import java.util.List;

public interface ProjectApplicationService {
    List<ProjectApplicationDto> getResponsesForProject(Long projectId, ProjectApplicationStatus status);
    List<ProjectApplicationDto> getInvitationsForProject(Long projectId, ProjectApplicationStatus status);
    List<ProjectApplicationDto> getResponsesByFreelancer(Long freelancerId, ProjectApplicationStatus status);
    List<ProjectApplicationDto> getInvitationsByFreelancer(Long freelancerId, ProjectApplicationStatus status);
}
