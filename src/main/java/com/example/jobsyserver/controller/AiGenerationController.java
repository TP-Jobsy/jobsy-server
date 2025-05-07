package com.example.jobsyserver.controller;

import com.example.jobsyserver.dto.ai.GenerateDescriptionRequest;
import com.example.jobsyserver.dto.ai.GenerateDescriptionResponse;
import com.example.jobsyserver.service.AiGenerationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects/{projectId}/ai")
@RequiredArgsConstructor
@Tag(name = "AI Generation", description = "Интеграция с Novita AI для генерации описания проекта")
public class AiGenerationController {

    private final AiGenerationService aiGenerationService;

    @Operation(
            summary = "Сгенерировать описание проекта",
            description = "Принимает системный и пользовательский промты, отсылает их в Novita AI и возвращает сгенерированный текст."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Сгенерированное описание получено"),
            @ApiResponse(responseCode = "400", description = "Неверные данные запроса"),
            @ApiResponse(responseCode = "401", description = "Неавторизованный доступ"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён (не роль CLIENT или нет прав на проект)"),
            @ApiResponse(responseCode = "404", description = "Проект не найден"),
            @ApiResponse(responseCode = "429", description = "Превышен лимит AI-запросов (10 в час)"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/description")
    @PreAuthorize("hasRole('CLIENT')")
    @ResponseStatus(HttpStatus.OK)
    public GenerateDescriptionResponse generateDescription(
            @PathVariable Long projectId,
            @Valid @RequestBody GenerateDescriptionRequest request
    ) {
        String generated = aiGenerationService.generate(projectId, request);
        return new GenerateDescriptionResponse(generated);
    }
}