package com.example.jobsyserver.service.impl.auth;

import com.example.jobsyserver.features.auth.dto.request.AuthenticationRequest;
import com.example.jobsyserver.features.auth.dto.response.AuthenticationResponse;
import com.example.jobsyserver.features.auth.service.impl.AuthenticationServiceImpl;
import com.example.jobsyserver.features.auth.service.impl.JwtServiceImpl;
import com.example.jobsyserver.features.user.dto.UserDto;
import com.example.jobsyserver.features.common.exception.BadRequestException;
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
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    private final String testEmail = "test@mail.com";
    private final String testPassword = "password";
    private final String testToken = "test.jwt.token";
    private User testUser;
    private UserDto testUserDto;

    @BeforeEach
    void setup(){
        this.testUser = User.builder().email(testEmail).build();
        this.testUserDto = new UserDto();
    }

    @Test
    void login_success() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail(testEmail);
        request.setPassword(testPassword);
        when(authenticationManager.authenticate(any()))
                .thenReturn(mock(Authentication.class));
        when(jwtService.generateToken(testEmail)).thenReturn(testToken);
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));
        when(userMapper.toDto(testUser)).thenReturn(testUserDto);
        AuthenticationResponse response = service.login(request);
        assertNotNull(response);
        assertEquals(testToken, response.getToken());
        assertEquals(testUserDto, response.getUser());
        verify(authenticationManager).authenticate(any());
        verify(jwtService).generateToken(testEmail);
        verify(userRepository).findByEmail(testEmail);
    }

    @Test
    void login_invalidCredentials_throwsBadRequest() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail(testEmail);
        request.setPassword("wrong_password");
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid credentials"));
        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> service.login(request)
        );
        assertEquals("Неверные учётные данные или пользователь не найден", ex.getMessage());
        verify(authenticationManager).authenticate(any());
        verifyNoInteractions(jwtService);
    }
}