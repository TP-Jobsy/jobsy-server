package com.example.jobsyserver.features.admin.service.impl;

import com.example.jobsyserver.features.admin.service.*;
import com.example.jobsyserver.features.client.dto.ClientProfileDto;
import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileDto;
import com.example.jobsyserver.features.portfolio.dto.FreelancerPortfolioDto;
import com.example.jobsyserver.features.portfolio.projection.PortfolioAdminListItem;
import com.example.jobsyserver.features.project.dto.ProjectDto;
import com.example.jobsyserver.features.project.projection.ProjectAdminListItem;
import com.example.jobsyserver.features.user.dto.UserDto;
import com.example.jobsyserver.features.common.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final FreelancerAdminService freelancerSvc;
    private final ClientAdminService clientSvc;
    private final ProjectAdminService projectSvc;
    private final PortfolioAdminService portfolioSvc;
    private final UserAdminService userSvc;

    @Override
    public List<FreelancerProfileDto> getAllFreelancers() {
        return freelancerSvc.getAll();
    }

    @Override
    public FreelancerProfileDto getFreelancerById(Long id) {
        return freelancerSvc.getById(id);
    }

    @Override
    public void deactivateFreelancer(Long id) {
        freelancerSvc.deactivate(id);
    }

    @Override
    public void deleteFreelancer(Long id) {
        freelancerSvc.delete(id);
    }

    @Override
    public void activateFreelancer(Long id) {
        freelancerSvc.activate(id);
    }

    @Override
    public List<ClientProfileDto> getAllClients() {
        return clientSvc.getAll();
    }

    @Override
    public ClientProfileDto getClientById(Long id) {
        return clientSvc.getById(id);
    }

    @Override
    public void deactivateClient(Long id) {
        clientSvc.deactivate(id);
    }

    @Override
    public void deleteClient(Long id) {
        clientSvc.delete(id);
    }

    @Override
    public void activateClient(Long id) {
        clientSvc.activate(id);
    }

    @Override
    public List<ProjectDto> getClientProjects(Long clientId) {
        return projectSvc.getByClient(clientId);
    }

    @Override
    public List<ProjectDto> getFreelancerProjects(Long freelancerId) {
        return projectSvc.getByFreelancer(freelancerId);
    }

    @Override
    public ProjectDto getProjectById(Long id) {
        return projectSvc.getById(id);
    }

    @Override
    public void deleteProject(Long id) {
        projectSvc.delete(id);
    }

    @Override
    public Page<ProjectAdminListItem> getAllProjectsPageable(Pageable pageable) {
        return projectSvc.pageAll(pageable);
    }

    @Override
    public Page<ProjectAdminListItem> searchProjects(
            String term, String status, LocalDateTime from, LocalDateTime to, Pageable p
    ) {
        return projectSvc.search(term, status, from, to, p);
    }

    @Override
    public List<FreelancerPortfolioDto> getFreelancerPortfolio(Long freelancerId) {
        return portfolioSvc.getByFreelancer(freelancerId);
    }

    @Override
    public FreelancerPortfolioDto getPortfolioById(Long portfolioId) {
        return portfolioSvc.getById(portfolioId);
    }

    @Override
    public void deletePortfolio(Long freelancerId, Long portfolioId) {
        portfolioSvc.delete(freelancerId, portfolioId);
    }

    @Override
    public Page<PortfolioAdminListItem> getAllPortfoliosPageable(Pageable pageable) {
        return portfolioSvc.pageAll(pageable);
    }

    @Override
    public Page<PortfolioAdminListItem> searchPortfolios(
            String term, String freelancerName, LocalDateTime from, LocalDateTime to, Pageable p
    ) {
        return portfolioSvc.search(term, freelancerName, from, to, p);
    }

    @Override
    public Page<UserDto> searchUsers(
            String term, UserRole role, LocalDateTime from, LocalDateTime to, Pageable p
    ) {
        return userSvc.search(term, role, from, to, p);
    }
}