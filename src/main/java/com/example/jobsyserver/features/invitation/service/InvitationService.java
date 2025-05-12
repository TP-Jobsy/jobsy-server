package com.example.jobsyserver.features.invitation.service;

import com.example.jobsyserver.features.project.dto.ProjectApplicationDto;
import com.example.jobsyserver.features.common.enums.ProjectApplicationStatus;

public interface InvitationService {
    ProjectApplicationDto invite(Long projectId, Long freelancerId);
    ProjectApplicationDto handleInvitationStatus(Long applicationId, ProjectApplicationStatus status);
}
