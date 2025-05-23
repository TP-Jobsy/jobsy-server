package com.example.jobsyserver.features.auth.listener;

import com.example.jobsyserver.features.auth.event.ConfirmationCodeResentEvent;
import com.example.jobsyserver.features.auth.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConfirmationEventListener {

    private final EmailService emailService;

    @EventListener
    public void onCodeResent(ConfirmationCodeResentEvent event) {
        String email = event.user().getEmail();
        String code  = event.confirmationCode();

        switch (event.action()) {
            case REGISTRATION -> {
                log.info("Отправляем код для регистрации {} → {}", email, code);
                emailService.sendConfirmationEmail(email, code);
            }
            case PASSWORD_RESET -> {
                log.info("Отправляем код для сброса пароля {} → {}", email, code);
                emailService.sendPasswordResetEmail(email, code);
            }
            case ADMIN_LOGIN -> {
                log.info("Отправляем код для входа в админ-панель {} → {}", email, code);
                emailService.sendAdminLoginEmail(email, code);
            }
        }
    }
}
