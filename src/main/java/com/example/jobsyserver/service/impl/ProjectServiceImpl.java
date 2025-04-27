package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.project.ProjectCreateDto;
import com.example.jobsyserver.dto.project.ProjectDto;
import com.example.jobsyserver.dto.project.ProjectUpdateDto;
import com.example.jobsyserver.enums.ProjectStatus;
import com.example.jobsyserver.exception.ResourceNotFoundException;
import com.example.jobsyserver.mapper.ProjectMapper;
import com.example.jobsyserver.model.ClientProfile;
import com.example.jobsyserver.model.Project;
import com.example.jobsyserver.model.User;
import com.example.jobsyserver.repository.*;
import com.example.jobsyserver.service.ProjectService;
import com.example.jobsyserver.service.ProjectSkillService;
import com.example.jobsyserver.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ClientProfileRepository clientProfileRepository;
    private final CategoryRepository categoryRepository;
    private final SpecializationRepository specializationRepository;
    private final ProjectMapper projectMapper;
    private final UserRepository userRepository;
    private final SecurityService securityService;
    private final ProjectSkillService projectSkillService;

    @Override
    public List<ProjectDto> getAllProjects(ProjectStatus status) {
        List<Project> projects = (status != null)
                ? projectRepository.findByStatus(status)
                : projectRepository.findAll();

        return projects.stream()
                .map(projectMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ProjectDto createProject(ProjectCreateDto dto) {
        User currentUser = getCurrentUser();

        ClientProfile client = clientProfileRepository.findByUser(currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Профиль"));

        Project project = projectMapper.toEntity(dto);
        project.setClient(client);
        project.setStatus(ProjectStatus.OPEN);

        if (dto.getCategory() != null) {
            project.setCategory(categoryRepository.findById(dto.getCategory().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Категория")));
        }

        if (dto.getSpecialization() != null) {
            project.setSpecialization(specializationRepository.findById(dto.getSpecialization().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Специализация")));
        }

        Project savedProject = projectRepository.save(project);

        if (dto.getSkills() != null) {
            dto.getSkills().forEach(skillDto ->
                    projectSkillService.addSkill(savedProject.getId(), skillDto.getId())
            );
        }

        return projectMapper.toDto(savedProject);
    }

    @Override
    @Transactional
    public ProjectDto updateProject(Long projectId, ProjectUpdateDto dto) {
        User currentUser = getCurrentUser();

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Проект"));

        if (!project.getClient().getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Вы не являетесь владельцем проекта");
        }

        if (project.getStatus() != ProjectStatus.OPEN) {
            throw new RuntimeException("Можно редактировать только проекты со статусом OPEN");
        }

        projectMapper.toEntity(dto, project);

        if (dto.getCategory() != null) {
            project.setCategory(categoryRepository.findById(dto.getCategory().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Категория")));
        }

        if (dto.getSpecialization() != null) {
            project.setSpecialization(specializationRepository.findById(dto.getSpecialization().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Специализация")));
        }

        Project updatedProject = projectRepository.save(project);

        if (dto.getSkills() != null) {
            projectSkillService.getSkillsByProject(projectId).forEach(existingSkill ->
                    projectSkillService.removeSkill(projectId, existingSkill.getId())
            );
            dto.getSkills().forEach(newSkill ->
                    projectSkillService.addSkill(projectId, newSkill.getId())
            );
        }

        return projectMapper.toDto(updatedProject);
    }

    @Override
    public void deleteProject(Long id) {
        User currentUser = getCurrentUser();
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Проект"));

        if (!project.getClient().getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Вы не являетесь владельцем проекта");
        }

        if (project.getStatus() != ProjectStatus.OPEN) {
            throw new RuntimeException("Удаление доступно только для проектов со статусом OPEN");
        }

        projectRepository.delete(project);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDto> getProjectsByClient(Long clientId, ProjectStatus status) {
        List<Project> projects;
        if (status != null) {
            projects = projectRepository.findByClientIdAndStatus(clientId, status);
        } else {
            projects = projectRepository.findByClientId(clientId);
        }
        return projects.stream()
                .map(projectMapper::toDto)
                .toList();
    }

    private User getCurrentUser() {
        String email = securityService.getCurrentUserEmail();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", "email", email));
    }
}