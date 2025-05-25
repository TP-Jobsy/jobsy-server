package com.example.jobsyserver.features.admin.controller;

import com.example.jobsyserver.features.admin.dto.AdminLoginRequest;
import com.example.jobsyserver.features.admin.dto.ConfirmAdminLoginRequest;
import com.example.jobsyserver.features.admin.service.AdminAuthService;
import com.example.jobsyserver.features.auth.dto.request.TokenRefreshRequest;
import com.example.jobsyserver.features.auth.dto.response.AuthenticationResponse;
import com.example.jobsyserver.features.auth.dto.response.TokenRefreshResponse;
import com.example.jobsyserver.features.common.dto.response.DefaultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/auth")
@RequiredArgsConstructor
@Tag(name = "admin-auth", description = "Аутентификация администратора по коду")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    @Operation(summary = "Запрос кода для входа админа")
    @PostMapping("/login")
    public ResponseEntity<DefaultResponse> requestCode(
            @Valid @RequestBody AdminLoginRequest req
    ) {
        return ResponseEntity.ok(adminAuthService.requestLoginCode(req));
    }

    @Operation(summary = "Подтверждение кода и получение токена")
    @PostMapping("/confirm")
    public ResponseEntity<AuthenticationResponse> confirmCode(
            @Valid @RequestBody ConfirmAdminLoginRequest req
    ) {
        return ResponseEntity.ok(adminAuthService.confirmLoginCode(req));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refreshToken(
            @RequestBody TokenRefreshRequest request
    ) {
        TokenRefreshResponse response = adminAuthService.refresh(request);
        return ResponseEntity.ok(response);
    }
}
