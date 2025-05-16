package com.example.jobsyserver.features.admin.service.impl;

import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.portfolio.model.FreelancerPortfolio;
import com.example.jobsyserver.features.project.model.Project;
import com.example.jobsyserver.features.user.dto.UserDto;
import com.example.jobsyserver.features.user.model.User;
import com.example.jobsyserver.features.common.enums.UserRole;
import com.example.jobsyserver.features.user.repository.UserRepository;
import com.example.jobsyserver.features.user.mapper.UserMapper;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final FreelancerPortfolioRepository portfolioRepository;
    private final FreelancerPortfolioMapper portfolioMapper;

    public List<UserDto> getAllFreelancerProfiles() {
        log.info("Получение всех профилей фрилансеров");
        return userRepository.findByRole(UserRole.FREELANCER).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDto getFreelancerById(Long id) {
        User freelancer = userRepository.findByIdAndRole(id, UserRole.FREELANCER)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", id));
        log.info("Получение профиля фрилансера с id: {}", id);
        return userMapper.toDto(freelancer);
    }

    public void deactivateFreelancer(Long id) {
        User freelancer = userRepository.findByIdAndRole(id, UserRole.FREELANCER)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", id));
        freelancer.setIsActive(false);
        log.info("Деактивация профиля фрилансера с id: {}", id);
        userRepository.save(freelancer);
    }

    public List<UserDto> getAllClients() {
        return userRepository.findByRole(UserRole.CLIENT).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDto getClientById(Long id) {
        User client = userRepository.findByIdAndRole(id, UserRole.CLIENT)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", id));
        log.info("Получение профиля заказчика с id: {}", id);
        return userMapper.toDto(client);
    }

    public void deactivateClient(Long id) {
        User client = userRepository.findByIdAndRole(id, UserRole.CLIENT)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", id));
        client.setIsActive(false);
        log.info("Деактивация профиля фрилансера с id: {}", id);
        userRepository.save(client);
    }

    public List<ProjectDto> getClientProjects(Long clientId) {
        log.info("Получение проектов заказчика с clientId: {}", clientId);
        return projectRepository.findByClientId(clientId).stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }

    public ProjectDto getProjectById(Long id) {
        log.info("Получение проекта по projectId: {}", id);
        return projectRepository.findById(id)
                .map(projectMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Проект", id));
    }

    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Проект", id));
        log.info("Удаление проекта с id: {}", id);
        projectRepository.delete(project);
    }

    public List<FreelancerPortfolioDto> getFreelancerPortfolio(Long freelancerId) {
        log.info("Получение портфолио фрилансера с id: {}", freelancerId);
        return portfolioRepository.findByFreelancerId(freelancerId).stream()
                .map(portfolioMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deletePortfolio(Long freelancerId, Long portfolioId) {
        log.info("Удаление портфолио фрилансера с id: {}", portfolioId);
        FreelancerPortfolio portfolio = portfolioRepository.findByIdAndFreelancerId(portfolioId, freelancerId)
                .orElseThrow(() -> new ResourceNotFoundException("Портфолио", portfolioId));
        portfolioRepository.delete(portfolio);
    }
}