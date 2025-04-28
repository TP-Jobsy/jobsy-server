package com.example.jobsyserver.service;

import com.example.jobsyserver.dto.project.ProjectCreateDto;
import com.example.jobsyserver.dto.project.ProjectDto;
import com.example.jobsyserver.dto.project.ProjectUpdateDto;
import com.example.jobsyserver.enums.ProjectStatus;

import java.util.List;

public interface ProjectService {
    List<ProjectDto> getAllProjects(ProjectStatus status);
    ProjectDto getProjectById(Long projectId);
    ProjectDto getProjectByIdAndClient(Long projectId, Long clientId);
    List<ProjectDto> getProjectsByClient(Long clientId, ProjectStatus status);
    List<ProjectDto> getProjectsForFreelancer(Long freelancerProfileId, ProjectStatus status);
    ProjectDto createProject(ProjectCreateDto dto);
    ProjectDto updateProject(Long projectId, ProjectUpdateDto dto);
    void deleteProject(Long projectId);
}
