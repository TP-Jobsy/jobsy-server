package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.project.ProjectApplicationDto;
import com.example.jobsyserver.enums.ApplicationType;
import com.example.jobsyserver.enums.ProjectApplicationStatus;
import com.example.jobsyserver.mapper.ProjectApplicationMapper;
import com.example.jobsyserver.repository.ProjectApplicationRepository;
import com.example.jobsyserver.service.ProjectApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectApplicationServiceImpl implements ProjectApplicationService {
    private final ProjectApplicationRepository repo;
    private final ProjectApplicationMapper mapper;

    @Override
    public List<ProjectApplicationDto> getResponsesForProject(Long projectId, ProjectApplicationStatus status) {
        var entities = (status != null)
                ? repo.findByProjectIdAndApplicationTypeAndStatus(projectId, ApplicationType.RESPONSE, status)
                : repo.findByProjectIdAndApplicationType(projectId, ApplicationType.RESPONSE);

        return entities.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<ProjectApplicationDto> getInvitationsForProject(Long projectId, ProjectApplicationStatus status) {
        var entities = (status != null)
                ? repo.findByProjectIdAndApplicationTypeAndStatus(projectId, ApplicationType.INVITATION, status)
                : repo.findByProjectIdAndApplicationType(projectId, ApplicationType.INVITATION);

        return entities.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<ProjectApplicationDto> getResponsesByFreelancer(Long freelancerId, ProjectApplicationStatus status) {
        var entities = (status != null)
                ? repo.findByFreelancerIdAndApplicationTypeAndStatus(freelancerId, ApplicationType.RESPONSE, status)
                : repo.findByFreelancerIdAndApplicationType(freelancerId, ApplicationType.RESPONSE);

        return entities.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<ProjectApplicationDto> getInvitationsByFreelancer(Long freelancerId, ProjectApplicationStatus status) {
        var entities = (status != null)
                ? repo.findByFreelancerIdAndApplicationTypeAndStatus(freelancerId, ApplicationType.INVITATION, status)
                : repo.findByFreelancerIdAndApplicationType(freelancerId, ApplicationType.INVITATION);

        return entities.stream()
                .map(mapper::toDto)
                .toList();
    }
}
