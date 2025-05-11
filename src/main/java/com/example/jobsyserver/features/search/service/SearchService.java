package com.example.jobsyserver.features.search.service;

import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileDto;
import com.example.jobsyserver.features.project.dto.ProjectDto;

import java.util.List;

public interface SearchService {
    List<FreelancerProfileDto> searchFreelancers(List<Long> skillIds, String textTerm);
    List<ProjectDto> searchProjects(List<Long> skillIds, String textTerm);
}
