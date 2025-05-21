package com.example.jobsyserver.features.search.service;

import com.example.jobsyserver.features.freelancer.projection.FreelancerListItem;
import com.example.jobsyserver.features.project.projection.ProjectListItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchService {
    Page<FreelancerListItem> searchFreelancers(List<Long> skillIds, String textTerm, Pageable pageable);

    Page<ProjectListItem> searchProjects(List<Long> skillIds, String term, Pageable pageable);
}
