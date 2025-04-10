package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.request.RegistrationRequest;
import com.example.jobsyserver.dto.response.RegistrationResponse;
import com.example.jobsyserver.enums.ConfirmationAction;
import com.example.jobsyserver.exception.BadRequestException;
import com.example.jobsyserver.model.Confirmation;
import com.example.jobsyserver.model.User;
import com.example.jobsyserver.repository.ConfirmationRepository;
import com.example.jobsyserver.repository.UserRepository;
import com.example.jobsyserver.service.EmailService;
import com.example.jobsyserver.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;
    private final ConfirmationRepository confirmationRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtService;

    @Override
    @Transactional
    public RegistrationResponse register(RegistrationRequest request) {
        log.info("Регистрация пользователя с email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Пользователь с таким email уже зарегистрирован");
        }

        if (LocalDate.now().minusYears(18).isBefore(request.getDateBirth())) {
            throw new BadRequestException("Пользователь должен быть старше 18 лет");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .dateBirth(request.getDateBirth())
                .role(request.getRole())
                .isVerified(false)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);
        log.info("Пользователь сохранен в базе с id: {}", savedUser.getId());

        String confirmationCode = String.format("%04d", (int)(Math.random() * 10000));
        log.info("Сгенерирован код подтверждения: {}", confirmationCode);

        Confirmation confirmation = new Confirmation();
        confirmation.setUser(savedUser);
        confirmation.setAction(ConfirmationAction.REGISTRATION);
        confirmation.setConfirmationCode(confirmationCode);
        confirmation.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        confirmation.setUsed(false);
        confirmationRepository.save(confirmation);
        log.info("Запись подтверждения сохранена для пользователя id: {}", savedUser.getId());

        try {
            emailService.sendConfirmationEmail(savedUser.getEmail(), confirmationCode);
            log.info("Письмо с кодом подтверждения отправлено на: {}", savedUser.getEmail());
        } catch (Exception ex) {
            log.error("Ошибка отправки email на {}: {}", savedUser.getEmail(), ex.getMessage(), ex);
            throw ex;
        }

        String token = jwtService.generateToken(savedUser.getEmail());
        log.info("JWT-токен сгенерирован для пользователя с email: {}", savedUser.getEmail());

        String message = "Регистрация прошла успешно. Проверьте свою почту для подтверждения";
        return new RegistrationResponse(savedUser.getId(), message);
    }
}