package com.example.jobsyserver.controller;

import com.example.jobsyserver.dto.request.ConfirmEmailRequest;
import com.example.jobsyserver.dto.response.DefaultResponse;
import com.example.jobsyserver.service.ConfirmEmailService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Подтверждение e-mail")
    @PostMapping("/confirm-email")
    public ResponseEntity<DefaultResponse> confirmEmail(@RequestBody ConfirmEmailRequest request) {
        confirmEmailService.confirmEmail(request.getEmail(), request.getConfirmationCode());
        return ResponseEntity.ok(new DefaultResponse("Почта успешно подтверждена"));
    }
}