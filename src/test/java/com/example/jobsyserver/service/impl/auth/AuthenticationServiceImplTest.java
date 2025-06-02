package com.example.jobsyserver.service.impl.auth;

import com.example.jobsyserver.features.auth.dto.request.AuthenticationRequest;
import com.example.jobsyserver.features.auth.dto.response.AuthenticationResponse;
import com.example.jobsyserver.features.auth.service.impl.AuthenticationServiceImpl;
import com.example.jobsyserver.features.auth.service.impl.JwtServiceImpl;
import com.example.jobsyserver.features.common.exception.BadRequestException;
import com.example.jobsyserver.features.refresh.model.RefreshToken;
import com.example.jobsyserver.features.refresh.service.RefreshTokenService;
import com.example.jobsyserver.features.user.dto.UserDto;
import com.example.jobsyserver.features.user.mapper.UserMapper;
import com.example.jobsyserver.features.user.model.User;
import com.example.jobsyserver.features.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @InjectMocks
    private AuthenticationServiceImpl service;

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtServiceImpl jwtService;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    private final String testEmail = "test@mail.com";
    private final String testRefreshToken = "refresh-token-uuid";
    private final Instant testExpiry = Instant.now().plusSeconds(3600);
    private User testUser;
    private UserDto testUserDto;
    private RefreshToken testRefreshTokenEntity;

    @BeforeEach
    void setup() {
        this.testUser = User.builder().email(testEmail).build();
        this.testUserDto = new UserDto();
        this.testRefreshTokenEntity = RefreshToken.builder()
                .token(testRefreshToken)
                .expiryDate(testExpiry)
                .user(testUser)
                .build();
    }

    @Test
    void login_success() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail(testEmail);
        String testPassword = "password";
        request.setPassword(testPassword);

        when(authenticationManager.authenticate(any()))
                .thenReturn(mock(Authentication.class));
        String testAccessToken = "test.jwt.token";
        when(jwtService.generateToken(testEmail)).thenReturn(testAccessToken);
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));
        when(userMapper.toDto(testUser)).thenReturn(testUserDto);
        when(refreshTokenService.createRefreshToken(testUser)).thenReturn(testRefreshTokenEntity);

        AuthenticationResponse response = service.login(request);

        assertNotNull(response);
        assertEquals(testAccessToken, response.getAccessToken());
        assertEquals(testRefreshToken, response.getRefreshToken());
        assertEquals(testExpiry, response.getRefreshTokenExpiry());
        assertEquals(testUserDto, response.getUser());

        verify(authenticationManager).authenticate(any());
        verify(jwtService).generateToken(testEmail);
        verify(userRepository).findByEmail(testEmail);
        verify(refreshTokenService).createRefreshToken(testUser);
    }

    @Test
    void login_invalidCredentials_throwsBadCredentials() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail(testEmail);
        request.setPassword("wrong_password");
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Неверные учётные данные или пользователь не найден"));
        BadCredentialsException ex = assertThrows(
                BadCredentialsException.class,
                () -> service.login(request)
        );
        assertEquals("Неверные учётные данные или пользователь не найден", ex.getMessage());
        verify(authenticationManager).authenticate(any());
        verifyNoInteractions(jwtService, refreshTokenService);
    }
}