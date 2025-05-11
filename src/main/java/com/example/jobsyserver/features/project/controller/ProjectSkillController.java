package com.example.jobsyserver.features.project.controller;

import com.example.jobsyserver.features.skill.dto.SkillDto;
import com.example.jobsyserver.features.project.service.ProjectSkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects/{projectId}/skills")
@RequiredArgsConstructor
@Tag(name = "project", description = "Управление навыками проектов")
@PreAuthorize("hasRole('CLIENT')")
public class ProjectSkillController {

    private final ProjectSkillService service;

    @Operation(summary = "Получить навыки проекта")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список навыков получен"),
            @ApiResponse(responseCode = "404", description = "Проект не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping
    public ResponseEntity<List<SkillDto>> getSkills(
            @PathVariable Long projectId
    ) {
        List<SkillDto> skills = service.getSkillsByProject(projectId);
        return ResponseEntity.ok(skills);
    }

    @Operation(summary = "Добавить навык к проекту")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Навык успешно добавлен"),
            @ApiResponse(responseCode = "400", description = "Нельзя добавить более 5 навыков или навык уже добавлен"),
            @ApiResponse(responseCode = "404", description = "Проект или навык не найдены"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/{skillId}")
    public ResponseEntity<Void> addSkill(
            @PathVariable Long projectId,
            @PathVariable Long skillId
    ) {
        service.addSkill(projectId, skillId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Удалить навык из проекта")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Навык успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Проект или навык не найдены в проекте"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/{skillId}")
    public ResponseEntity<Void> removeSkill(
            @PathVariable Long projectId,
            @PathVariable Long skillId
    ) {
        service.removeSkill(projectId, skillId);
        return ResponseEntity.noContent().build();
    }
}