package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.project.ProjectCreateDto;
import com.example.jobsyserver.dto.project.ProjectDto;
import com.example.jobsyserver.dto.project.ProjectUpdateDto;
import com.example.jobsyserver.enums.ProjectStatus;
import com.example.jobsyserver.exception.ResourceNotFoundException;
import com.example.jobsyserver.mapper.ProjectMapper;
import com.example.jobsyserver.model.User;
import com.example.jobsyserver.repository.CategoryRepository;
import com.example.jobsyserver.repository.ClientProfileRepository;
import com.example.jobsyserver.repository.ProjectRepository;
import com.example.jobsyserver.repository.SpecializationRepository;
import com.example.jobsyserver.repository.UserRepository;
import com.example.jobsyserver.service.ProjectService;
import com.example.jobsyserver.service.ProjectSkillService;
import com.example.jobsyserver.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
        var projects = status != null
                ? projectRepository.findByStatus(status)
                : projectRepository.findAll();
        return projects.stream()
                .map(projectMapper::toDto)
                .toList();
    }

    @Override
    public ProjectDto getProjectById(Long projectId) {
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Проект", projectId));
        return projectMapper.toDto(project);
    }

    @Override
    public ProjectDto getProjectByIdAndClient(Long projectId, Long clientId) {
        var project = projectRepository.findById(projectId)
                .filter(p -> p.getClient().getId().equals(clientId))
                .orElseThrow(() -> new ResourceNotFoundException("Проект", projectId));
        return projectMapper.toDto(project);
    }

    @Override
    public List<ProjectDto> getProjectsByClient(Long clientId, ProjectStatus status) {
        var projects = status != null
                ? projectRepository.findByClientIdAndStatus(clientId, status)
                : projectRepository.findByClientId(clientId);
        return projects.stream()
                .map(projectMapper::toDto)
                .toList();
    }

    @Override
    public List<ProjectDto> getProjectsForFreelancer(Long freelancerProfileId, ProjectStatus status) {
        var projects = status != null
                ? projectRepository.findByAssignedFreelancerIdAndStatus(freelancerProfileId, status)
                : projectRepository.findByAssignedFreelancerId(freelancerProfileId);
        return projects.stream()
                .map(projectMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ProjectDto createProject(ProjectCreateDto dto) {
        User current = getCurrentUser();
        var client = clientProfileRepository.findByUser(current)
                .orElseThrow(() -> new ResourceNotFoundException("Профиль заказчика"));
        var project = projectMapper.toEntity(dto);
        project.setClient(client);
        project.setStatus(ProjectStatus.OPEN);

        if (dto.getCategory() != null) {
            var cat = categoryRepository.findById(dto.getCategory().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Категория", dto.getCategory().getId()));
            project.setCategory(cat);
        }

        if (dto.getSpecialization() != null) {
            var spec = specializationRepository.findById(dto.getSpecialization().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Специализация", dto.getSpecialization().getId()));
            project.setSpecialization(spec);
        }

        var saved = projectRepository.save(project);
        if (dto.getSkills() != null) {
            dto.getSkills().forEach(s -> projectSkillService.addSkill(saved.getId(), s.getId()));
        }

        return projectMapper.toDto(saved);
    }

    @Override
    @Transactional
    public ProjectDto updateProject(Long projectId, ProjectUpdateDto dto) {
        User current = getCurrentUser();
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Проект", projectId));

        if (!project.getClient().getUser().getId().equals(current.getId())) {
            throw new AccessDeniedException("Не ваш проект");
        }
        if (project.getStatus() != ProjectStatus.OPEN) {
            throw new IllegalStateException("Редактировать можно только проекты в статусе OPEN");
        }
        projectMapper.toEntity(dto, project);

        if (dto.getCategory() != null) {
            var cat = categoryRepository.findById(dto.getCategory().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Категория", dto.getCategory().getId()));
            project.setCategory(cat);
        }

        if (dto.getSpecialization() != null) {
            var spec = specializationRepository.findById(dto.getSpecialization().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Специализация", dto.getSpecialization().getId()));
            project.setSpecialization(spec);
        }
        var updated = projectRepository.save(project);

        if (dto.getSkills() != null) {
            projectSkillService.getSkillsByProject(projectId)
                    .forEach(s -> projectSkillService.removeSkill(projectId, s.getId()));
            dto.getSkills().forEach(s -> projectSkillService.addSkill(projectId, s.getId()));
        }
        return projectMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId) {
        User current = getCurrentUser();
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Проект", projectId));

        if (!project.getClient().getUser().getId().equals(current.getId())) {
            throw new AccessDeniedException("Не ваш проект");
        }
        if (project.getStatus() != ProjectStatus.OPEN) {
            throw new IllegalStateException("Удалять можно только OPEN");
        }
        projectRepository.delete(project);
    }

    private User getCurrentUser() {
        var email = securityService.getCurrentUserEmail();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", "email", email));
    }
}