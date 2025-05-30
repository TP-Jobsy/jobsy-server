package com.example.jobsyserver.features.admin.service.impl;

import com.example.jobsyserver.features.admin.dto.AdminLoginRequest;
import com.example.jobsyserver.features.admin.dto.ConfirmAdminLoginRequest;
import com.example.jobsyserver.features.admin.service.AdminAuthService;
import com.example.jobsyserver.features.auth.dto.request.TokenRefreshRequest;
import com.example.jobsyserver.features.auth.dto.response.AuthenticationResponse;
import com.example.jobsyserver.features.auth.dto.response.TokenRefreshResponse;
import com.example.jobsyserver.features.auth.event.ConfirmationCodeResentEvent;
import com.example.jobsyserver.features.auth.service.ConfirmationService;
import com.example.jobsyserver.features.auth.service.impl.JwtServiceImpl;
import com.example.jobsyserver.features.common.config.admin.AdminTestAuthProperties;
import com.example.jobsyserver.features.common.dto.response.DefaultResponse;
import com.example.jobsyserver.features.common.enums.ConfirmationAction;
import com.example.jobsyserver.features.common.enums.UserRole;
import com.example.jobsyserver.features.common.exception.BadRequestException;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.refresh.model.RefreshToken;
import com.example.jobsyserver.features.refresh.service.RefreshTokenService;
import com.example.jobsyserver.features.user.dto.UserDto;
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
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final AdminTestAuthProperties testProps;

    @Override
    public DefaultResponse requestLoginCode(AdminLoginRequest request) {
        if (testProps.enabled() && testProps.email().equalsIgnoreCase(request.email())) {
            return new DefaultResponse("Код отправлен (тестовый режим)");
        }

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
        if (testProps.enabled() && testProps.email().equalsIgnoreCase(request.email())) {
            if (!testProps.code().equals(request.confirmationCode())) {
                throw new BadRequestException("Неверный код подтверждения (тестовый режим)");
            }
            return issueTokensFor(request.email());
        }

        confirmationService.validateAndUse(
                request.email(),
                request.confirmationCode(),
                ConfirmationAction.ADMIN_LOGIN
        );
        return issueTokensFor(request.email());
    }

    @Override
    public TokenRefreshResponse refresh(TokenRefreshRequest request) {
        RefreshToken existing = refreshTokenService.verifyAndGet(request.getRefreshToken());
        if (!existing.getUser().getRole().equals(UserRole.ADMIN)) {
            throw new BadRequestException("Недопустимый токен для администратора");
        }
        String newAccessToken = jwtService.generateToken(existing.getUser().getEmail());
        RefreshToken rotated = refreshTokenService.rotateRefreshToken(existing);
        return new TokenRefreshResponse(
                newAccessToken,
                rotated.getToken(),
                rotated.getExpiryDate()
        );
    }

    @Override
    public Optional<User> findByEmailAndRole(String email, UserRole role) {
        return userRepository.findByEmailAndRole(email, role);
    }

    private AuthenticationResponse issueTokensFor(String email) {
        String accessToken = jwtService.generateToken(email);
        User user = userRepository
                .findByEmailAndRole(email, UserRole.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Администратор"));
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        UserDto dto = userMapper.toDto(user);
        return new AuthenticationResponse(
                accessToken,
                refreshToken.getToken(),
                refreshToken.getExpiryDate(),
                dto
        );
    }
}