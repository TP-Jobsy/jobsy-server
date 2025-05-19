package com.example.jobsyserver.features.rating.controller;

import com.example.jobsyserver.features.auth.service.SecurityService;
import com.example.jobsyserver.features.rating.dto.RatingRequestDto;
import com.example.jobsyserver.features.rating.dto.RatingResponseDto;
import com.example.jobsyserver.features.rating.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects/{projectId}/rating")
@Validated
@RequiredArgsConstructor
@Tag(name = "Rating", description = "Оценка завершённых проектов")
public class RatingController {

    private final RatingService   ratingService;
    private final SecurityService security;

    @Operation(summary = "Оценить проект",
            description = "Позволяет авторизованному CLIENT или FREELANCER выставить оценку завершённому проекту")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Оценка успешно создана"),
            @ApiResponse(responseCode = "400", description = "Неверная оценка или проект не завершён"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "403", description = "Нет роли CLIENT или FREELANCER"),
            @ApiResponse(responseCode = "404", description = "Проект или профиль не найдены"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENT','FREELANCER')")
    public ResponseEntity<Void> rateProject(
            @PathVariable Long projectId,
            @RequestBody @Valid RatingRequestDto dto
    ) {
        Long profileId = security.getCurrentProfileId();
        ratingService.rateProject(projectId, profileId, dto.getScore());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Получить оценки проекта",
            description = "Возвращает все оценки по указанному проекту")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список оценок успешно получен"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "403", description = "Нет роли CLIENT или FREELANCER"),
            @ApiResponse(responseCode = "404", description = "Проект не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENT','FREELANCER')")
    public ResponseEntity<List<RatingResponseDto>> getRatings(
            @PathVariable Long projectId
    ) {
        List<RatingResponseDto> ratings = ratingService.getRatings(projectId);
        return ResponseEntity.ok(ratings);
    }
}