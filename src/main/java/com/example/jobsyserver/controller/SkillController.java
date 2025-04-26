package com.example.jobsyserver.controller;

import com.example.jobsyserver.dto.common.SkillDto;
import com.example.jobsyserver.service.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/skills")
@RequiredArgsConstructor
@Tag(name = "Skills", description = "Операции для работы с навыками")
public class SkillController {

    private final SkillService skillService;

    @Operation(summary = "Получить список всех навыков", description = "Возвращает список всех скиллов, доступных в системе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список навыков получен успешно"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping
    public ResponseEntity<List<SkillDto>> getAllSkills() {
        List<SkillDto> skills = skillService.getAllSkills();
        return ResponseEntity.ok(skills);
    }

    @Operation(summary = "Получить навык по идентификатору", description = "Возвращает конкретный навык по его id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Навык получен успешно"),
            @ApiResponse(responseCode = "404", description = "Навык с указанным id не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SkillDto> getSkillById(@PathVariable Long id) {
        SkillDto skill = skillService.getSkillById(id);
        return ResponseEntity.ok(skill);
    }

    @Operation(summary = "Создать новый навык", description = "Создаёт новый навык на основании переданных данных")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Навык успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверные данные для создания навыка"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping
    public ResponseEntity<SkillDto> createSkill(@RequestBody SkillDto skillDto) {
        SkillDto createdSkill = skillService.createSkill(skillDto);
        return new ResponseEntity<>(createdSkill, HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить навык", description = "Обновляет данные существующего навыка по его id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Навык успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Неверные данные для обновления навыка"),
            @ApiResponse(responseCode = "404", description = "Навык с указанным id не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SkillDto> updateSkill(@PathVariable Long id, @RequestBody SkillDto skillDto) {
        SkillDto updatedSkill = skillService.updateSkill(id, skillDto);
        return ResponseEntity.ok(updatedSkill);
    }

    @Operation(summary = "Удалить навык", description = "Удаляет навык по его id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Навык успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Навык с указанным id не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        skillService.deleteSkillById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Автодополнение навыков по части названия")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список совпавших навыков"),
            @ApiResponse(responseCode = "400", description = "Запрос слишком короткий (min 2 символа)")
    })
    @GetMapping("/autocomplete")
    public ResponseEntity<List<SkillDto>> autocomplete(
            @RequestParam("query")
            @Size(min = 2, message = "Введите минимум 2 символа")
            String query
    ) {
        List<SkillDto> dtos = skillService.autocompleteSkills(query);
        return ResponseEntity.ok(dtos);
    }
}