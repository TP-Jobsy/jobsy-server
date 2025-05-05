package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.enums.ConfirmationAction;
import com.example.jobsyserver.event.ConfirmationCodeResentEvent;
import com.example.jobsyserver.event.UserVerifiedEvent;
import com.example.jobsyserver.exception.BadRequestException;
import com.example.jobsyserver.model.Confirmation;
import com.example.jobsyserver.model.User;
import com.example.jobsyserver.repository.UserRepository;
import com.example.jobsyserver.service.ConfirmationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfirmEmailServiceImplTest {

    @Mock
    private ConfirmationService confirmationService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private ConfirmEmailServiceImpl service;

    private final String testEmail = "test@example.com";
    private final String testCode = "123456";
    private User testUser;
    private Confirmation testConfirmation;

    @BeforeEach
    void setup(){
        testUser = User.builder()
                .email(testEmail)
                .isVerified(false)
                .build();
        testConfirmation = Confirmation.builder()
                .user(testUser)
                .confirmationCode(testCode)
                .expiresAt(LocalDateTime.now().plusMinutes(30))
                .build();
    }

    @Test
    void confirmEmail_shouldVerifyUserAndPublishEvent() {
        when(confirmationService.validateAndUse(testEmail, testCode, ConfirmationAction.REGISTRATION))
                .thenReturn(testConfirmation);
        when(userRepository.save(testUser)).thenReturn(testUser);

        service.confirmEmail(testEmail, testCode);

        assertTrue(testUser.getIsVerified());
        verify(userRepository).save(testUser);

        ArgumentCaptor<UserVerifiedEvent> eventCaptor = ArgumentCaptor.forClass(UserVerifiedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        assertEquals(testUser, eventCaptor.getValue().user());
    }

    @Test
    void confirmEmail_shouldThrowWhenValidationFails() {
        when(confirmationService.validateAndUse(testEmail, testCode, ConfirmationAction.REGISTRATION))
                .thenThrow(new BadRequestException("Invalid code"));

        assertThrows(BadRequestException.class,
                () -> service.confirmEmail(testEmail, testCode));
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void resendConfirmationCode_shouldCreateNewConfirmationAndPublishEvent() {
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));
        when(confirmationService.findActiveByEmail(testEmail, ConfirmationAction.REGISTRATION))
                .thenReturn(Optional.empty());
        when(confirmationService.createConfirmationFor(testUser, ConfirmationAction.REGISTRATION))
                .thenReturn(testConfirmation);

        service.resendConfirmationCode(testEmail);

        ArgumentCaptor<ConfirmationCodeResentEvent> eventCaptor = ArgumentCaptor.forClass(ConfirmationCodeResentEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        assertEquals(testUser, eventCaptor.getValue().user());
        assertEquals(testCode, eventCaptor.getValue().confirmationCode());
    }

    @Test
    void resendConfirmationCode_shouldThrowWhenUserNotFound() {
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class,
                () -> service.resendConfirmationCode(testEmail));
        verifyNoInteractions(confirmationService);
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void resendConfirmationCode_shouldThrowWhenAlreadyVerified() {
        User verifiedUser = User.builder().email(testEmail).isVerified(true).build();
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(verifiedUser));

        assertThrows(BadRequestException.class,
                () -> service.resendConfirmationCode(testEmail));
        verifyNoInteractions(confirmationService);
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void resendConfirmationCode_shouldThrowWhenActiveCodeExists() {
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));
        when(confirmationService.findActiveByEmail(testEmail, ConfirmationAction.REGISTRATION))
                .thenReturn(Optional.of(testConfirmation));

        assertThrows(BadRequestException.class,
                () -> service.resendConfirmationCode(testEmail));
        verifyNoMoreInteractions(confirmationService);
        verifyNoInteractions(eventPublisher);
    }
}