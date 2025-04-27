package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.enums.ConfirmationAction;
import com.example.jobsyserver.event.ConfirmationCodeResentEvent;
import com.example.jobsyserver.event.UserVerifiedEvent;
import com.example.jobsyserver.exception.BadRequestException;
import com.example.jobsyserver.model.Confirmation;
import com.example.jobsyserver.model.User;
import com.example.jobsyserver.service.ConfirmationService;
import com.example.jobsyserver.service.ConfirmEmailService;
import com.example.jobsyserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ConfirmEmailServiceImpl implements ConfirmEmailService {

    private final ConfirmationService confirmationService;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void confirmEmail(String email, String confirmationCode) {
        log.info("Подтверждаем email={} с кодом={}", email, confirmationCode);
        Confirmation confirmation = confirmationService.validateAndUse(email, confirmationCode, ConfirmationAction.REGISTRATION);
        User user = confirmation.getUser();
        user.setIsVerified(true);
        userRepository.save(user);
        log.info("Пользователь {} успешно верифицирован, публикуем событие", email);
        eventPublisher.publishEvent(new UserVerifiedEvent(user));
    }

    @Override
    public void resendConfirmationCode(String email) {
        log.info("Повторная генерация кода для {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Пользователь с email " + email + " не найден"));
        if (Boolean.TRUE.equals(user.getIsVerified())) {
            throw new BadRequestException("Email уже подтверждён");
        }
        Confirmation newConfirmation = confirmationService.createConfirmationFor(user, ConfirmationAction.REGISTRATION);
        log.info("Сгенерирован новый код {}, публикуем событие", newConfirmation.getConfirmationCode());
        eventPublisher.publishEvent(
                new ConfirmationCodeResentEvent(user, newConfirmation.getConfirmationCode())
        );
    }
}