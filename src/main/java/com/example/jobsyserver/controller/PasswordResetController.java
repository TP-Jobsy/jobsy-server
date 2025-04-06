package com.example.jobsyserver.controller;

import com.example.jobsyserver.dto.request.PasswordResetRequest;
import com.example.jobsyserver.dto.response.DefaultResponse;
import com.example.jobsyserver.dto.response.PasswordResetConfirmRequest;
import com.example.jobsyserver.service.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/password-reset")
@RequiredArgsConstructor
@Tag(name = "auth", description = "Восстановление пароля")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @Operation(summary = "Запрос на восстановление пароля")
    @PostMapping("/request")
    public ResponseEntity<DefaultResponse> requestPasswordReset(@RequestBody PasswordResetRequest request) {
        passwordResetService.initiatePasswordReset(request.getEmail());
        return ResponseEntity.ok(new DefaultResponse("Код для восстановления пароля отправлен"));
    }

    @Operation(summary = "Подтверждение восстановления пароля")
    @PostMapping("/confirm")
    public ResponseEntity<DefaultResponse> confirmPasswordReset(@RequestBody PasswordResetConfirmRequest request) {
        passwordResetService.confirmPasswordReset(request.getEmail(), request.getResetCode(), request.getNewPassword());
        return ResponseEntity.ok(new DefaultResponse("Пароль успешно обновлён"));
    }
}
