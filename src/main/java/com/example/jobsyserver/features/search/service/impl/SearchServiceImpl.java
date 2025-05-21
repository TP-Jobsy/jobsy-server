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
        if (skillIds != null && !skillIds.isEmpty()) {
            return freelancerRepo.findBySkillsAndTerm(skillIds, textTerm, pageable);
        } else if (textTerm != null && !textTerm.isBlank()) {
            return freelancerRepo.findByTextTerm(textTerm, pageable);
        } else {
            return freelancerRepo.findAllProjected(pageable);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectListItem> searchProjects(
            List<Long> skillIds,
            String     term,
            Pageable   pageable
    ) {
        if (skillIds != null && !skillIds.isEmpty()) {
            return projectRepo.findBySkillsAndTerm(skillIds, term, pageable);
        } else if (term != null && !term.isBlank()) {
            return projectRepo.findByTerm(term, pageable);
        } else {
            return projectRepo.findAllProjectedBy(pageable);
        }
    }
}