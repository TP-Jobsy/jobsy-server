package com.example.jobsyserver.service;

import com.example.jobsyserver.dto.freelancer.FreelancerProfileDto;
import com.example.jobsyserver.dto.project.ProjectDto;

import java.util.List;

public interface SearchService {
    List<FreelancerProfileDto> searchFreelancers(List<Long> skillIds, String textTerm);
    List<ProjectDto> searchProjects(List<Long> skillIds, String textTerm);
}
