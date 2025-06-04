package com.example.jobsyserver.features.auth.controller;

import com.example.jobsyserver.features.auth.dto.request.PasswordResetRequest;
import com.example.jobsyserver.features.auth.service.ConfirmationService;
import com.example.jobsyserver.features.common.dto.response.DefaultResponse;
import com.example.jobsyserver.features.auth.dto.request.PasswordResetConfirmRequest;
import com.example.jobsyserver.features.auth.service.PasswordResetService;
import com.example.jobsyserver.features.common.enums.ConfirmationAction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/password-reset")
@RequiredArgsConstructor
@Tag(name = "auth", description = "Восстановление пароля")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;
    private final ConfirmationService confirmationService;

    @Operation(summary = "Запрос на восстановление пароля",
            description = "Инициирует процесс восстановления пароля, отправляя 4-значный код на e-mail пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Код для восстановления пароля отправлен"),
            @ApiResponse(responseCode = "400", description = "Неверный формат запроса"),
            @ApiResponse(responseCode = "404", description = "Пользователь с указанным e-mail не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/request")
    public ResponseEntity<DefaultResponse> requestPasswordReset(@RequestBody PasswordResetRequest request) {
        passwordResetService.initiatePasswordReset(request.getEmail());
        return ResponseEntity.ok(new DefaultResponse("Код для восстановления пароля отправлен"));
    }

    @Operation(summary = "Подтверждение восстановления пароля",
            description = "Подтверждает восстановление пароля, проверяя код, отправленный на e-mail пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пароль успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Неверный формат запроса или код восстановления неверный/просрочен"),
            @ApiResponse(responseCode = "404", description = "Пользователь с указанным e-mail не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/confirm")
    public ResponseEntity<DefaultResponse> confirmPasswordReset(@RequestBody PasswordResetConfirmRequest request) {
        passwordResetService.confirmPasswordReset(request.getEmail(), request.getResetCode(), request.getNewPassword());
        return ResponseEntity.ok(new DefaultResponse("Пароль успешно обновлён"));
    }

    @PostMapping("/validate-reset-code")
    @ResponseStatus(HttpStatus.OK)
    public void validateResetCode(
            @RequestParam String email,
            @RequestParam String code
    ) {
        confirmationService.validateAndUse(email, code, ConfirmationAction.PASSWORD_RESET);
    }
}
