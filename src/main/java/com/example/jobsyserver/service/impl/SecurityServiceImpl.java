package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.model.ClientProfile;
import com.example.jobsyserver.model.FreelancerProfile;
import com.example.jobsyserver.repository.ClientProfileRepository;
import com.example.jobsyserver.repository.FreelancerProfileRepository;
import com.example.jobsyserver.service.SecurityService;
import lombok.RequiredArgsConstructor;
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
    public Long getCurrentClientProfileId() {
        String email = getCurrentUserEmail();
        ClientProfile profile = clientProfileRepository
                .findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException(
                        "Профиль клиента не найден для пользователя " + email));
        return profile.getId();
    }

    @Override
    public Long getCurrentFreelancerProfileId() {
        String email = getCurrentUserEmail();
        FreelancerProfile profile = freelancerProfileRepository
                .findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException(
                        "Профиль фрилансера не найден для пользователя " + email));
        return profile.getId();
    }
}
