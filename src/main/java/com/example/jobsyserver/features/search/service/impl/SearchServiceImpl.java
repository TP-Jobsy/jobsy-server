package com.example.jobsyserver.features.search.service.impl;

import com.example.jobsyserver.features.freelancer.projection.FreelancerListItem;
import com.example.jobsyserver.features.freelancer.repository.FreelancerProfileRepository;
import com.example.jobsyserver.features.project.projection.ProjectListItem;
import com.example.jobsyserver.features.project.repository.ProjectRepository;
import com.example.jobsyserver.features.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final FreelancerProfileRepository freelancerRepo;
    private final ProjectRepository projectRepo;

    @Override
    @Transactional(readOnly = true)
    public Page<FreelancerListItem> searchFreelancers(
            List<Long> skillIds,
            String     textTerm,
            Pageable   pageable
    ) {
        return freelancerRepo.searchProjectedFreelancers(skillIds, textTerm, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectListItem> searchProjects(
            List<Long> skillIds,
            String term,
            Pageable pageable
    ) {
        return projectRepo.searchProjected(skillIds, term, pageable);
    }
}