package com.example.jobsyserver.controller;

import com.example.jobsyserver.dto.project.ProjectCreateDto;
import com.example.jobsyserver.dto.project.ProjectDto;
import com.example.jobsyserver.dto.project.ProjectUpdateDto;
import com.example.jobsyserver.enums.ProjectStatus;
import com.example.jobsyserver.service.impl.ProjectServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectServiceImpl projectServiceImpl;

    @Operation(summary = "Получить список всех проектов", description = "Возвращает список всех проектов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список проектов получен успешно"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAllProjects(
            @RequestParam(required = false) ProjectStatus status) {
        return ResponseEntity.ok(projectServiceImpl.getAllProjects(status));
    }

    @Operation(summary = "Создать новый проект", description = "Создаёт новый проект")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Проект создан успешно"),
            @ApiResponse(responseCode = "400", description = "Проект не создан, получены неверные данные"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping
    public ResponseEntity<ProjectDto> createProject(
            @Valid @RequestBody ProjectCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectServiceImpl.createProject(dto));
    }

    @Operation(summary = "Обновить проект", description = "Обновляет данные проекта с указанным id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Проект обновлён успешно"),
            @ApiResponse(responseCode = "400", description = "Проект не обновлён, получены неверные данные"),
            @ApiResponse(responseCode = "404", description = "Проект с указанным id не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectUpdateDto dto) {
        return ResponseEntity.ok(projectServiceImpl.updateProject(id, dto));
    }

    @Operation(summary = "Удалить проект", description = "Удаляет проект с указанным id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Проект удалён успешно"),
            @ApiResponse(responseCode = "404", description = "Проект с указанным id не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectServiceImpl.deleteProject(id);
        return ResponseEntity.ok().build();
    }
}
