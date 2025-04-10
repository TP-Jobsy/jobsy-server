package com.example.jobsyserver.controller;

import com.example.jobsyserver.dto.request.ConfirmEmailRequest;
import com.example.jobsyserver.dto.response.DefaultResponse;
import com.example.jobsyserver.service.ConfirmEmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "auth", description = "Аутентификация и подтверждение")
public class ConfirmEmailController {

    private final ConfirmEmailService confirmEmailService;

    @Operation(summary = "Подтверждение e-mail", description = "Проверяет код подтверждения, отправленный на e-mail пользователя при регистрации")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Почта успешно подтверждена"),
            @ApiResponse(responseCode = "400", description = "Неверный или просроченный код подтверждения"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/confirm-email")
    public ResponseEntity<DefaultResponse> confirmEmail(@RequestBody ConfirmEmailRequest request) {
        confirmEmailService.confirmEmail(request.getEmail(), request.getConfirmationCode());
        return ResponseEntity.ok(new DefaultResponse("Почта успешно подтверждена"));
    }
}