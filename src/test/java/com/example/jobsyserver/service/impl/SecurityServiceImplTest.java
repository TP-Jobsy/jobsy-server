package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.model.ClientProfile;
import com.example.jobsyserver.model.FreelancerProfile;
import com.example.jobsyserver.repository.ClientProfileRepository;
import com.example.jobsyserver.repository.FreelancerProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SecurityServiceImplTest {

    @Mock
    private ClientProfileRepository clientProfileRepository;

    @Mock
    private FreelancerProfileRepository freelancerProfileRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private SecurityServiceImpl securityService;

    private static final String TEST_EMAIL = "test@example.com";
    private static final Long CLIENT_PROFILE_ID = 1L;
    private static final Long FREELANCER_PROFILE_ID = 2L;

    @BeforeEach
    void setup() {
        SecurityContext context = mock(SecurityContext.class);
        SecurityContextHolder.setContext(context);
    }

    @Test
    void getCurrentUserEmail_ShouldReturnEmail_WhenUserIsAuthenticated() {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(TEST_EMAIL);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);

        String email = securityService.getCurrentUserEmail();

        assertEquals(TEST_EMAIL, email);
    }

    @Test
    void getCurrentUserEmail_ShouldThrowBadCredentialsException_WhenUserIsNotAuthenticated() {
        when(authentication.isAuthenticated()).thenReturn(false);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);

        BadCredentialsException exception = assertThrows(BadCredentialsException.class,
                () -> securityService.getCurrentUserEmail());
        assertEquals("Пользователь не аутентифицирован", exception.getMessage());
    }

    @Test
    void getCurrentClientProfileId_ShouldReturnProfileId_WhenClientProfileExists() {
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setId(CLIENT_PROFILE_ID);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(TEST_EMAIL);
        when(clientProfileRepository.findByUserEmail(TEST_EMAIL)).thenReturn(Optional.of(clientProfile));
        when(authentication.isAuthenticated()).thenReturn(true);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);

        Long clientProfileId = securityService.getCurrentClientProfileId();
        assertEquals(CLIENT_PROFILE_ID, clientProfileId);
    }

    @Test
    void getCurrentClientProfileId_ShouldThrowRuntimeException_WhenClientProfileNotFound() {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(TEST_EMAIL);
        when(clientProfileRepository.findByUserEmail(TEST_EMAIL)).thenReturn(Optional.empty());
        when(authentication.isAuthenticated()).thenReturn(true);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> securityService.getCurrentClientProfileId());
        assertEquals("Профиль клиента не найден для пользователя " + TEST_EMAIL, exception.getMessage());
    }

    @Test
    void getCurrentFreelancerProfileId_ShouldReturnProfileId_WhenFreelancerProfileExists() {
        FreelancerProfile freelancerProfile = new FreelancerProfile();
        freelancerProfile.setId(FREELANCER_PROFILE_ID);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(TEST_EMAIL);
        when(freelancerProfileRepository.findByUserEmail(TEST_EMAIL)).thenReturn(Optional.of(freelancerProfile));
        when(authentication.isAuthenticated()).thenReturn(true);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);

        Long freelancerProfileId = securityService.getCurrentFreelancerProfileId();

        assertEquals(FREELANCER_PROFILE_ID, freelancerProfileId);
    }

    @Test
    void getCurrentFreelancerProfileId_ShouldThrowRuntimeException_WhenFreelancerProfileNotFound() {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(TEST_EMAIL);
        when(freelancerProfileRepository.findByUserEmail(TEST_EMAIL)).thenReturn(Optional.empty());
        when(authentication.isAuthenticated()).thenReturn(true);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> securityService.getCurrentFreelancerProfileId());
        assertEquals("Профиль фрилансера не найден для пользователя " + TEST_EMAIL, exception.getMessage());
    }
}
