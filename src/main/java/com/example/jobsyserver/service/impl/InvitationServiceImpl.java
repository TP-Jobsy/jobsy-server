package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.project.ProjectApplicationDto;
import com.example.jobsyserver.enums.ApplicationType;
import com.example.jobsyserver.enums.ProjectApplicationStatus;
import com.example.jobsyserver.enums.ProjectStatus;
import com.example.jobsyserver.exception.ProjectApplicationNotFoundException;
import com.example.jobsyserver.exception.ProjectNotFoundException;
import com.example.jobsyserver.exception.UserNotFoundException;
import com.example.jobsyserver.mapper.ProjectApplicationMapper;
import com.example.jobsyserver.model.FreelancerProfile;
import com.example.jobsyserver.model.Project;
import com.example.jobsyserver.model.ProjectApplication;
import com.example.jobsyserver.repository.FreelancerProfileRepository;
import com.example.jobsyserver.repository.ProjectApplicationRepository;
import com.example.jobsyserver.repository.ProjectRepository;
import com.example.jobsyserver.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {

    private final ProjectApplicationRepository applicationRepository;
    private final ProjectRepository projectRepository;
    private final FreelancerProfileRepository freelancerProfileRepository;
    private final ProjectApplicationMapper mapper;

    @Override
    @Transactional
    public ProjectApplicationDto invite(Long projectId, Long freelancerId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Проект не найден"));
        FreelancerProfile freelancer = freelancerProfileRepository.findById(freelancerId)
                .orElseThrow(() -> new UserNotFoundException("Фрилансер не найден"));

        ProjectApplication application = ProjectApplication.builder()
                .project(project)
                .freelancer(freelancer)
                .applicationType(ApplicationType.INVITATION)
                .status(ProjectApplicationStatus.PENDING)
                .build();

        return mapper.toDto(applicationRepository.save(application));
    }

    @Override
    @Transactional
    public ProjectApplicationDto handleInvitationStatus(Long applicationId, ProjectApplicationStatus status) {
        ProjectApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ProjectApplicationNotFoundException("Заявка не найдена"));

        if (application.getApplicationType() != ApplicationType.INVITATION) {
            throw new IllegalArgumentException("Это не приглашение клиента");
        }

        application.setStatus(status);
        if (status == ProjectApplicationStatus.APPROVED) {
            Project project = application.getProject();
            project.setAssignedFreelancer(application.getFreelancer());
            project.setStatus(ProjectStatus.IN_PROGRESS);
            projectRepository.save(project);
        }

        return mapper.toDto(applicationRepository.save(application));
    }
}