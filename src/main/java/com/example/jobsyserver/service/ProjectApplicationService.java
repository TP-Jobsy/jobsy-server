package com.example.jobsyserver.service;

import com.example.jobsyserver.dto.project.ProjectApplicationDto;
import com.example.jobsyserver.dto.project.ProjectApplicationRequestDto;
import com.example.jobsyserver.enums.ProjectApplicationStatus;

public interface ProjectApplicationService {
    ProjectApplicationDto getById(Long id);
    ProjectApplicationDto createApplication(ProjectApplicationRequestDto dto);
    ProjectApplicationDto updateStatus(Long id, ProjectApplicationStatus newStatus);
    void deleteApplication(Long id);
}
