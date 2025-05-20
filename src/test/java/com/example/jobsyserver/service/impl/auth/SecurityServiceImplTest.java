package com.example.jobsyserver.service.impl.auth;

import com.example.jobsyserver.features.auth.provider.CurrentUserProvider;
import com.example.jobsyserver.features.auth.service.impl.SecurityServiceImpl;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityServiceImplTest {

    @Mock
    private CurrentUserProvider userProv;

    @InjectMocks
    private SecurityServiceImpl securityService;

    private static final String TEST_EMAIL = "test@example.com";
    private static final Long CLIENT_PROFILE_ID = 1L;
    private static final Long FREELANCER_PROFILE_ID = 2L;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getCurrentUserEmail_ShouldReturnEmail() {
        when(userProv.getEmail()).thenReturn(TEST_EMAIL);
        String email = securityService.getCurrentUserEmail();
        assertEquals(TEST_EMAIL, email);
        verify(userProv).getEmail();
    }

    @Test
    void getCurrentUserEmail_ShouldPropagateException() {
        when(userProv.getEmail()).thenThrow(new BadCredentialsException("Пользователь не аутентифицирован"));
        BadCredentialsException ex = assertThrows(BadCredentialsException.class,
                () -> securityService.getCurrentUserEmail());
        assertEquals("Пользователь не аутентифицирован", ex.getMessage());
    }

    @Test
    void getCurrentClientProfileId_ShouldReturnProfileId() {
        when(userProv.getClientProfileId()).thenReturn(CLIENT_PROFILE_ID);
        Long id = securityService.getCurrentClientProfileId();
        assertEquals(CLIENT_PROFILE_ID, id);
        verify(userProv).getClientProfileId();
    }

    @Test
    void getCurrentClientProfileId_ShouldPropagateNotFound() {
        when(userProv.getClientProfileId())
                .thenThrow(new ResourceNotFoundException("Профиль клиента для пользователя " + TEST_EMAIL));
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> securityService.getCurrentClientProfileId());
        assertTrue(ex.getMessage().contains("Профиль клиента для пользователя"));
    }

    @Test
    void getCurrentFreelancerProfileId_ShouldReturnProfileId() {
        when(userProv.getFreelancerProfileId()).thenReturn(FREELANCER_PROFILE_ID);
        Long id = securityService.getCurrentFreelancerProfileId();
        assertEquals(FREELANCER_PROFILE_ID, id);
        verify(userProv).getFreelancerProfileId();
    }

    @Test
    void getCurrentFreelancerProfileId_ShouldPropagateNotFound() {
        when(userProv.getFreelancerProfileId())
                .thenThrow(new ResourceNotFoundException("Профиль фрилансера для пользователя " + TEST_EMAIL));
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> securityService.getCurrentFreelancerProfileId());
        assertTrue(ex.getMessage().contains("Профиль фрилансера для пользователя"));
    }

    @Test
    void getCurrentProfileId_ShouldReturnClient_WhenRoleClient() {
        com.example.jobsyserver.features.user.model.User u =
                com.example.jobsyserver.features.user.model.User.builder()
                        .role(com.example.jobsyserver.features.common.enums.UserRole.CLIENT)
                        .build();
        when(userProv.getUser()).thenReturn(u);
        when(userProv.getClientProfileId()).thenReturn(CLIENT_PROFILE_ID);
        Long id = securityService.getCurrentProfileId();
        assertEquals(CLIENT_PROFILE_ID, id);
    }

    @Test
    void getCurrentProfileId_ShouldReturnFreelancer_WhenRoleFreelancer() {
        com.example.jobsyserver.features.user.model.User u =
                com.example.jobsyserver.features.user.model.User.builder()
                        .role(com.example.jobsyserver.features.common.enums.UserRole.FREELANCER)
                        .build();
        when(userProv.getUser()).thenReturn(u);
        when(userProv.getFreelancerProfileId()).thenReturn(FREELANCER_PROFILE_ID);
        Long id = securityService.getCurrentProfileId();
        assertEquals(FREELANCER_PROFILE_ID, id);
    }

    @Test
    void getCurrentProfileId_ShouldThrow_WhenNoRole() {
        com.example.jobsyserver.features.user.model.User u =
                com.example.jobsyserver.features.user.model.User.builder()
                        .role(null)
                        .build();
        when(userProv.getUser()).thenReturn(u);
        assertThrows(AccessDeniedException.class,
                () -> securityService.getCurrentProfileId());
    }
}