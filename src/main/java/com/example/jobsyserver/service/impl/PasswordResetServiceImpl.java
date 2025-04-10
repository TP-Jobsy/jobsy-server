package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.enums.ConfirmationAction;
import com.example.jobsyserver.exception.BadRequestException;
import com.example.jobsyserver.exception.UserNotFoundException;
import com.example.jobsyserver.model.Confirmation;
import com.example.jobsyserver.model.User;
import com.example.jobsyserver.repository.ConfirmationRepository;
import com.example.jobsyserver.repository.UserRepository;
import com.example.jobsyserver.service.EmailService;
import com.example.jobsyserver.service.PasswordResetService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final ConfirmationRepository confirmationRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void initiatePasswordReset(String email) {
        log.info("Запрос на восстановление пароля для email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден с email " + email));
        log.info("Пользователь найден: id={}, email={}", user.getId(), user.getEmail());

        String resetCode = String.format("%04d", (int)(Math.random() * 10000));
        log.info("Сгенерирован код восстановления: {}", resetCode);

        Confirmation confirmation = new Confirmation();
        confirmation.setUser(user);
        confirmation.setAction(ConfirmationAction.PASSWORD_RESET);
        confirmation.setConfirmationCode(resetCode);
        confirmation.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        confirmation.setUsed(false);
        confirmationRepository.save(confirmation);
        log.info("Запись восстановления пароля сохранена для пользователя id: {}", user.getId());

        emailService.sendPasswordResetEmail(user.getEmail(), resetCode);
        log.info("Письмо с кодом восстановления отправлено на email: {}", user.getEmail());
    }

    @Override
    public void confirmPasswordReset(String email, String resetCode, String newPassword) {
        log.info("Подтверждение восстановления пароля для email: {} с кодом: {}", email, resetCode);
        Confirmation confirmation = confirmationRepository
                .findByUserEmailAndActionAndUsedFalse(email, ConfirmationAction.PASSWORD_RESET)
                .orElseThrow(() -> new BadRequestException("Запись для восстановления пароля не найдена или уже использована"));
        log.info("Найдена запись восстановления пароля для пользователя id: {}", confirmation.getUser().getId());

        if (LocalDateTime.now().isAfter(confirmation.getExpiresAt())) {
            log.warn("Срок действия кода истёк для пользователя id: {}", confirmation.getUser().getId());
            confirmationRepository.delete(confirmation);
            throw new BadRequestException("Срок действия кода истёк. Пожалуйста, запросите новый");
        }

        if (!confirmation.getConfirmationCode().equals(resetCode)) {
            log.warn("Неверный код восстановления для пользователя id: {}", confirmation.getUser().getId());
            throw new BadRequestException("Неверный код восстановления");
        }

        User user = confirmation.getUser();
        String oldPasswordHash = user.getPassword();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Пароль для пользователя id: {} успешно обновлён. Старый пароль: {}, новый пароль зашифрован",
                user.getId(), oldPasswordHash);

        confirmation.setUsed(true);
        confirmationRepository.save(confirmation);
        log.info("Запись восстановления пароля отмечена как использованная для пользователя id: {}", user.getId());
    }
}