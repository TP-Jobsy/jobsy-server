package com.example.jobsyserver.features.admin.service;

import com.example.jobsyserver.features.client.dto.ClientProfileDto;
import com.example.jobsyserver.features.common.enums.UserRole;
import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileDto;
import com.example.jobsyserver.features.portfolio.dto.FreelancerPortfolioDto;
import com.example.jobsyserver.features.portfolio.projection.PortfolioAdminListItem;
import com.example.jobsyserver.features.project.dto.ProjectDto;
import com.example.jobsyserver.features.project.projection.ProjectAdminListItem;
import com.example.jobsyserver.features.user.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminService {
    List<FreelancerProfileDto> getAllFreelancers();

    FreelancerProfileDto getFreelancerById(Long id);

    void deactivateFreelancer(Long id);

    void deleteFreelancer(Long id);

    List<ClientProfileDto> getAllClients();

    ClientProfileDto getClientById(Long id);

    void deactivateClient(Long id);

    void deleteClient(Long id);

    List<ProjectDto> getClientProjects(Long clientId);

    Page<ProjectAdminListItem> getAllProjectsPageable(Pageable pageable);

    ProjectDto getProjectById(Long id);

    void deleteProject(Long id);

    List<FreelancerPortfolioDto> getFreelancerPortfolio(Long freelancerId);

    FreelancerPortfolioDto getPortfolioById(Long portfolioId);

    Page<PortfolioAdminListItem> getAllPortfoliosPageable(Pageable pageable);

    void deletePortfolio(Long freelancerId, Long portfolioId);

    Page<ProjectAdminListItem> searchProjects(
            String titleTerm,
            String clientName,
            String status,
            LocalDateTime createdFrom,
            LocalDateTime createdTo,
            Pageable pageable
    );

    Page<PortfolioAdminListItem> searchPortfolios(
            String titleTerm,
            String freelancerName,
            LocalDateTime createdFrom,
            LocalDateTime createdTo,
            Pageable pageable
    );

    Page<UserDto> searchUsers(String email, String firstName, String lastName, String phone, UserRole role, Pageable pageable);

    void activateFreelancer(Long userId);

    void activateClient(Long userId);

    List<ProjectDto> getFreelancerProjects(Long freelancerId);
}
