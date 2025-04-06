package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendConfirmationEmail(String email, String confirmationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Подтверждение регистрации");
        String text = String.format(
                "Здравствуйте!\n\nВаш код подтверждения для регистрации: %s\n\nС уважением,\nКоманда Jobsy",
                confirmationCode
        );
        message.setText(text);
        mailSender.send(message);
    }

    @Override
    public void sendPasswordResetEmail(String email, String resetCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Запрос на восстановление пароля");
        String text = String.format(
                "Здравствуйте!\n\nВаш код для восстановления пароля: %s\n" +
                        "Если вы не запрашивали восстановление пароля, просто проигнорируйте это письмо.\n\n" +
                        "С уважением,\nКоманда Jobsy",
                resetCode
        );
        message.setText(text);
        mailSender.send(message);
    }
}
