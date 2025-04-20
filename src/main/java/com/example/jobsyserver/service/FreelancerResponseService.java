package com.example.jobsyserver.service;

import com.example.jobsyserver.dto.project.ProjectApplicationDto;
import com.example.jobsyserver.enums.ProjectApplicationStatus;

public interface FreelancerResponseService {
    ProjectApplicationDto respond(Long projectId, Long freelancerId);
    ProjectApplicationDto handleResponseStatus(Long applicationId, ProjectApplicationStatus status);
}
