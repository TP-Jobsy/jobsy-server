package com.example.jobsyserver.features.admin.service.impl;

import com.example.jobsyserver.features.admin.dto.AdminLoginRequest;
import com.example.jobsyserver.features.admin.dto.ConfirmAdminLoginRequest;
import com.example.jobsyserver.features.admin.service.AdminAuthService;
import com.example.jobsyserver.features.auth.dto.response.AuthenticationResponse;
import com.example.jobsyserver.features.auth.event.ConfirmationCodeResentEvent;
import com.example.jobsyserver.features.auth.service.ConfirmationService;
import com.example.jobsyserver.features.auth.service.impl.JwtServiceImpl;
import com.example.jobsyserver.features.common.dto.response.DefaultResponse;
import com.example.jobsyserver.features.common.enums.ConfirmationAction;
import com.example.jobsyserver.features.common.enums.UserRole;
import com.example.jobsyserver.features.common.exception.BadRequestException;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.user.mapper.UserMapper;
import com.example.jobsyserver.features.user.model.User;
import com.example.jobsyserver.features.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminAuthServiceImpl implements AdminAuthService {

    private final UserRepository userRepository;
    private final ConfirmationService confirmationService;
    private final JwtServiceImpl jwtService;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public DefaultResponse requestLoginCode(AdminLoginRequest request) {
        User user = userRepository
                .findByEmailAndRole(request.email(), UserRole.ADMIN)
                .orElseThrow(() -> new BadRequestException("Администратор не найден"));

        var confirmation = confirmationService.createConfirmationFor(user, ConfirmationAction.ADMIN_LOGIN);

        eventPublisher.publishEvent(new ConfirmationCodeResentEvent(
                user,
                confirmation.getConfirmationCode(),
                ConfirmationAction.ADMIN_LOGIN
        ));
        return new DefaultResponse("Код отправлен на почту");
    }

    @Override
    public AuthenticationResponse confirmLoginCode(ConfirmAdminLoginRequest request) {
        confirmationService.validateAndUse(
                request.email(),
                request.confirmationCode(),
                ConfirmationAction.ADMIN_LOGIN
        );

        String token = jwtService.generateToken(request.email());
        var userDto = userRepository.findByEmail(request.email())
                .map(userMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь"));

        return new AuthenticationResponse(token, userDto);
    }

    @Override
    public Optional<User> findByEmailAndRole(String email, UserRole role) {
        return userRepository.findByEmailAndRole(email, role);
    }
}