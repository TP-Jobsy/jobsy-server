package com.example.jobsyserver.features.auth.controller;

import com.example.jobsyserver.features.auth.dto.request.RegistrationRequest;
import com.example.jobsyserver.features.auth.dto.response.RegistrationResponse;
import com.example.jobsyserver.features.common.dto.response.DefaultResponse;
import com.example.jobsyserver.features.auth.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован, код подтверждения отправлен на e-mail"),
            @ApiResponse(responseCode = "400", description = "Неверные данные запроса"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest request) {
        log.info("Начало регистрации пользователя с email: {}", request.getEmail());
        try {
            RegistrationResponse response = registrationService.register(request);
            log.info("Пользователь с email {} успешно зарегистрирован", request.getEmail());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("Ошибка при регистрации пользователя с email {}: {}", request.getEmail(), e.getMessage(), e);
            return new ResponseEntity<>(new DefaultResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            log.error("Ошибка при регистрации пользователя с email {}: {}", request.getEmail(), ex.getMessage(), ex);
            return new ResponseEntity<>(new DefaultResponse("Внутренняя ошибка сервера"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
