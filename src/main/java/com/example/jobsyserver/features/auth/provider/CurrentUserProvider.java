package com.example.jobsyserver.features.auth.provider;

import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.client.model.ClientProfile;
import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.user.model.User;
import com.example.jobsyserver.features.client.repository.ClientProfileRepository;
import com.example.jobsyserver.features.freelancer.repository.FreelancerProfileRepository;
import com.example.jobsyserver.features.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@RequiredArgsConstructor
public class CurrentUserProvider {

    private final UserRepository userRepository;
    private final ClientProfileRepository clientProfileRepository;
    private final FreelancerProfileRepository freelancerProfileRepository;

    private String email;
    private User user;
    private Long clientProfileId;
    private Long freelancerProfileId;

    public String getEmail() {
        if (email == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
                throw new BadCredentialsException("Пользователь не аутентифицирован");
            }
            Object principal = auth.getPrincipal();
            email = principal instanceof UserDetails
                    ? ((UserDetails) principal).getUsername()
                    : principal.toString();
        }
        return email;
    }

    public User getUser() {
        if (user == null) {
            user = userRepository.findByEmail(getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "email", getEmail()));
        }
        return user;
    }

    public Long getClientProfileId() {
        if (clientProfileId == null) {
            ClientProfile profile = clientProfileRepository.findByUserEmail(getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Профиль клиента для пользователя " + getEmail()));
            clientProfileId = profile.getId();
        }
        return clientProfileId;
    }

    public Long getFreelancerProfileId() {
        if (freelancerProfileId == null) {
            FreelancerProfile profile = freelancerProfileRepository.findByUserEmail(getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Профиль фрилансера для пользователя " + getEmail()));
            freelancerProfileId = profile.getId();
        }
        return freelancerProfileId;
    }
}