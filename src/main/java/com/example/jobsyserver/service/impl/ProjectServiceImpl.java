package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.common.SkillDto;
import com.example.jobsyserver.dto.project.ProjectBasicDto;
import com.example.jobsyserver.dto.project.ProjectCreateDto;
import com.example.jobsyserver.dto.project.ProjectDto;
import com.example.jobsyserver.dto.project.ProjectUpdateDto;
import com.example.jobsyserver.enums.ProjectStatus;
import com.example.jobsyserver.exception.ResourceNotFoundException;
import com.example.jobsyserver.mapper.ProjectMapper;
import com.example.jobsyserver.model.Project;
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
    public ProjectDto updateProject(Long projectId, ProjectUpdateDto dto) {
        User current = securityService.getCurrentUser();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Проект", projectId));
        if (!project.getClient().getUser().getId().equals(current.getId())) {
            throw new AccessDeniedException("Не ваш проект");
        }
        if (project.getStatus() != ProjectStatus.OPEN) {
            throw new IllegalStateException("Редактировать можно только опубликованные проекты");
        }
        projectMapper.toEntity(dto, project);
        applyCategoryAndSpec(project, dto);
        syncSkills(project, dto.getSkills());
        return projectMapper.toDto(projectRepository.save(project));
    }

    @Override
    @Transactional
    public void deleteProject(Long projectId) {
        User current = securityService.getCurrentUser();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Проект", projectId));
        if (!project.getClient().getUser().getId().equals(current.getId())) {
            throw new AccessDeniedException("Не ваш проект");
        }
        if (project.getStatus() != ProjectStatus.OPEN) {
            throw new IllegalStateException("Удалять можно только опубликованные проекты");
        }
        projectRepository.delete(project);
    }

    @Override
    @Transactional
    public ProjectDto createDraft(ProjectCreateDto dto) {
        User current = securityService.getCurrentUser();
        var client = clientProfileRepository.findByUser(current)
                .orElseThrow(() -> new ResourceNotFoundException("Профиль клиента"));
        Project draft = projectMapper.toEntity(dto);
        draft.setClient(client);
        applyCategoryAndSpec(draft, dto);
        draft.setStatus(ProjectStatus.DRAFT);
        draft = projectRepository.save(draft);
        syncSkills(draft, dto.getSkills());
        return projectMapper.toDto(draft);
    }

    @Override
    @Transactional
    public ProjectDto updateDraft(Long draftId, ProjectUpdateDto dto) {
        Project draft = projectRepository.findById(draftId)
                .orElseThrow(() -> new ResourceNotFoundException("Черновик проекта", draftId));
        if (draft.getStatus() != ProjectStatus.DRAFT) {
            throw new IllegalStateException("Редактировать можно только черновик");
        }
        projectMapper.toEntity(dto, draft);
        applyCategoryAndSpec(draft, dto);
        syncSkills(draft, dto.getSkills());
        return projectMapper.toDto(projectRepository.save(draft));
    }

    @Override
    @Transactional
    public ProjectDto publish(Long draftId, ProjectUpdateDto dto) {
        ProjectDto updated = updateDraft(draftId, dto);
        Project draft = projectRepository.getReferenceById(draftId);
        draft.setStatus(ProjectStatus.OPEN);
        projectRepository.save(draft);
        return updated;
    }

    private void applyCategoryAndSpec(Project project, ProjectBasicDto dto) {
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
    }

    private void syncSkills(Project project, List<SkillDto> skills) {
        projectSkillService.getSkillsByProject(project.getId())
                .forEach(s -> projectSkillService.removeSkill(project.getId(), s.getId()));
        if (skills != null) {
            skills.forEach(s -> projectSkillService.addSkill(project.getId(), s.getId()));
        }
    }
}