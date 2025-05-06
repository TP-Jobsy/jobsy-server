package com.example.jobsyserver.controller;

import com.example.jobsyserver.dto.common.SpecializationDto;
import com.example.jobsyserver.service.SpecializationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Specializations", description = "Операции для работы со специализациями")
public class SpecializationController {

    private final SpecializationService specializationService;

    @Operation(summary = "Получить список специализаций", description = "Возвращает список всех специализаций, доступных в системе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список специализаций успешно получен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/specializations")
    public ResponseEntity<List<SpecializationDto>> getAllSpecializations() {
        List<SpecializationDto> specializations = specializationService.getAllSpecializations();
        return ResponseEntity.ok(specializations);
    }

    @Operation(summary = "Получить специализации по категории", description = "Возвращает список специализаций, относящихся к заданной категории по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список специализаций успешно получен"),
            @ApiResponse(responseCode = "404", description = "Специализации для данной категории не найдены"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/categories/{categoryId}/specializations")
    public ResponseEntity<List<SpecializationDto>> getSpecializationsByCategory(@PathVariable Long categoryId) {
        List<SpecializationDto> specializations = specializationService.getAllByCategoryId(categoryId);
        return ResponseEntity.ok(specializations);
    }

    @Operation(summary = "Получить специализацию по id", description = "Возвращает специализацию, найденную по уникальному идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Специализация успешно получена"),
            @ApiResponse(responseCode = "404", description = "Специализация с указанным id не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/specializations/{id}")
    public ResponseEntity<SpecializationDto> getSpecializationById(@PathVariable Long id) {
        SpecializationDto specialization = specializationService.getSpecializationById(id);
        return ResponseEntity.ok(specialization);
    }

    @Operation(summary = "Создать новую специализацию", description = "Создаёт новую специализацию на основании предоставленных данных")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Специализация успешно создана"),
            @ApiResponse(responseCode = "400", description = "Неверные данные для создания специализации"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/specializations")
    public ResponseEntity<SpecializationDto> createSpecialization(@RequestBody SpecializationDto specializationDto) {
        SpecializationDto createdSpecialization = specializationService.createSpecialization(specializationDto);
        return new ResponseEntity<>(createdSpecialization, HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить специализацию", description = "Обновляет данные специализации, найденной по указанному id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Специализация успешно обновлена"),
            @ApiResponse(responseCode = "400", description = "Неверные данные для обновления специализации"),
            @ApiResponse(responseCode = "404", description = "Специализация с указанным id не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PutMapping("/specializations/{id}")
    public ResponseEntity<SpecializationDto> updateSpecialization(@PathVariable Long id,
                                                                  @RequestBody SpecializationDto specializationDto) {
        SpecializationDto updatedSpecialization = specializationService.updateSpecialization(id, specializationDto);
        return ResponseEntity.ok(updatedSpecialization);
    }

    @Operation(summary = "Удалить специализацию", description = "Удаляет специализацию, найденную по уникальному идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Специализация успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Специализация с указанным id не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/specializations/{id}")
    public ResponseEntity<Void> deleteSpecialization(@PathVariable Long id) {
        specializationService.deleteSpecializationById(id);
        return ResponseEntity.noContent().build();
    }
}