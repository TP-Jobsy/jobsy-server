package com.example.jobsyserver.controller;

import com.example.jobsyserver.dto.project.ProjectCreateDto;
import com.example.jobsyserver.dto.project.ProjectDto;
import com.example.jobsyserver.dto.project.ProjectUpdateDto;
import com.example.jobsyserver.enums.ProjectStatus;
import com.example.jobsyserver.service.ProjectService;
import com.example.jobsyserver.service.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
@Validated
public class ClientProjectController {

    private final ProjectService projectService;
    private final SecurityService securityService;

    @Operation(summary = "Получить список всех проектов", description = "Доступно всем аутентифицированным")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Проекты успешно получены"),
            @ApiResponse(responseCode = "401", description = "Не аутентифицирован"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAllProjects(
            @RequestParam(required = false) ProjectStatus status) {
        return ResponseEntity.ok(projectService.getAllProjects(status));
    }

    @Operation(summary = "Получить проект по ID", description = "Возвращает данные одного проекта")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Проект найден"),
            @ApiResponse(responseCode = "404", description = "Проект не найден"),
            @ApiResponse(responseCode = "401", description = "Не аутентифицирован"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.getProjectById(projectId));
    }

    @Operation(summary = "Создать проект", description = "Доступно только CLIENT")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Проект успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверные данные"),
            @ApiResponse(responseCode = "401", description = "Не аутентифицирован"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping
    public ResponseEntity<ProjectDto> createProject(
            @Valid @RequestBody ProjectCreateDto dto) {
        ProjectDto created = projectService.createProject(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @Operation(summary = "Обновить проект", description = "Доступно только владельцу (CLIENT)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Проект успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Неверные данные"),
            @ApiResponse(responseCode = "401", description = "Не аутентифицирован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Проект не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PreAuthorize("hasRole('CLIENT')")
    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectDto> updateProject(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectUpdateDto dto) {
        return ResponseEntity.ok(
                projectService.updateProject(projectId, dto));
    }

    @Operation(summary = "Удалить проект", description = "Доступно только владельцу (CLIENT)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Проект успешно удалён"),
            @ApiResponse(responseCode = "401", description = "Не аутентифицирован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "404", description = "Проект не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PreAuthorize("hasRole('CLIENT')")
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить проекты конкретного клиента", description = "Поиск по clientId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Проекты успешно получены"),
            @ApiResponse(responseCode = "404", description = "Клиент не найден"),
            @ApiResponse(responseCode = "401", description = "Не аутентифицирован"),
            @ApiResponse(responseCode = "403", description = "Нет прав"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ProjectDto>> getProjectsByClient(
            @PathVariable Long clientId,
            @RequestParam(required = false) ProjectStatus status) {
        return ResponseEntity.ok(
                projectService.getProjectsByClient(clientId, status));
    }

    @Operation(summary = "Получить свои проекты", description = "CLIENT → только свои")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Проекты успешно получены"),
            @ApiResponse(responseCode = "401", description = "Не аутентифицирован"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/me")
    public ResponseEntity<List<ProjectDto>> getMyProjects(
            @RequestParam(required = false) ProjectStatus status) {
        Long myClientId = securityService.getCurrentClientProfileId();
        return ResponseEntity.ok(
                projectService.getProjectsByClient(myClientId, status));
    }
}