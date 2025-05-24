package com.example.jobsyserver.features.auth.controller;

import com.example.jobsyserver.features.auth.dto.request.AuthenticationRequest;
import com.example.jobsyserver.features.auth.dto.request.TokenRefreshRequest;
import com.example.jobsyserver.features.auth.dto.response.AuthenticationResponse;
import com.example.jobsyserver.features.auth.dto.response.TokenRefreshResponse;
import com.example.jobsyserver.features.auth.service.JwtService;
import com.example.jobsyserver.features.common.dto.response.DefaultResponse;
import com.example.jobsyserver.features.refresh.model.RefreshToken;
import com.example.jobsyserver.features.refresh.service.RefreshTokenService;
import com.example.jobsyserver.features.user.model.User;
import com.example.jobsyserver.features.auth.service.AuthenticationService;
import com.example.jobsyserver.features.user.service.UserService;
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
@Tag(name = "auth", description = "Аутентификация")
public class AuthController {

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    @Operation(summary = "Получить информацию о текущем пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о пользователе получена успешно"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Вход пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Вход выполнен успешно"),
            @ApiResponse(responseCode = "400", description = "Неверные учетные данные или формат запроса"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")

    })
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Выход пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно вышел из системы"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/logout")
    public ResponseEntity<DefaultResponse> logout() {
        User currentUser = userService.getCurrentUser();
        refreshTokenService.revokeAllForUser(currentUser);
        return ResponseEntity.ok(new DefaultResponse("Вы успешно вышли из системы"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refreshToken(
            @RequestBody TokenRefreshRequest request
    ) {
        RefreshToken existing = refreshTokenService.verifyAndGet(request.getRefreshToken());

        String newAccessToken = jwtService.generateToken(existing.getUser().getEmail());
        RefreshToken rotated = refreshTokenService.rotateRefreshToken(existing);
        TokenRefreshResponse resp = new TokenRefreshResponse(
                newAccessToken,
                rotated.getToken(),
                rotated.getExpiryDate()
        );
        return ResponseEntity.ok(resp);
    }
}