package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.enums.ConfirmationAction;
import com.example.jobsyserver.exception.BadRequestException;
import com.example.jobsyserver.model.Confirmation;
import com.example.jobsyserver.model.User;
import com.example.jobsyserver.repository.ConfirmationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfirmationServiceImplTest {

    @Mock
    private ConfirmationRepository repo;

    @InjectMocks
    private ConfirmationServiceImpl service;

    private User testUser;
    private final String testCode = "123456";
    private ConfirmationAction testAction;
    private Confirmation testConfirmation;

    @BeforeEach
    void setup(){
        testUser = User.builder().email("test@example.com").build();
        testAction = ConfirmationAction.REGISTRATION;
        testConfirmation = Confirmation.builder()
                .id(1L)
                .user(testUser)
                .action(testAction)
                .confirmationCode(testCode)
                .expiresAt(LocalDateTime.now().plusMinutes(30))
                .used(false)
                .build();
    }

    @Test
    void validateAndUse_shouldValidateAndMarkAsUsed() {
        when(repo.findFirstByUserEmailAndActionAndUsedFalseOrderByExpiresAtDesc(testUser.getEmail(), testAction))
                .thenReturn(Optional.of(testConfirmation));
        when(repo.save(testConfirmation)).thenReturn(testConfirmation);
        Confirmation result = service.validateAndUse(testUser.getEmail(), testCode, testAction);
        assertTrue(result.getUsed());
        verify(repo).save(testConfirmation);
    }

    @Test
    void validateAndUse_shouldThrowWhenCodeNotFound() {
        when(repo.findFirstByUserEmailAndActionAndUsedFalseOrderByExpiresAtDesc(testUser.getEmail(), testAction))
                .thenReturn(Optional.empty());

        assertThrows(BadRequestException.class,
                () -> service.validateAndUse(testUser.getEmail(), testCode, testAction));
    }

    @Test
    void validateAndUse_shouldThrowWhenCodeExpired() {
        Confirmation expired = Confirmation.builder()
                .expiresAt(LocalDateTime.now().minusMinutes(1))
                .build();
        when(repo.findFirstByUserEmailAndActionAndUsedFalseOrderByExpiresAtDesc(testUser.getEmail(), testAction))
                .thenReturn(Optional.of(expired));

        assertThrows(BadRequestException.class,
                () -> service.validateAndUse(testUser.getEmail(), testCode, testAction));
    }

    @Test
    void validateAndUse_shouldThrowWhenCodeMismatch() {
        when(repo.findFirstByUserEmailAndActionAndUsedFalseOrderByExpiresAtDesc(testUser.getEmail(), testAction))
                .thenReturn(Optional.of(testConfirmation));

        assertThrows(BadRequestException.class,
                () -> service.validateAndUse(testUser.getEmail(), "wrong_code", testAction));
    }

    @Test
    void deleteExpired_shouldDeleteExpiredConfirmations() {
        LocalDateTime now = LocalDateTime.now();

        List<Confirmation> expired = List.of(
                Confirmation.builder().expiresAt(now.minusMinutes(1)).build(),
                Confirmation.builder().expiresAt(now.minusHours(1)).build()
        );

        when(repo.findAllByActionAndUsedFalseAndExpiresAtBefore(eq(testAction), any(LocalDateTime.class)))
                .thenReturn(expired);

        List<Confirmation> result = service.deleteExpired(testAction);

        assertEquals(2, result.size());
        verify(repo).deleteAll(expired);
    }


    @Test
    void findActiveByEmail_shouldReturnActiveConfirmation() {
        when(repo.findFirstByUserEmailAndActionAndUsedFalseOrderByExpiresAtDesc(testUser.getEmail(), testAction))
                .thenReturn(Optional.of(testConfirmation));

        Optional<Confirmation> result = service.findActiveByEmail(testUser.getEmail(), testAction);

        assertTrue(result.isPresent());
        assertEquals(testConfirmation, result.get());
    }

    @Test
    void findActiveByEmail_shouldFilterExpired() {
        Confirmation expired = Confirmation.builder()
                .expiresAt(LocalDateTime.now().minusMinutes(1))
                .build();
        when(repo.findFirstByUserEmailAndActionAndUsedFalseOrderByExpiresAtDesc(testUser.getEmail(), testAction))
                .thenReturn(Optional.of(expired));

        Optional<Confirmation> result = service.findActiveByEmail(testUser.getEmail(), testAction);

        assertTrue(result.isEmpty());
    }
}