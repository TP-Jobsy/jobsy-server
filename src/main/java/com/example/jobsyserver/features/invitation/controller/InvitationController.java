package com.example.jobsyserver.features.invitation.controller;

import com.example.jobsyserver.features.project.dto.ProjectApplicationDto;
import com.example.jobsyserver.features.common.enums.ProjectApplicationStatus;
import com.example.jobsyserver.features.invitation.service.InvitationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects/{projectId}/invitations")
@RequiredArgsConstructor
@Tag(name = "Client Invitations", description = "Операции приглашения фрилансера на проект")
public class InvitationController {

    private final InvitationService service;

    @Operation(summary = "Пригласить фрилансера", description = "Клиент отправляет приглашение фрилансеру на участие в проекте")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Приглашение отправлено"),
            @ApiResponse(responseCode = "400", description = "Неверные данные"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет роли CLIENT"),
            @ApiResponse(responseCode = "404", description = "Проект или фрилансер не найдены"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping
    public ResponseEntity<ProjectApplicationDto> invite(
            @PathVariable Long projectId,
            @RequestParam Long freelancerId
    ) {
        ProjectApplicationDto dto = service.invite(projectId, freelancerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Обработать статус приглашения", description = "Фрилансер принимает или отклоняет приглашение")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Статус приглашения обновлён"),
            @ApiResponse(responseCode = "400", description = "Неверные данные или попытка обработать не‑приглашение"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет роли FREELANCER"),
            @ApiResponse(responseCode = "404", description = "Приглашение не найдено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PreAuthorize("hasRole('FREELANCER')")
    @PatchMapping("/{applicationId}/status")
    public ResponseEntity<ProjectApplicationDto> handleInvitationStatus(
            @PathVariable Long projectId,
            @PathVariable Long applicationId,
            @RequestParam ProjectApplicationStatus status
    ) {
        ProjectApplicationDto dto = service.handleInvitationStatus(applicationId, status);
        return ResponseEntity.ok(dto);
    }
}