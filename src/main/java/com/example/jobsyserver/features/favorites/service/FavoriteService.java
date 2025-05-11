package com.example.jobsyserver.features.favorites.service;

import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileDto;
import com.example.jobsyserver.features.project.dto.ProjectDto;

import java.util.List;

public interface FavoriteService {
    void addProjectToFavorites(Long freelancerId, Long projectId);
    void removeProjectFromFavorites(Long freelancerId, Long projectId);
    List<ProjectDto> getFavoriteProjects(Long freelancerId);
    void addFreelancerToFavorites(Long clientId, Long freelancerId);
    void removeFreelancerFromFavorites(Long clientId, Long freelancerId);
    List<FreelancerProfileDto> getFavoriteFreelancers(Long clientId);
}
