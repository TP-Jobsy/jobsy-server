package com.example.jobsyserver.controller;

import com.example.jobsyserver.dto.request.RegistrationRequest;
import com.example.jobsyserver.dto.response.RegistrationResponse;
import com.example.jobsyserver.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "auth", description = "Аутентификация и регистрация")
public class RegistrationController {

    private final RegistrationService registrationService;

    @Operation(summary = "Регистрация нового пользователя")
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody RegistrationRequest request) {
        log.info("Начало регистрации пользователя с email: {}", request.getEmail());
        try {
            RegistrationResponse response = registrationService.register(request);
            log.info("Пользователь с email {} успешно зарегистрирован", request.getEmail());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception ex) {
            log.error("Ошибка при регистрации пользователя с email {}: {}", request.getEmail(), ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
