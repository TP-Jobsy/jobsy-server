package com.example.jobsyserver.features.auth.service.impl;

import com.example.jobsyserver.features.auth.dto.request.AuthenticationRequest;
import com.example.jobsyserver.features.auth.dto.response.AuthenticationResponse;
import com.example.jobsyserver.features.auth.service.JwtService;
import com.example.jobsyserver.features.common.exception.BadRequestException;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.refresh.model.RefreshToken;
import com.example.jobsyserver.features.refresh.service.RefreshTokenService;
import com.example.jobsyserver.features.user.dto.UserDto;
import com.example.jobsyserver.features.user.mapper.UserMapper;
import com.example.jobsyserver.features.user.model.User;
import com.example.jobsyserver.features.user.repository.UserRepository;
import com.example.jobsyserver.features.auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception ex) {
            throw new BadRequestException("Неверные учётные данные или пользователь не найден");
        }
        String accessToken = jwtService.generateToken(request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь"));
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        UserDto userDto = userMapper.toDto(user);
        return new AuthenticationResponse(
                accessToken,
                refreshToken.getToken(),
                refreshToken.getExpiryDate(),
                userDto
        );
    }
}
