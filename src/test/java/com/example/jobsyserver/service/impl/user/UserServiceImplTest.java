package com.example.jobsyserver.service.impl.user;

import com.example.jobsyserver.features.common.enums.UserRole;
import com.example.jobsyserver.features.user.model.User;
import com.example.jobsyserver.features.user.repository.UserRepository;
import com.example.jobsyserver.features.user.service.impl.UserServiceImpl;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserServiceImpl userService;

    private static final String testEmail = "test@example.com";
    private static final String testPassword = "password";

    private User testUser;

    @BeforeEach
    void setup() {
        testUser = new User();
        testUser.setEmail(testEmail);
        testUser.setPassword(testPassword);
        testUser.setRole(UserRole.CLIENT);

        SecurityContext context = mock(SecurityContext.class);
        SecurityContextHolder.setContext(context);
    }

    @Test
    void getCurrentUser_ShouldReturnUser_WhenUserIsAuthenticated() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(testEmail);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(userRepository.findByEmail(testEmail)).thenReturn(java.util.Optional.of(testUser));
        User user = userService.getCurrentUser();

        assertNotNull(user);
        assertEquals(testEmail, user.getEmail());
        assertEquals(testPassword, user.getPassword());
    }

    @Test
    void getCurrentUser_ShouldThrowBadCredentialsException_WhenUserIsNotAuthenticated() {
        when(authentication.isAuthenticated()).thenReturn(false);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> userService.getCurrentUser());
        assertEquals("Пользователь не аутентифицирован", exception.getMessage());
    }

    @Test
    void getCurrentUser_ShouldThrowBadCredentialsException_WhenUserNotFound() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(testEmail);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(userRepository.findByEmail(testEmail)).thenReturn(java.util.Optional.empty());

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> userService.getCurrentUser());
        assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        when(userRepository.findByEmail(testEmail)).thenReturn(java.util.Optional.of(testUser));
        UserDetails userDetails = userService.loadUserByUsername(testEmail);
        assertNotNull(userDetails);
        assertEquals(testEmail, userDetails.getUsername());
        assertEquals(testPassword, userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + testUser.getRole().name())));
    }


    @Test
    void loadUserByUsername_ShouldThrowUsernameNotFoundException_WhenUserNotFound() {
        when(userRepository.findByEmail(testEmail)).thenReturn(java.util.Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(testEmail));
        assertEquals("Пользователь не найден с email: " + testEmail, exception.getMessage());
    }
}
