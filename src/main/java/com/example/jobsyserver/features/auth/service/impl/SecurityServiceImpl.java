package com.example.jobsyserver.features.auth.service.impl;

import com.example.jobsyserver.features.common.enums.UserRole;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.client.model.ClientProfile;
import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.project.model.Project;
import com.example.jobsyserver.features.user.model.User;
import com.example.jobsyserver.features.client.repository.ClientProfileRepository;
import com.example.jobsyserver.features.freelancer.repository.FreelancerProfileRepository;
import com.example.jobsyserver.features.project.repository.ProjectRepository;
import com.example.jobsyserver.features.user.repository.UserRepository;
import com.example.jobsyserver.features.auth.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final ClientProfileRepository clientProfileRepository;
    private final FreelancerProfileRepository freelancerProfileRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Override
    public String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null
                || !auth.isAuthenticated()
                || "anonymousUser".equals(auth.getPrincipal())) {
            throw new BadCredentialsException("Пользователь не аутентифицирован");
        }
        Object principal = auth.getPrincipal();
        return (principal instanceof UserDetails)
                ? ((UserDetails) principal).getUsername()
                : principal.toString();
    }
    @Override
    public User getCurrentUser() {
        String email = getCurrentUserEmail();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    @Override
    public Project getProjectReference(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", projectId));
    }

    @Override
    public Long getCurrentClientProfileId() {
        String email = getCurrentUserEmail();
        ClientProfile profile = clientProfileRepository
                .findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Профиль клиента для пользователя " + email));
        return profile.getId();
    }

    @Override
    public Long getCurrentFreelancerProfileId() {
        String email = getCurrentUserEmail();
        FreelancerProfile profile = freelancerProfileRepository
                .findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Профиль фрилансера для пользователя " + email));
        return profile.getId();
    }

    @Override
    public Long getCurrentProfileId() {
        User user = getCurrentUser();
        UserRole role = user.getRole();
        if (role == UserRole.CLIENT) {
            return getCurrentClientProfileId();
        }
        if (role == UserRole.FREELANCER) {
            return getCurrentFreelancerProfileId();
        }
        throw new AccessDeniedException("У пользователя нет роли CLIENT или FREELANCER");
    }
}
