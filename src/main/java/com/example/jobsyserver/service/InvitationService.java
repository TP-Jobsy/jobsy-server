package com.example.jobsyserver.service;

import com.example.jobsyserver.dto.project.ProjectApplicationDto;
import com.example.jobsyserver.enums.ProjectApplicationStatus;

public interface InvitationService {
    ProjectApplicationDto invite(Long projectId, Long freelancerId);
    ProjectApplicationDto handleInvitationStatus(Long applicationId, ProjectApplicationStatus status);
}
