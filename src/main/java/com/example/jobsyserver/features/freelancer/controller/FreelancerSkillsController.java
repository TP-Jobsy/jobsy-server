package com.example.jobsyserver.features.freelancer.controller;

import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileDto;
import com.example.jobsyserver.features.freelancer.service.FreelancerProfileService;
import com.example.jobsyserver.features.skill.dto.SkillDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Freelancer Skills", description = "Операции управления навыками фрилансера")
@RestController
@RequestMapping("/profile/freelancer/skills")
@RequiredArgsConstructor
@Validated
public class FreelancerSkillsController {

    private final FreelancerProfileService freelancerProfileService;

    @Operation(summary = "Просмотр навыков", description = "Возвращает все навыки текущего фрилансера")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список навыков успешно получен"),
            @ApiResponse(responseCode = "401", description = "Не аутентифицирован"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping
    public ResponseEntity<List<SkillDto>> getMySkills() {
        return ResponseEntity.ok(freelancerProfileService.getSkills());
    }

    @Operation(summary = "Добавить навык", description = "Добавляет указанный навык к профилю фрилансера"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Навык успешно добавлен"),
            @ApiResponse(responseCode = "404", description = "Навык или профиль фрилансера не найдены"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/{skillId}")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<FreelancerProfileDto> addSkill(@Valid @PathVariable Long skillId) {
        FreelancerProfileDto dto = freelancerProfileService.addSkill(skillId);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Удалить навык", description = "Удаляет указанный навык из профиля фрилансера"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Навык успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Навык не найден в профиле фрилансера"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/{skillId}")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<FreelancerProfileDto> removeSkill(@PathVariable Long skillId) {
        FreelancerProfileDto dto = freelancerProfileService.removeSkill(skillId);
        return ResponseEntity.ok(dto);
    }
}