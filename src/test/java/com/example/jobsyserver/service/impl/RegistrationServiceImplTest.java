package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.request.RegistrationRequest;
import com.example.jobsyserver.dto.response.RegistrationResponse;
import com.example.jobsyserver.enums.UserRole;
import com.example.jobsyserver.exception.BadRequestException;
import com.example.jobsyserver.model.Confirmation;
import com.example.jobsyserver.model.User;
import com.example.jobsyserver.repository.UserRepository;
import com.example.jobsyserver.service.ConfirmationService;
import com.example.jobsyserver.event.ConfirmationCodeResentEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ConfirmationService confirmationService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    private RegistrationRequest validRequest;

    @BeforeEach
    void setup() {
        validRequest = new RegistrationRequest();
        validRequest.setFirstName("John");
        validRequest.setLastName("Doe");
        validRequest.setEmail("john.doe@example.com");
        validRequest.setPhone("1234567890");
        validRequest.setDateBirth(LocalDate.of(1990, 1, 1));
        validRequest.setPassword("password");
        validRequest.setRole(UserRole.CLIENT);
    }

    @Test
    void register_ShouldRegisterUser_WhenValidRequest() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPhone("1234567890");
        user.setDateBirth(LocalDate.of(1990, 1, 1));
        user.setRole(UserRole.CLIENT);
        user.setPassword("encodedPassword");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setIsVerified(false);
        user.setIsActive(true);
        when(userRepository.existsByEmail(validRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(confirmationService.createConfirmationFor(any(User.class), any())).thenReturn(mock(Confirmation.class));
        RegistrationResponse response = registrationService.register(validRequest);
        assertNotNull(response);
        assertEquals("Регистрация прошла успешно. Проверьте почту для подтверждения", response.getMessage());
        verify(userRepository, times(1)).save(any(User.class));
        verify(eventPublisher, times(1)).publishEvent(any(ConfirmationCodeResentEvent.class));
    }

    @Test
    void register_ShouldThrowBadRequestException_WhenEmailAlreadyExists() {
        when(userRepository.existsByEmail(validRequest.getEmail())).thenReturn(true);
        BadRequestException exception = assertThrows(BadRequestException.class, () -> registrationService.register(validRequest));
        assertEquals("Пользователь с таким email уже зарегистрирован", exception.getMessage());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void register_ShouldHashPassword_WhenUserIsCreated() {
        when(userRepository.existsByEmail(validRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(validRequest.getPassword())).thenReturn("encodedPassword");
        Confirmation confirmation = mock(Confirmation.class);
        when(confirmationService.createConfirmationFor(any(User.class), any())).thenReturn(confirmation);
        when(confirmation.getId()).thenReturn(1L);  // Мокаем ID для Confirmation
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        registrationService.register(validRequest);
        verify(passwordEncoder, times(1)).encode(validRequest.getPassword());
        verify(confirmationService, times(1)).createConfirmationFor(any(User.class), any());
    }


    @Test
    void register_ShouldSendConfirmationEvent_WhenUserIsRegistered() {
        User user = new User();
        user.setId(1L);
        when(userRepository.existsByEmail(validRequest.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(confirmationService.createConfirmationFor(any(User.class), any())).thenReturn(mock(Confirmation.class));
        registrationService.register(validRequest);
        verify(eventPublisher, times(1)).publishEvent(any(ConfirmationCodeResentEvent.class));
    }
}
