package com.example.jobsyserver.controller;

import com.example.jobsyserver.dto.project.ProjectApplicationDto;
import com.example.jobsyserver.dto.project.ProjectDetailDto;
import com.example.jobsyserver.dto.project.ProjectDto;
import com.example.jobsyserver.enums.ProjectApplicationStatus;
import com.example.jobsyserver.enums.ProjectStatus;
import com.example.jobsyserver.service.DashboardService;
import com.example.jobsyserver.service.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
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
    @GetMapping("/client/projects")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<ProjectDto>> clientProjects(
            @RequestParam(required = false) ProjectStatus status) {
        Long clientId = security.getCurrentClientProfileId();
        return ResponseEntity.ok(dash.getClientProjects(clientId, status));
    }

    @Operation(summary = "Детали проекта (CLIENT)", description = "Описание + отклики + приглашения")
    @GetMapping("/client/projects/{projectId}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ProjectDetailDto> clientProjectDetail(
            @PathVariable Long projectId) {
        Long clientId = security.getCurrentClientProfileId();
        return ResponseEntity.ok(
                dash.getClientProjectDetail(clientId, projectId));
    }

    @Operation(summary = "Мои проекты (FREELANCER)", description = "В работе / архив")
    @GetMapping("/freelancer/projects")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<List<ProjectDto>> freelancerProjects(
            @RequestParam(required = false) ProjectStatus status) {
        Long frId = security.getCurrentFreelancerProfileId();
        return ResponseEntity.ok(dash.getFreelancerProjects(frId, status));
    }

    @Operation(summary = "Мои отклики (FREELANCER)")
    @GetMapping("/freelancer/responses")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<List<ProjectApplicationDto>> myResponses(
            @RequestParam(required = false) ProjectApplicationStatus status) {
        Long frId = security.getCurrentFreelancerProfileId();
        return ResponseEntity.ok(dash.getMyResponses(frId, status));
    }

    @Operation(summary = "Мои приглашения (FREELANCER)")
    @GetMapping("/freelancer/invitations")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<List<ProjectApplicationDto>> myInvitations(
            @RequestParam(required = false) ProjectApplicationStatus status) {
        Long frId = security.getCurrentFreelancerProfileId();
        return ResponseEntity.ok(dash.getMyInvitations(frId, status));
    }
}