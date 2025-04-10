package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.enums.ConfirmationAction;
import com.example.jobsyserver.event.UserVerifiedEvent;
import com.example.jobsyserver.model.Confirmation;
import com.example.jobsyserver.model.User;
import com.example.jobsyserver.repository.ConfirmationRepository;
import com.example.jobsyserver.repository.UserRepository;
import com.example.jobsyserver.service.ConfirmEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmEmailServiceImpl implements ConfirmEmailService {

    private final ConfirmationRepository confirmationRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void confirmEmail(String email, String confirmationCode) {
        log.info("Пытаемся подтвердить email: {} с кодом {}", email, confirmationCode);

        Confirmation confirmation = confirmationRepository
                .findByUserEmailAndActionAndUsedFalse(email, ConfirmationAction.REGISTRATION)
                .orElseThrow(() -> {
                    log.error("Запись подтверждения не найдена или уже использована для email: {}", email);
                    return new RuntimeException("Запись подтверждения не найдена или уже использована");
                });

        if (LocalDateTime.now().isAfter(confirmation.getExpiresAt())) {
            User user = confirmation.getUser();
            confirmationRepository.delete(confirmation);
            userRepository.delete(user);
            log.error("Срок подтверждения истёк для пользователя с email {}. Пользователь удалён", email);
            throw new RuntimeException("Срок подтверждения истёк. Пожалуйста, зарегистрируйтесь снова");
        }

        if (!confirmation.getConfirmationCode().equals(confirmationCode)) {
            log.error("Неверный код подтверждения для email {}. Введён: {}, ожидаемый: {}",
                    email, confirmationCode, confirmation.getConfirmationCode());
            throw new RuntimeException("Неверный код подтверждения");
        }

        confirmation.setUsed(true);
        confirmationRepository.save(confirmation);
        log.info("Запись подтверждения обновлена (отмечена как использованная) для email: {}", email);

        User user = confirmation.getUser();
        user.setIsVerified(true);
        userRepository.save(user);
        log.info("Пользователь с email {} подтверждён", email);

        eventPublisher.publishEvent(new UserVerifiedEvent(user));
        log.info("Публикация события UserVerifiedEvent для пользователя с email: {}", email);
    }
}