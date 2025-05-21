package com.example.jobsyserver.features.project.controller;

import com.example.jobsyserver.features.project.dto.ProjectDto;
import com.example.jobsyserver.features.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectCompletionController {

    private final ProjectService projectService;

    @Operation(summary = "Завершить проект как клиент", description = "Клиент отмечает проект как выполненный им")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Проект отмечен как завершённый клиентом"),
            @ApiResponse(responseCode = "403", description = "Не ваш проект"),
            @ApiResponse(responseCode = "404", description = "Проект не найден"),
            @ApiResponse(responseCode = "401", description = "Не аутентифицирован"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PreAuthorize("hasRole('CLIENT')")
    @PatchMapping("/{projectId}/complete/client")
    public ResponseEntity<ProjectDto> completeByClient(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.completeByClient(projectId));
    }

    @Operation(summary = "Завершить проект как фрилансер", description = "Фрилансер отмечает проект как выполненный им")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Проект отмечен как завершённый фрилансером"),
            @ApiResponse(responseCode = "403", description = "Вы не назначены на этот проект"),
            @ApiResponse(responseCode = "404", description = "Проект не найден"),
            @ApiResponse(responseCode = "401", description = "Не аутентифицирован"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PreAuthorize("hasRole('FREELANCER')")
    @PatchMapping("/{projectId}/complete/freelancer")
    public ResponseEntity<ProjectDto> completeByFreelancer(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.completeByFreelancer(projectId));
    }
}
