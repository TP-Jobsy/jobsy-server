package com.example.jobsyserver.features.project.service;

import com.example.jobsyserver.features.project.dto.ProjectCreateDto;
import com.example.jobsyserver.features.project.dto.ProjectDto;
import com.example.jobsyserver.features.project.dto.ProjectUpdateDto;
import com.example.jobsyserver.features.common.enums.ProjectStatus;
import com.example.jobsyserver.features.project.projection.ProjectListItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {
    List<ProjectDto> getAllProjects(ProjectStatus status);
    Page<ProjectListItem> listProjects(ProjectStatus status, Pageable pageable);
    ProjectDto getProjectById(Long projectId);
    ProjectDto getProjectByIdAndClient(Long projectId, Long clientId);
    List<ProjectDto> getProjectsByClient(Long clientId, ProjectStatus status);
    List<ProjectDto> getProjectsForFreelancer(Long freelancerProfileId, ProjectStatus status);
    ProjectDto updateProject(Long projectId, ProjectUpdateDto dto);
    void deleteProject(Long projectId);
    ProjectDto createDraft(ProjectCreateDto dto);
    ProjectDto updateDraft(Long draftId, ProjectUpdateDto dto);
    ProjectDto publish(Long draftId, ProjectUpdateDto dto);
    ProjectDto completeByClient(Long projectId);
    ProjectDto completeByFreelancer(Long projectId);
}
