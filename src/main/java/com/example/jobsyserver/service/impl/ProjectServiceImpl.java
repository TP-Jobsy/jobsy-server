package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.project.ProjectCreateDto;
import com.example.jobsyserver.dto.project.ProjectDto;
import com.example.jobsyserver.dto.project.ProjectUpdateDto;
import com.example.jobsyserver.enums.ProjectStatus;
import com.example.jobsyserver.mapper.ProjectMapper;
import com.example.jobsyserver.model.Project;
import com.example.jobsyserver.repository.ProjectRepository;
import com.example.jobsyserver.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public List<ProjectDto> getAllProjects(ProjectStatus status) {
        if (status != null) {
            return projectRepository.findByStatus(status).stream()
                    .map(projectMapper::toDto)
                    .toList();
        }
        return projectRepository.findAll().stream()
                .map(projectMapper::toDto)
                .toList();
    }

    public ProjectDto createProject(ProjectCreateDto dto) {
        Project project = projectMapper.toEntity(dto);
        project.setStatus(ProjectStatus.OPEN);
        return projectMapper.toDto(projectRepository.save(project));
    }

    @Transactional
    public ProjectDto updateProject(Long id, ProjectUpdateDto dto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Проект не найден"));

        if (project.getStatus() != ProjectStatus.OPEN) {
            throw new RuntimeException("Можно обновить только проекты со статусом \"open\".");
        }

        projectMapper.toEntity(dto, project);
        return projectMapper.toDto(projectRepository.save(project));
    }

    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Проект не найден"));

        if (project.getStatus() != ProjectStatus.OPEN) {
            throw new RuntimeException("Можно удалить только проекты со статусом \"open\".");
        }

        projectRepository.delete(project);
    }
}
