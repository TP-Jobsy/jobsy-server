package com.example.jobsyserver.features.auth.service.impl;

import com.example.jobsyserver.features.auth.dto.request.RegistrationRequest;
import com.example.jobsyserver.features.auth.dto.response.RegistrationResponse;
import com.example.jobsyserver.features.common.enums.ConfirmationAction;
import com.example.jobsyserver.features.common.exception.BadRequestException;
import com.example.jobsyserver.features.auth.event.ConfirmationCodeResentEvent;
import com.example.jobsyserver.features.user.model.User;
import com.example.jobsyserver.features.user.repository.UserRepository;
import com.example.jobsyserver.features.auth.service.ConfirmationService;
import com.example.jobsyserver.features.auth.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;
    private final ConfirmationService confirmationService;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public RegistrationResponse register(RegistrationRequest request) {
        log.info("Регистрация пользователя с email: {}", request.getEmail());
        String normalizedEmail = request.getEmail().trim().toLowerCase(Locale.ROOT);
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new BadRequestException("Пользователь с таким email уже зарегистрирован");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(normalizedEmail)
                .phone(request.getPhone())
                .dateBirth(request.getDateBirth())
                .role(request.getRole())
                .isVerified(false)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user = userRepository.save(user);
        log.info("Пользователь сохранён с id={}", user.getId());
        var confirmation = confirmationService.createConfirmationFor(user, ConfirmationAction.REGISTRATION);
        log.info("Создана запись Confirmation(id={}), код={}",
                confirmation.getId(), confirmation.getConfirmationCode());
        eventPublisher.publishEvent(new ConfirmationCodeResentEvent(
                user, confirmation.getConfirmationCode(), ConfirmationAction.REGISTRATION
        ));
        log.info("Опубликовано событие ConfirmationCodeResentEvent для {}", user.getEmail());
        log.info("JWT сформирован для {}", user.getEmail());
        return new RegistrationResponse(user.getId(), "Регистрация прошла успешно. Проверьте почту для подтверждения");
    }
}