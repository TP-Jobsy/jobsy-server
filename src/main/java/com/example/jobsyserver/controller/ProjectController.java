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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final SecurityService securityService;

    @Operation(summary = "Получить список всех проектов", description = "Возвращает список всех проектов по статусу (если указан)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Проекты успешно получены"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAllProjects(
            @RequestParam(required = false) ProjectStatus status) {
        return ResponseEntity.ok(projectService.getAllProjects(status));
    }

    @Operation(summary = "Создать проект", description = "Создание нового проекта. Доступно только пользователям с ролью CLIENT")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Проект успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверные данные для создания проекта"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет прав"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping
    public ResponseEntity<ProjectDto> createProject(
            @Valid @RequestBody ProjectCreateDto dto
    ) {
        ProjectDto created = projectService.createProject(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Обновить проект", description = "Обновление существующего проекта. Доступно только владельцу проекта с ролью CLIENT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Проект успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Неверные данные для обновления проекта"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет прав"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён: не владелец проекта"),
            @ApiResponse(responseCode = "404", description = "Проект не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PreAuthorize("hasRole('CLIENT')")
    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectDto> updateProject(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectUpdateDto dto
    ) {
        return ResponseEntity.ok(projectService.updateProject(projectId, dto));
    }

    @Operation(summary = "Удалить проект", description = "Удаление проекта. Доступно только владельцу проекта с ролью CLIENT")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Проект успешно удалён"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет прав"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён: не владелец проекта"),
            @ApiResponse(responseCode = "404", description = "Проект не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PreAuthorize("hasRole('CLIENT')")
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить проекты конкретного клиента")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Проекты успешно получены"),
            @ApiResponse(responseCode = "404", description = "Клиент не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ProjectDto>> getProjectsByClient(
            @PathVariable Long clientId,
            @RequestParam(required = false) ProjectStatus status
    ) {
        List<ProjectDto> dtos = projectService.getProjectsByClient(clientId, status);
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Получить свои проекты (текущий клиент)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Проекты успешно получены"),
            @ApiResponse(responseCode = "401", description = "Не аутентифицирован"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/me")
    public ResponseEntity<List<ProjectDto>> getMyProjects(
            @RequestParam(required = false) ProjectStatus status
    ) {
        Long clientId = securityService.getCurrentClientProfileId();
        List<ProjectDto> dtos = projectService.getProjectsByClient(clientId, status);
        return ResponseEntity.ok(dtos);
    }
}