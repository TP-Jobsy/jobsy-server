package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.request.AuthenticationRequest;
import com.example.jobsyserver.dto.response.AuthenticationResponse;
import com.example.jobsyserver.exception.UserNotFoundException;
import com.example.jobsyserver.mapper.UserMapper;
import com.example.jobsyserver.repository.UserRepository;
import com.example.jobsyserver.service.AuthenticationService;
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
    private final JwtServiceImpl jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtService.generateToken(request.getEmail());
        var userDto = userRepository.findByEmail(request.getEmail())
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        return new AuthenticationResponse(token, userDto);
    }
}
