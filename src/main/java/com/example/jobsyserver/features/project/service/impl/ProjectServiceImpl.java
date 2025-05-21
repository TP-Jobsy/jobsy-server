package com.example.jobsyserver.features.project.service.impl;

import com.example.jobsyserver.features.project.projection.ProjectListItem;
import com.example.jobsyserver.features.skill.dto.SkillDto;
import com.example.jobsyserver.features.project.dto.ProjectBasicDto;
import com.example.jobsyserver.features.project.dto.ProjectCreateDto;
import com.example.jobsyserver.features.project.dto.ProjectDto;
import com.example.jobsyserver.features.project.dto.ProjectUpdateDto;
import com.example.jobsyserver.features.common.enums.Complexity;
import com.example.jobsyserver.features.common.enums.PaymentType;
import com.example.jobsyserver.features.common.enums.ProjectDuration;
import com.example.jobsyserver.features.common.enums.ProjectStatus;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.project.mapper.ProjectMapper;
import com.example.jobsyserver.features.project.model.Project;
import com.example.jobsyserver.features.skill.model.Skill;
import com.example.jobsyserver.features.skill.repository.SkillRepository;
import com.example.jobsyserver.features.user.model.User;
import com.example.jobsyserver.features.category.repository.CategoryRepository;
import com.example.jobsyserver.features.client.repository.ClientProfileRepository;
import com.example.jobsyserver.features.project.repository.ProjectRepository;
import com.example.jobsyserver.features.specialization.repository.SpecializationRepository;
import com.example.jobsyserver.features.project.service.ProjectService;
import com.example.jobsyserver.features.auth.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final SkillRepository skillRepository;
    private final ClientProfileRepository clientProfileRepository;
    private final CategoryRepository categoryRepository;
    private final SpecializationRepository specializationRepository;
    private final ProjectMapper projectMapper;
    private final SecurityService securityService;

    @Override
    public List<ProjectDto> getAllProjects(ProjectStatus status) {
        List<Project> projects = (status != null)
                ? projectRepository.findAllWithGraphByStatus(status)
                : projectRepository.findAllWithGraph();
        return projectMapper.toDtoList(projects);
    }

    @Override
    public Page<ProjectListItem> listProjects(ProjectStatus status, Pageable pageable) {
        return projectRepository.findAllProjectedBy(status, pageable);
    }

    @Override
    public ProjectDto getProjectById(Long projectId) {
        Project project = projectRepository.findById(projectId)
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
        draft.setTitle(dto.getTitle());
        draft.setDescription(dto.getDescription() != null ? dto.getDescription() : "");
        draft.setPaymentType(dto.getPaymentType() != null
                ? dto.getPaymentType()
                : PaymentType.FIXED);
        draft.setFixedPrice(dto.getFixedPrice() != null
                ? dto.getFixedPrice()
                : BigDecimal.ZERO);
        draft.setProjectComplexity(dto.getComplexity() != null
                ? dto.getComplexity()
                : Complexity.EASY);
        draft.setProjectDuration(dto.getDuration() != null
                ? dto.getDuration()
                : ProjectDuration.LESS_THAN_1_MONTH);
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

    @Override
    @Transactional
    public ProjectDto completeByClient(Long projectId) {
        Project p = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Проект", projectId));
        if (!securityService.getCurrentClientProfileId().equals(p.getClient().getId())) {
            throw new AccessDeniedException("Не ваш проект");
        }
        p.setClientCompleted(true);
        if (p.isFreelancerCompleted()) {
            p.setStatus(ProjectStatus.COMPLETED);
        }
        return projectMapper.toDto(projectRepository.save(p));
    }

    @Override
    @Transactional
    public ProjectDto completeByFreelancer(Long projectId) {
        Project p = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Проект", projectId));
        if (p.getAssignedFreelancer() == null ||
                !securityService.getCurrentFreelancerProfileId().equals(p.getAssignedFreelancer().getId())) {
            throw new AccessDeniedException("Вы не назначены на этот проект");
        }
        p.setFreelancerCompleted(true);
        if (p.isClientCompleted()) {
            p.setStatus(ProjectStatus.COMPLETED);
        }
        return projectMapper.toDto(projectRepository.save(p));
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

    private void syncSkills(Project project, List<SkillDto> skillDtos) {
        project.getSkills().clear();
        if (skillDtos != null) {
            for (SkillDto sd : skillDtos) {
                Skill skill = skillRepository.findById(sd.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Навык", sd.getId()));
                project.getSkills().add(skill);
            }
        }
    }
}