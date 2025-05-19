package com.example.jobsyserver.features.admin.service.impl;

import com.example.jobsyserver.features.admin.service.AdminService;
import com.example.jobsyserver.features.client.dto.ClientProfileDto;
import com.example.jobsyserver.features.client.mapper.ClientProfileMapper;
import com.example.jobsyserver.features.client.model.ClientProfile;
import com.example.jobsyserver.features.client.repository.ClientProfileRepository;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileDto;
import com.example.jobsyserver.features.freelancer.mapper.FreelancerProfileMapper;
import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.freelancer.repository.FreelancerProfileRepository;
import com.example.jobsyserver.features.portfolio.model.FreelancerPortfolio;
import com.example.jobsyserver.features.project.model.Project;
import com.example.jobsyserver.features.user.model.User;
import com.example.jobsyserver.features.common.enums.UserRole;
import com.example.jobsyserver.features.user.repository.UserRepository;
import com.example.jobsyserver.features.project.repository.ProjectRepository;
import com.example.jobsyserver.features.project.mapper.ProjectMapper;
import com.example.jobsyserver.features.portfolio.repository.FreelancerPortfolioRepository;
import com.example.jobsyserver.features.portfolio.mapper.FreelancerPortfolioMapper;
import com.example.jobsyserver.features.project.dto.ProjectDto;
import com.example.jobsyserver.features.portfolio.dto.FreelancerPortfolioDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final FreelancerPortfolioRepository portfolioRepository;
    private final FreelancerPortfolioMapper portfolioMapper;
    private final FreelancerProfileRepository freelancerProfileRepository;
    private final FreelancerProfileMapper freelancerProfileMapper;
    private final ClientProfileRepository clientProfileRepository;
    private final ClientProfileMapper clientProfileMapper;

    @Override
    public List<FreelancerProfileDto> getAllFreelancers() {
        log.info("Получение всех профилей фрилансеров");
        return userRepository.findByRole(UserRole.FREELANCER).stream()
                .map(freelancerProfileRepository::findByUser)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(freelancerProfileMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public FreelancerProfileDto getFreelancerById(Long userId) {
        FreelancerProfile freelancerProfile = freelancerProfileRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Профиль фрилансера", userId));
        log.info("Получение профиля фрилансера с userId: {}", userId);
        return freelancerProfileMapper.toDto(freelancerProfile);
    }

    @Override
    public void deactivateFreelancer(Long userId) {
        User freelancer = userRepository.findByIdAndRole(userId, UserRole.FREELANCER)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", userId));
        freelancer.setIsActive(false);
        log.info("Деактивация профиля фрилансера с userId: {}", userId);
        userRepository.save(freelancer);
    }

    @Override
    public void deleteFreelancer(Long userId) {
        User user = userRepository.findByIdAndRole(userId, UserRole.FREELANCER)
                .orElseThrow(() -> new ResourceNotFoundException("Фрилансер", "id", userId));
        userRepository.delete(user);
        log.info("Аккаунт фрилансера с userId {} успешно удалён", userId);
    }

    @Override
    public List<ClientProfileDto> getAllClients() {
        return userRepository.findByRole(UserRole.CLIENT).stream()
                .map(clientProfileRepository::findByUser)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(clientProfileMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ClientProfileDto getClientById(Long userId) {
        ClientProfile clientProfile = clientProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Профиль заказчика", userId));
        log.info("Получение профиля заказчика с userId: {}", userId);
        return clientProfileMapper.toDto(clientProfile);
    }

    @Override
    public void deactivateClient(Long userId) {
        User client = userRepository.findByIdAndRole(userId, UserRole.CLIENT)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", userId));
        client.setIsActive(false);
        log.info("Деактивация профиля заказчика с userId: {}", userId);
        userRepository.save(client);
    }

    @Override
    public void deleteClient(Long userId) {
        User user = userRepository.findByIdAndRole(userId, UserRole.CLIENT)
                .orElseThrow(() -> new ResourceNotFoundException("Фрилансер", "id", userId));
        userRepository.delete(user);
        log.info("Аккаунт заказчика с userId {} успешно удалён", userId);
    }

    @Override
    public List<ProjectDto> getClientProjects(Long clientId) {
        log.info("Получение проектов заказчика с clientId: {}", clientId);
        return projectRepository.findByClientId(clientId).stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDto getProjectById(Long id) {
        log.info("Получение проекта по projectId: {}", id);
        return projectRepository.findById(id)
                .map(projectMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Проект", id));
    }

    @Override
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Проект", id));
        log.info("Удаление проекта с id: {}", id);
        projectRepository.delete(project);
    }

    @Override
    public List<FreelancerPortfolioDto> getFreelancerPortfolio(Long freelancerId) {
        log.info("Получение портфолио фрилансера с id: {}", freelancerId);
        return portfolioRepository.findByFreelancerId(freelancerId).stream()
                .map(portfolioMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deletePortfolio(Long freelancerId, Long portfolioId) {
        log.info("Удаление портфолио фрилансера с id: {}", portfolioId);
        FreelancerPortfolio portfolio = portfolioRepository.findByIdAndFreelancerId(portfolioId, freelancerId)
                .orElseThrow(() -> new ResourceNotFoundException("Портфолио", portfolioId));
        portfolioRepository.delete(portfolio);
    }
}