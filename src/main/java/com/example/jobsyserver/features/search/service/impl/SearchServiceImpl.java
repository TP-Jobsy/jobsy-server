package com.example.jobsyserver.features.search.service.impl;

import com.example.jobsyserver.features.common.enums.ProjectStatus;
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
            String textTerm,
            Pageable pageable
    ) {
        boolean hasTerm = textTerm != null && !textTerm.isBlank();
        boolean hasSkills = skillIds != null && !skillIds.isEmpty();

        if (hasTerm && hasSkills) {
            return freelancerRepo.findBySkillsAndTerm(skillIds, textTerm, pageable);
        }
        if (hasTerm) {
            return freelancerRepo.findByTextTerm(textTerm, pageable);
        }
        if (hasSkills) {
            return freelancerRepo.findBySkills(skillIds, pageable);
        }
        return freelancerRepo.findAllProjected(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<ProjectListItem> searchProjects(
            List<Long> skillIds,
            String term,
            Pageable pageable
    ) {
        boolean hasTerm = term != null && !term.isBlank();
        boolean hasSkills = skillIds != null && !skillIds.isEmpty();
        ProjectStatus open = ProjectStatus.OPEN;

        if (hasTerm && hasSkills) {
            return projectRepo.findBySkillsAndTermStatus(skillIds, term, pageable, open);
        }
        if (hasTerm) {
            return projectRepo.findByTermStatus(term, pageable, open);
        }
        if (hasSkills) {
            return projectRepo.findBySkillsStatus(skillIds, pageable, open);
        }
        return projectRepo.findAllProjectedByStatus(open, pageable);
    }

}