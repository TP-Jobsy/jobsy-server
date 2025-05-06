package com.example.jobsyserver.controller;

import com.example.jobsyserver.dto.project.ProjectApplicationDto;
import com.example.jobsyserver.dto.project.ProjectDetailDto;
import com.example.jobsyserver.dto.project.ProjectDto;
import com.example.jobsyserver.enums.ProjectApplicationStatus;
import com.example.jobsyserver.enums.ProjectStatus;
import com.example.jobsyserver.service.DashboardService;
import com.example.jobsyserver.service.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Панель CLIENT и FREELANCER")
public class DashboardController {

    private final DashboardService dash;
    private final SecurityService security;

    @Operation(summary = "Мои проекты (CLIENT)", description = "Открытые / в работе / архив")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список проектов успешно получен"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет роли CLIENT"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/client/projects")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<ProjectDto>> clientProjects(
            @RequestParam(required = false) ProjectStatus status) {
        Long clientId = security.getCurrentClientProfileId();
        return ResponseEntity.ok(dash.getClientProjects(clientId, status));
    }

    @Operation(summary = "Детали проекта (CLIENT)", description = "Описание + отклики + приглашения")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Детали проекта успешно получены"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет роли CLIENT"),
            @ApiResponse(responseCode = "404", description = "Проект не найден или не принадлежит клиенту"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/client/projects/{projectId}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ProjectDetailDto> clientProjectDetail(
            @PathVariable Long projectId) {
        Long clientId = security.getCurrentClientProfileId();
        return ResponseEntity.ok(
                dash.getClientProjectDetail(clientId, projectId));
    }

    @Operation(summary = "Мои проекты (FREELANCER)", description = "В работе / архив")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список проектов успешно получен"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет роли FREELANCER"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/freelancer/projects")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<List<ProjectDto>> freelancerProjects(
            @RequestParam(required = false) ProjectStatus status) {
        Long frId = security.getCurrentFreelancerProfileId();
        return ResponseEntity.ok(dash.getFreelancerProjects(frId, status));
    }

    @Operation(summary = "Мои отклики (FREELANCER)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список откликов успешно получен"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет роли FREELANCER"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/freelancer/responses")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<List<ProjectApplicationDto>> myResponses(
            @RequestParam(required = false) ProjectApplicationStatus status) {
        Long frId = security.getCurrentFreelancerProfileId();
        return ResponseEntity.ok(dash.getMyResponses(frId, status));
    }

    @Operation(summary = "Мои приглашения (FREELANCER)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список приглашений успешно получен"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет роли FREELANCER"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/freelancer/invitations")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<List<ProjectApplicationDto>> myInvitations(
            @RequestParam(required = false) ProjectApplicationStatus status) {
        Long frId = security.getCurrentFreelancerProfileId();
        return ResponseEntity.ok(dash.getMyInvitations(frId, status));
    }
}