package com.example.jobsyserver.controller;

import com.example.jobsyserver.dto.freelancer.FreelancerProfileDto;
import com.example.jobsyserver.service.FreelancerProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Freelancer Skills", description = "Операции управления навыками фрилансера")
@RestController
@RequestMapping("/profile/freelancer/skills")
@RequiredArgsConstructor
public class FreelancerSkillsController {

    private final FreelancerProfileService freelancerProfileService;

    @Operation(summary = "Добавить навык", description = "Добавляет указанный навык к профилю фрилансера"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Навык успешно добавлен"),
            @ApiResponse(responseCode = "404", description = "Навык или профиль фрилансера не найдены"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/{skillId}")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<FreelancerProfileDto> addSkill(@PathVariable Long skillId) {
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