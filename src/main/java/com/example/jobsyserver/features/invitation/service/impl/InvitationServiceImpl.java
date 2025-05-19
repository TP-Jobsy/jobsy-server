package com.example.jobsyserver.features.invitation.service.impl;

import com.example.jobsyserver.features.common.exception.BadRequestException;
import com.example.jobsyserver.features.project.dto.ProjectApplicationDto;
import com.example.jobsyserver.features.common.enums.ApplicationType;
import com.example.jobsyserver.features.common.enums.ProjectApplicationStatus;
import com.example.jobsyserver.features.common.enums.ProjectStatus;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.project.mapper.ProjectApplicationMapper;
import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.project.model.Project;
import com.example.jobsyserver.features.project.model.ProjectApplication;
import com.example.jobsyserver.features.freelancer.repository.FreelancerProfileRepository;
import com.example.jobsyserver.features.project.repository.ProjectApplicationRepository;
import com.example.jobsyserver.features.project.repository.ProjectRepository;
import com.example.jobsyserver.features.invitation.service.InvitationService;
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
        boolean alreadyInvited = applicationRepository
                .findByProjectIdAndFreelancerIdAndApplicationType(
                        projectId,
                        freelancerId,
                        ApplicationType.INVITATION
                )
                .isPresent();

        if (alreadyInvited) {
            throw new BadRequestException("Вы уже приглашали этого фрилансера на проект");
        }
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Проект"));
        FreelancerProfile freelancer = freelancerProfileRepository.findById(freelancerId)
                .orElseThrow(() -> new ResourceNotFoundException("Фрилансер"));

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
                .orElseThrow(() -> new ResourceNotFoundException("Заявка"));

        if (application.getApplicationType() != ApplicationType.INVITATION) {
            throw new IllegalArgumentException("Это не приглашение клиента");
        }
        if (application.getStatus() != ProjectApplicationStatus.PENDING) {
            throw new BadRequestException(
                    application.getStatus() == ProjectApplicationStatus.APPROVED
                            ? "Приглашение уже принято"
                            : "Приглашение уже отклонено"
            );
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