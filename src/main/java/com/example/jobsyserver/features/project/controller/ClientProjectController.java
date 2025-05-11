package com.example.jobsyserver.features.project.controller;

import com.example.jobsyserver.features.project.dto.ProjectCreateDto;
import com.example.jobsyserver.features.project.dto.ProjectDto;
import com.example.jobsyserver.features.project.dto.ProjectUpdateDto;
import com.example.jobsyserver.features.common.enums.ProjectStatus;
import com.example.jobsyserver.features.project.service.ProjectService;
import com.example.jobsyserver.features.auth.service.SecurityService;
import com.example.jobsyserver.features.common.validation.Draft;
import com.example.jobsyserver.features.common.validation.Publish;
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

    @Operation(summary = "Создать черновик проекта")
    @PostMapping("/drafts")
    @PreAuthorize("hasRole('CLIENT')")
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(Draft.class)
    public ProjectDto createDraft(@RequestBody @Validated(Draft.class) ProjectCreateDto dto) {
        return projectService.createDraft(dto);
    }

    @Operation(summary = "Обновить черновик проекта")
    @PutMapping("/{draftId}/draft")
    @PreAuthorize("hasRole('CLIENT')")
    @Validated(Draft.class)
    public ProjectDto updateDraft(
            @PathVariable Long draftId,
            @RequestBody @Validated(Draft.class) ProjectUpdateDto dto) {
        return projectService.updateDraft(draftId, dto);
    }

    @Operation(summary = "Опубликовать черновик (финальное редактирование + статус → OPEN)")
    @PostMapping("/{draftId}/publish")
    @PreAuthorize("hasRole('CLIENT')")
    @Validated(Publish.class)
    public ProjectDto publishDraft(
            @PathVariable Long draftId,
            @RequestBody @Validated(Publish.class) ProjectUpdateDto dto) {
        return projectService.publish(draftId, dto);
    }
}