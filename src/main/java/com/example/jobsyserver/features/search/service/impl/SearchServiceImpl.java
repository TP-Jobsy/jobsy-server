package com.example.jobsyserver.features.search.service.impl;

import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileDto;
import com.example.jobsyserver.features.project.dto.ProjectDto;
import com.example.jobsyserver.features.freelancer.mapper.FreelancerProfileMapper;
import com.example.jobsyserver.features.project.mapper.ProjectMapper;
import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.project.model.Project;
import com.example.jobsyserver.features.freelancer.repository.FreelancerProfileRepository;
import com.example.jobsyserver.features.project.repository.ProjectRepository;
import com.example.jobsyserver.features.search.service.SearchService;
import com.example.jobsyserver.features.search.specification.SearchSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final FreelancerProfileRepository freelancerRepo;
    private final ProjectRepository projectRepo;
    private final FreelancerProfileMapper freelancerMapper;
    private final ProjectMapper projectMapper;

    @Override
    @Transactional(readOnly = true)
    public List<FreelancerProfileDto> searchFreelancers(List<Long> skillIds, String textTerm) {
        Specification<FreelancerProfile> spec = Specification
                .where(SearchSpecifications.hasAnySkill(skillIds))
                .and(SearchSpecifications.textSearchFreelancer(textTerm));
        List<FreelancerProfile> found = freelancerRepo.findAll(spec);
        return found.stream()
                .map(freelancerMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDto> searchProjects(List<Long> skillIds, String textTerm) {
        Specification<Project> spec = Specification
                .where(SearchSpecifications.hasAnySkillProject(skillIds))
                .and(SearchSpecifications.textSearchProject(textTerm));
        List<Project> found = projectRepo.findAll(spec);
        return found.stream()
                .map(projectMapper::toDto)
                .toList();
    }
}