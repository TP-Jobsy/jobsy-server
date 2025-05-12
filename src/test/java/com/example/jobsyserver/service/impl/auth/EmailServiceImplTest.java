package com.example.jobsyserver.service.impl.auth;

import com.example.jobsyserver.features.auth.service.impl.EmailServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    private final String testEmail = "test@example.com";
    private final String testCode = "123456";

    @Test
    void sendConfirmationEmail_shouldSendCorrectMessage() {
        emailService.sendConfirmationEmail(testEmail, testCode);
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertEquals(testEmail, sentMessage.getTo()[0]);
        assertEquals("Подтверждение регистрации", sentMessage.getSubject());
        assertTrue(sentMessage.getText().contains(testCode));
        assertTrue(sentMessage.getText().contains("Здравствуйте!"));
        assertTrue(sentMessage.getText().contains("Команда Jobsy"));
    }

    @Test
    void sendPasswordResetEmail_shouldSendCorrectMessage() {
        emailService.sendPasswordResetEmail(testEmail, testCode);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertEquals(testEmail, sentMessage.getTo()[0]);
        assertEquals("Запрос на восстановление пароля", sentMessage.getSubject());
        assertTrue(sentMessage.getText().contains(testCode));
        assertTrue(sentMessage.getText().contains("Здравствуйте!"));
        assertTrue(sentMessage.getText().contains("Команда Jobsy"));
        assertTrue(sentMessage.getText().contains("проигнорируйте это письмо"));
    }

    @Test
    void sendConfirmationEmail_shouldHandleEmptyCode() {
        emailService.sendConfirmationEmail(testEmail, "");

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertTrue(sentMessage.getText().contains("Ваш код подтверждения для регистрации: "));
    }

    @Test
    void sendPasswordResetEmail_shouldHandleNullCode() {
        emailService.sendPasswordResetEmail(testEmail, null);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertTrue(sentMessage.getText().contains("Ваш код для восстановления пароля: null"));
    }
}