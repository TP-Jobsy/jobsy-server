package com.example.jobsyserver.listener;

import com.example.jobsyserver.event.ConfirmationCodeResentEvent;
import com.example.jobsyserver.service.EmailService;
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
        log.info("Отправляем повторный код подтверждения {} → {}", email, code);
        emailService.sendConfirmationEmail(email, code);
    }
}
