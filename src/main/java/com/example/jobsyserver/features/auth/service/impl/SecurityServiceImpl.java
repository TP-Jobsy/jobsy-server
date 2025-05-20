package com.example.jobsyserver.features.auth.service.impl;

import com.example.jobsyserver.features.auth.provider.CurrentUserProvider;
import com.example.jobsyserver.features.common.enums.UserRole;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.project.model.Project;
import com.example.jobsyserver.features.project.repository.ProjectRepository;
import com.example.jobsyserver.features.user.model.User;
import com.example.jobsyserver.features.auth.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final CurrentUserProvider userProv;
    private final ProjectRepository projectRepository;

    @Override
    public String getCurrentUserEmail() {
        return userProv.getEmail();
    }

    @Override
    public User getCurrentUser() {
        return userProv.getUser();
    }

    @Override
    public Long getCurrentClientProfileId() {
        return userProv.getClientProfileId();
    }

    @Override
    public Long getCurrentFreelancerProfileId() {
        return userProv.getFreelancerProfileId();
    }

    @Override
    public Long getCurrentProfileId() {
        User user = userProv.getUser();
        UserRole role = user.getRole();
        if (role == UserRole.CLIENT) {
            return userProv.getClientProfileId();
        } else if (role == UserRole.FREELANCER) {
            return userProv.getFreelancerProfileId();
        }
        throw new AccessDeniedException("У пользователя нет роли CLIENT или FREELANCER");
    }

    @Override
    public Project getProjectReference(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", projectId));
    }
}