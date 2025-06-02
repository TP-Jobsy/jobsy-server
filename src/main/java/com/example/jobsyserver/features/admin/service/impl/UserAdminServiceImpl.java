package com.example.jobsyserver.features.admin.service.impl;

import com.example.jobsyserver.features.admin.service.UserAdminService;
import com.example.jobsyserver.features.common.enums.UserRole;
import com.example.jobsyserver.features.user.dto.UserDto;
import com.example.jobsyserver.features.user.mapper.UserMapper;
import com.example.jobsyserver.features.user.model.User;
import com.example.jobsyserver.features.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.example.jobsyserver.features.user.specification.UserSpecifications.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserAdminServiceImpl implements UserAdminService {

    private final UserRepository userRepo;
    private final UserMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> search(
            String term, UserRole role,
            LocalDateTime from, LocalDateTime to,
            Pageable pageable
    ) {
        Specification<User> spec = Specification
                .where(textSearch(term))
                .and(hasRole(role))
                .and(registeredBetween(from, to));
        return userRepo.findAll(spec, pageable)
                .map(mapper::toDto);
    }
}