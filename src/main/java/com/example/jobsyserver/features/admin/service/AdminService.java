package com.example.jobsyserver.features.admin.service;

import com.example.jobsyserver.features.portfolio.dto.FreelancerPortfolioDto;
import com.example.jobsyserver.features.project.dto.ProjectDto;
import com.example.jobsyserver.features.user.dto.UserDto;

import java.util.List;

public interface AdminService {
    List<UserDto> getAllFreelancers();
    UserDto getFreelancerById(Long id);
    void deactivateFreelancer(Long id);
    List<UserDto> getAllClients();
    UserDto getClientById(Long id);
    void deactivateClient(Long id);
    List<ProjectDto> getClientProjects(Long clientId);
    ProjectDto getProjectById(Long id);
    void deleteProject(Long id);
    List<FreelancerPortfolioDto> getFreelancerPortfolio(Long freelancerId);
    void deletePortfolio(Long freelancerId, Long portfolioId);
}
