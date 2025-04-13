package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.service.SecurityService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Override
    public String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new BadCredentialsException("Пользователь не аутентифицирован");
        }
        Object principal = auth.getPrincipal();
        return (principal instanceof UserDetails)
                ? ((UserDetails) principal).getUsername()
                : principal.toString();
    }
}
