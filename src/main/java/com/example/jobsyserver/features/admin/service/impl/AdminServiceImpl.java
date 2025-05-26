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
import com.example.jobsyserver.features.portfolio.projection.PortfolioAdminListItem;
import com.example.jobsyserver.features.project.model.Project;
import com.example.jobsyserver.features.project.projection.ProjectAdminListItem;
import com.example.jobsyserver.features.user.dto.UserDto;
import com.example.jobsyserver.features.user.mapper.UserMapper;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.jobsyserver.features.user.specification.UserSpecifications.*;

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
    private final UserMapper userMapper;

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
        toggleUserActive(userId, UserRole.FREELANCER, false);
        log.info("Деактивирован фрилансер userId={}", userId);
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
        toggleUserActive(userId, UserRole.CLIENT, false);
        log.info("Деактивирован клиент userId={}", userId);
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

    @Override
    public Page<ProjectAdminListItem> searchProjects(String term, String status, String clientName, Pageable pageable) {
        log.info("Поиск проектов в админке: term='{}', status='{}', clientName='{}'", term, status, clientName);
        boolean isFilterEmpty = (term == null || term.isBlank())
                && (status == null || status.isBlank())
                && (clientName == null || clientName.isBlank());
        if (isFilterEmpty) {
            return projectRepository.findAllProjectedByAdmin(pageable);
        }
        String safeTerm = term != null ? term : "";
        String safeStatus = status != null ? status : "OPEN";
        String safeClientName = clientName != null ? clientName : "";
        return projectRepository.searchProjectsAdmin(safeTerm, safeStatus, safeClientName, pageable);
    }

    @Override
    public Page<PortfolioAdminListItem> searchPortfolios(String term, String freelancerName, Pageable pageable) {
        log.info("Поиск портфолио в админке: term='{}', freelancerName='{}'", term, freelancerName);
        boolean isFilterEmpty = (term == null || term.isBlank()) &&
                (freelancerName == null || freelancerName.isBlank());
        if (isFilterEmpty) {
            return portfolioRepository.findAllProjectedByAdmin(pageable);
        }
        String safeTerm = term != null ? term : "";
        String safeFreelancerName = freelancerName != null ? freelancerName : "";
        return portfolioRepository.searchPortfoliosAdmin(safeTerm, safeFreelancerName, pageable);
    }

    @Override
    public Page<UserDto> searchUsers(String email, String firstName, String lastName, String phone, UserRole role, Pageable pageable) {
        log.info("Поиск пользователей в админке: email='{}', firstName='{}', lastName='{}', phone='{}', role='{}'",
                email, firstName, lastName, phone, role);

        var spec = Specification.where(emailContains(email))
                .and(firstNameContains(firstName))
                .and(lastNameContains(lastName))
                .and(phoneContains(phone))
                .and(hasRole(role));

        return userRepository.findAll(spec, pageable)
                .map(userMapper::toDto);
    }

    @Override
    public Page<ProjectAdminListItem> getAllProjectsPageable(Pageable pageable) {
        log.info("Получение страницы проектов для админ-панели: {}", pageable);
        return projectRepository.findAllProjectedByAdmin(pageable);
    }

    @Override
    public Page<PortfolioAdminListItem> getAllPortfoliosPageable(Pageable pageable) {
        log.info("Получение страницы портфолио для админ-панели: {}", pageable);
        return portfolioRepository.findAllProjectedByAdmin(pageable);
    }

    @Override
    public FreelancerPortfolioDto getPortfolioById(Long portfolioId) {
        log.info("Получение полного портфолио с id: {}", portfolioId);
        FreelancerPortfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Портфолио", portfolioId));
        return portfolioMapper.toDto(portfolio);
    }

    @Override
    public void activateFreelancer(Long userId) {
        toggleUserActive(userId, UserRole.FREELANCER, true);
        log.info("Активирован фрилансер userId={}", userId);
    }

    @Override
    public void activateClient(Long userId) {
        toggleUserActive(userId, UserRole.CLIENT, true);
        log.info("Активирован клиент userId={}", userId);
    }

    private void toggleUserActive(Long userId, UserRole role, boolean active) {
        User user = userRepository.findByIdAndRole(userId, role)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", userId));
        if (user.getIsActive().equals(active)) {
            log.warn("Попытка {} пользователя userId={}, роль={} — уже в таком состоянии",
                    active ? "активации" : "деактивации", userId, role);
            return;
        }
        user.setIsActive(active);
        userRepository.save(user);
    }
}