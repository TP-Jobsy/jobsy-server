package com.example.jobsyserver.features.freelancer.service.impl;

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
import com.example.jobsyserver.features.freelancer.service.FreelancerResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FreelancerResponseServiceImpl implements FreelancerResponseService {

    private final ProjectApplicationRepository applicationRepository;
    private final ProjectRepository projectRepository;
    private final FreelancerProfileRepository freelancerProfileRepository;
    private final ProjectApplicationMapper mapper;

    @Override
    @Transactional
    public ProjectApplicationDto respond(Long projectId, Long freelancerId) {
        boolean already = applicationRepository
                .findByProjectIdAndFreelancerIdAndApplicationType(
                        projectId,
                        freelancerId,
                        ApplicationType.RESPONSE
                )
                .isPresent();

        if (already) {
            throw new BadRequestException("Вы уже оставили отклик на этот проект");
        }
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Проект"));
        FreelancerProfile freelancer = freelancerProfileRepository.findById(freelancerId)
                .orElseThrow(() -> new ResourceNotFoundException("Фрилансер"));

        ProjectApplication application = ProjectApplication.builder()
                .project(project)
                .freelancer(freelancer)
                .applicationType(ApplicationType.RESPONSE)
                .status(ProjectApplicationStatus.PENDING)
                .build();

        return mapper.toDto(applicationRepository.save(application));
    }

    @Override
    @Transactional
    public ProjectApplicationDto handleResponseStatus(Long applicationId, ProjectApplicationStatus status) {
        ProjectApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Заявка"));

        if (application.getApplicationType() != ApplicationType.RESPONSE) {
            throw new IllegalArgumentException("Это не отклик фрилансера");
        }
        if (application.getStatus() != ProjectApplicationStatus.PENDING) {
            throw new BadRequestException(
                    application.getStatus() == ProjectApplicationStatus.APPROVED
                            ? "Отклик уже одобрен"
                            : "Отклик уже отклонён"
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