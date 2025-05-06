package com.example.jobsyserver.service;

import com.example.jobsyserver.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User getCurrentUser();
}
