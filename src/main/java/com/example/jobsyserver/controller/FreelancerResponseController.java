package com.example.jobsyserver.controller;

import com.example.jobsyserver.dto.project.ProjectApplicationDto;
import com.example.jobsyserver.enums.ProjectApplicationStatus;
import com.example.jobsyserver.service.FreelancerResponseService;
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
@RequestMapping("/projects/{projectId}/responses")
@RequiredArgsConstructor
@Tag(name = "Freelancer Responses", description = "Операции отклика фрилансера на проект")
public class FreelancerResponseController {

    private final FreelancerResponseService service;

    @Operation(summary = "Откликнуться на проект", description = "Фрилансер оставляет отклик на указанный проект")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Отклик создан успешно"),
            @ApiResponse(responseCode = "400", description = "Неверные данные"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет роли FREELANCER"),
            @ApiResponse(responseCode = "404", description = "Проект или фрилансер не найдены"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PreAuthorize("hasRole('FREELANCER')")
    @PostMapping
    public ResponseEntity<ProjectApplicationDto> respond(
            @PathVariable Long projectId,
            @RequestParam Long freelancerId
    ) {
        ProjectApplicationDto dto = service.respond(projectId, freelancerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Обработать статус отклика", description = "Клиент подтверждает или отклоняет отклик фрилансера")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Статус отклика обновлён"),
            @ApiResponse(responseCode = "400", description = "Неверные данные или попытка обработать не‑отклик"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет роли CLIENT"),
            @ApiResponse(responseCode = "404", description = "Отклик не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PreAuthorize("hasRole('CLIENT')")
    @PatchMapping("/{applicationId}/status")
    public ResponseEntity<ProjectApplicationDto> handleResponseStatus(
            @PathVariable Long projectId,
            @PathVariable Long applicationId,
            @RequestParam ProjectApplicationStatus status
    ) {
        ProjectApplicationDto dto = service.handleResponseStatus(applicationId, status);
        return ResponseEntity.ok(dto);
    }
}