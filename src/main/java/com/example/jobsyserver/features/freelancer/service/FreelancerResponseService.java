package com.example.jobsyserver.features.freelancer.service;

import com.example.jobsyserver.features.project.dto.ProjectApplicationDto;
import com.example.jobsyserver.features.common.enums.ProjectApplicationStatus;

public interface FreelancerResponseService {
    ProjectApplicationDto respond(Long projectId, Long freelancerId);
    ProjectApplicationDto handleResponseStatus(Long applicationId, ProjectApplicationStatus status);
}
