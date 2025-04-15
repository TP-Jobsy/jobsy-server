package com.example.jobsyserver.controller;

import com.example.jobsyserver.dto.project.ProjectApplicationDto;
import com.example.jobsyserver.dto.project.ProjectApplicationRequestDto;
import com.example.jobsyserver.enums.ProjectApplicationStatus;
import com.example.jobsyserver.service.impl.ProjectApplicationServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/project-applications")
@RequiredArgsConstructor
public class ProjectApplicationController {

    private final ProjectApplicationServiceImpl service;

    @Operation(summary = "Получить данные заявки", description = "Возвращает заявку с указанным id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Заявка получена успешно"),
            @ApiResponse(responseCode = "404", description = "Заявки с указанным id не найдено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProjectApplicationDto> getById(@PathVariable Long id){
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Создать новую заявку", description = "Создаёт новую заявку на работу над проектом")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Заявка создана успешно"),
            @ApiResponse(responseCode = "400", description = "Заявка не создана, получены неверные данные"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping
    public ResponseEntity<ProjectApplicationDto> create(@Valid @RequestBody ProjectApplicationRequestDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createApplication(dto));
    }

    @Operation(summary = "Обновить статус заявки", description = "Обновляет статус заявки на работу над проектом с указанным id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус заявки обновлён успешно"),
            @ApiResponse(responseCode = "400", description = "Статус не обновлён, получены неверные данные"),
            @ApiResponse(responseCode = "404", description = "Заявки с указанным id не найдено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<ProjectApplicationDto> updateStatus(@PathVariable Long id, @RequestParam ProjectApplicationStatus status){
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @Operation(summary = "Удалить заявку", description = "Удаляет заявку на работу над проектом с указанным id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Заявка удалена успешно"),
            @ApiResponse(responseCode = "404", description = "Заявки с указанным id не найдено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }
}
