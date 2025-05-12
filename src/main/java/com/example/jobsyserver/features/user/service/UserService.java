package com.example.jobsyserver.features.user.service;

import com.example.jobsyserver.features.user.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User getCurrentUser();
}
