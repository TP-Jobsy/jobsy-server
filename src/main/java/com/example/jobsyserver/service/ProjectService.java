package com.example.jobsyserver.service;

import com.example.jobsyserver.dto.project.ProjectCreateDto;
import com.example.jobsyserver.dto.project.ProjectDto;
import com.example.jobsyserver.dto.project.ProjectUpdateDto;
import com.example.jobsyserver.enums.ProjectStatus;

import java.util.List;

public interface ProjectService {
    List<ProjectDto> getAllProjects(ProjectStatus status);
    ProjectDto createProject(ProjectCreateDto createDto);
    ProjectDto updateProject(Long id, ProjectUpdateDto updateDto);
    void deleteProject(Long id);
}
