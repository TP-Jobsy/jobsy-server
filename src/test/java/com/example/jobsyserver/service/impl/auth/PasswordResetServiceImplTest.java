package com.example.jobsyserver.service.impl.auth;

import com.example.jobsyserver.features.common.enums.ConfirmationAction;
import com.example.jobsyserver.features.auth.service.impl.PasswordResetServiceImpl;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.auth.model.Confirmation;
import com.example.jobsyserver.features.user.model.User;
import com.example.jobsyserver.features.user.repository.UserRepository;
import com.example.jobsyserver.features.auth.service.ConfirmationService;
import com.example.jobsyserver.features.auth.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PasswordResetServiceImplTest {

    @InjectMocks
    private PasswordResetServiceImpl passwordResetService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConfirmationService confirmationService;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;
    private Confirmation confirmation;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("oldPasswordHash");

        confirmation = new Confirmation();
        confirmation.setConfirmationCode("resetCode");
        confirmation.setUser(user);
    }

    @Test
    void initiatePasswordReset_ShouldSendResetEmail_WhenUserExists() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(java.util.Optional.of(user));
        when(confirmationService.createConfirmationFor(user, ConfirmationAction.PASSWORD_RESET)).thenReturn(confirmation);
        passwordResetService.initiatePasswordReset("test@example.com");
        verify(emailService, times(1)).sendPasswordResetEmail(eq(user.getEmail()), eq(confirmation.getConfirmationCode()));
        verify(confirmationService, times(1)).createConfirmationFor(user, ConfirmationAction.PASSWORD_RESET);
    }

    @Test
    void initiatePasswordReset_ShouldThrowResourceNotFoundException_WhenUserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(java.util.Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> passwordResetService.initiatePasswordReset("test@example.com"));
    }

    @Test
    void confirmPasswordReset_ShouldUpdatePassword_WhenValidData() {
        String newPassword = "newPassword123";
        String resetCode = "resetCode";
        when(confirmationService.validateAndUse("test@example.com", resetCode, ConfirmationAction.PASSWORD_RESET)).thenReturn(confirmation);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");
        passwordResetService.confirmPasswordReset("test@example.com", resetCode, newPassword);
        verify(userRepository, times(1)).save(user);
        assertEquals("encodedNewPassword", user.getPassword());
        verify(passwordEncoder, times(1)).encode(newPassword);
    }

    @Test
    void confirmPasswordReset_ShouldThrowResourceNotFoundException_WhenInvalidResetCode() {
        String invalidCode = "invalidCode";
        when(confirmationService.validateAndUse("test@example.com", invalidCode, ConfirmationAction.PASSWORD_RESET))
                .thenThrow(ResourceNotFoundException.class);
        assertThrows(ResourceNotFoundException.class, () -> passwordResetService.confirmPasswordReset("test@example.com", invalidCode, "newPassword123"));
    }

    @Test
    void confirmPasswordReset_ShouldUpdatePassword_WhenSamePassword() {
        String resetCode = "resetCode";
        String samePassword = "oldPasswordHash";
        when(confirmationService.validateAndUse("test@example.com", resetCode, ConfirmationAction.PASSWORD_RESET)).thenReturn(confirmation);
        when(passwordEncoder.encode(samePassword)).thenReturn("oldPasswordHash");
        passwordResetService.confirmPasswordReset("test@example.com", resetCode, samePassword);
        verify(userRepository, times(1)).save(user);
        assertEquals("oldPasswordHash", user.getPassword());
        verify(passwordEncoder, times(1)).encode(samePassword);
    }
}
