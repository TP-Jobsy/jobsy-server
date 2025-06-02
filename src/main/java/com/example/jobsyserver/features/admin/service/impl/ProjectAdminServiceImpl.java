package com.example.jobsyserver.features.admin.service.impl;

import com.example.jobsyserver.features.admin.service.ProjectAdminService;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.freelancer.repository.FreelancerProfileRepository;
import com.example.jobsyserver.features.project.dto.ProjectDto;
import com.example.jobsyserver.features.project.mapper.ProjectMapper;
import com.example.jobsyserver.features.project.model.Project;
import com.example.jobsyserver.features.project.projection.ProjectAdminListItem;
import com.example.jobsyserver.features.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.jobsyserver.features.search.specification.ProjectSpecification.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectAdminServiceImpl implements ProjectAdminService {

    private final ProjectRepository projectRepo;
    private final ProjectMapper mapper;
    private final FreelancerProfileRepository freelancerProfileRepo;

    @Override
    public List<ProjectDto> getByClient(Long clientProfileId) {
        return projectRepo.findByClientId(clientProfileId).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<ProjectDto> getByFreelancer(Long userId) {
        FreelancerProfile profile = freelancerProfileRepo
                .findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("FreelancerProfile", userId));
        return projectRepo
                .findByAssignedFreelancerId(profile.getId())
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public ProjectDto getById(Long id) {
        Project p = projectRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Проект", id));
        return mapper.toDto(p);
    }

    @Override
    public void delete(Long id) {
        Project p = projectRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Проект", id));
        projectRepo.delete(p);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectAdminListItem> pageAll(Pageable pageable) {
        return projectRepo.findAllProjectedByAdmin(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectAdminListItem> search(
            String term, String status,
            LocalDateTime from, LocalDateTime to,
            Pageable pageable
    ) {
        Specification<Project> spec = Specification
                .where(textSearchProject(term))
                .or(textSearchClient(term))
                .and(hasStatus(status))
                .and(createdBetween(from, to));
        return projectRepo.findAllProjected(spec, pageable);
    }
}