package com.example.jobsyserver.features.auth.controller;

import com.example.jobsyserver.features.auth.dto.request.ConfirmEmailRequest;
import com.example.jobsyserver.features.auth.dto.request.ResendConfirmationRequest;
import com.example.jobsyserver.features.common.dto.response.DefaultResponse;
import com.example.jobsyserver.features.auth.service.ConfirmEmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    @Operation(
            summary = "Повторная отправка кода подтверждения",
            description = "Удаляет старую запись и шлёт новый код, если пользователь ещё не подтвердил e-mail"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Новый код отправлен"),
            @ApiResponse(responseCode = "400", description = "Пользователь не найден или уже подтверждён"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/resend-confirmation")
    public ResponseEntity<DefaultResponse> resendConfirmation(@Valid @RequestBody ResendConfirmationRequest req) {
        confirmEmailService.resendConfirmationCode(req.getEmail());
        return ResponseEntity.ok(new DefaultResponse("Код подтверждения успешно выслан"));
    }
}