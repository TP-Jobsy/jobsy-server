package com.example.jobsyserver.features.auth.service.impl;

import com.example.jobsyserver.features.common.enums.ConfirmationAction;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.auth.model.Confirmation;
import com.example.jobsyserver.features.user.model.User;
import com.example.jobsyserver.features.auth.service.ConfirmationService;
import com.example.jobsyserver.features.auth.service.EmailService;
import com.example.jobsyserver.features.auth.service.PasswordResetService;
import com.example.jobsyserver.features.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final ConfirmationService confirmationService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void initiatePasswordReset(String email) {
        log.info("Запрос на восстановление пароля для email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", "email", email));
        Confirmation confirmation = confirmationService.createConfirmationFor(
                user, ConfirmationAction.PASSWORD_RESET);
        log.info("Сгенерирован и сохранён код восстановления для userId={}: {}", user.getId(),
                confirmation.getConfirmationCode());
        emailService.sendPasswordResetEmail(user.getEmail(), confirmation.getConfirmationCode());
        log.info("Письмо с кодом восстановления отправлено на email={}", user.getEmail());
    }

    @Override
    public void confirmPasswordReset(String email, String resetCode, String newPassword) {
        log.info("Подтверждение восстановления пароля для email={}", email);
        Confirmation confirmation = confirmationService.validateAndUse(
                email, resetCode, ConfirmationAction.PASSWORD_RESET);
        User user = confirmation.getUser();
        String oldHash = user.getPassword();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        log.info("Пароль пользователя id={} обновлён (старый хеш={}, новый хеш={})",
                user.getId(), oldHash, user.getPassword());
    }
}