package com.example.jobsyserver.features.project.controller;

import com.example.jobsyserver.features.skill.dto.SkillDto;
import com.example.jobsyserver.features.project.service.ProjectSkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects/{projectId}/skills")
@RequiredArgsConstructor
public class ProjectSkillController {

    private final ProjectSkillService projectSkillService;

    @Operation(summary = "Получить список навыков проекта", description = "Возвращает все навыки указанного проекта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список навыков успешно получен"),
            @ApiResponse(responseCode = "404", description = "Проект не найден"),
            @ApiResponse(responseCode = "401", description = "Не аутентифицирован"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping
    public ResponseEntity<List<SkillDto>> getSkills(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectSkillService.getSkills(projectId));
    }

    @Operation(summary = "Добавить навык к проекту", description = "Добавляет указанный навык к проекту")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Навык успешно добавлен к проекту"),
            @ApiResponse(responseCode = "404", description = "Проект или навык не найдены"),
            @ApiResponse(responseCode = "403", description = "Нет прав"),
            @ApiResponse(responseCode = "401", description = "Не аутентифицирован"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/{skillId}")
    public ResponseEntity<Void> addSkill(
            @PathVariable Long projectId,
            @PathVariable Long skillId
    ) {
        projectSkillService.addSkill(projectId, skillId);
        return ResponseEntity.status(201).build();
    }

    @Operation(summary = "Удалить навык из проекта", description = "Удаляет указанный навык из проекта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Навык успешно удалён из проекта"),
            @ApiResponse(responseCode = "404", description = "Проект или навык не найдены"),
            @ApiResponse(responseCode = "403", description = "Нет прав"),
            @ApiResponse(responseCode = "401", description = "Не аутентифицирован"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PreAuthorize("hasRole('CLIENT')")
    @DeleteMapping("/{skillId}")
    public ResponseEntity<Void> removeSkill(
            @PathVariable Long projectId,
            @PathVariable Long skillId
    ) {
        projectSkillService.removeSkill(projectId, skillId);
        return ResponseEntity.noContent().build();
    }
}