package com.example.jobsyserver.features.admin.service;

import com.example.jobsyserver.features.common.enums.UserRole;
import com.example.jobsyserver.features.user.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface UserAdminService {
    Page<UserDto> search(String term, UserRole role,
                         LocalDateTime from, LocalDateTime to,
                         Pageable pageable);
}
