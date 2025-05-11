package com.example.jobsyserver.features.portfolio.controller;

import com.example.jobsyserver.features.skill.dto.SkillDto;
import com.example.jobsyserver.features.portfolio.service.PortfolioSkillService;
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
@RequestMapping("/profile/freelancer/portfolio/{portfolioId}/skills")
@RequiredArgsConstructor
@Tag(name = "freelancer", description = "Управление навыками портфолио")
@PreAuthorize("hasRole('FREELANCER')")
public class PortfolioSkillController {

    private final PortfolioSkillService service;

    @Operation(summary = "Получить навыки элемента портфолио")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список навыков получен"),
            @ApiResponse(responseCode = "404", description = "Элемент портфолио не найден или не принадлежит пользователю"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping
    public ResponseEntity<List<SkillDto>> getSkills(
            @PathVariable Long portfolioId
    ) {
        return ResponseEntity.ok(service.getSkillsForPortfolio(portfolioId));
    }

    @Operation(summary = "Добавить навык к портфолио")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Навык добавлен в портфолио"),
            @ApiResponse(responseCode = "404", description = "Портфолио или навык не найдены"),
            @ApiResponse(responseCode = "403", description = "Нет прав на изменение чужого портфолио"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/{skillId}")
    public ResponseEntity<Void> addSkill(
            @PathVariable Long portfolioId,
            @PathVariable Long skillId
    ) {
        service.addSkillToPortfolio(portfolioId, skillId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Удалить навык из портфолио")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Навык удалён из портфолио"),
            @ApiResponse(responseCode = "404", description = "Портфолио или навык не найдены"),
            @ApiResponse(responseCode = "403", description = "Нет прав на изменение чужого портфолио"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/{skillId}")
    public ResponseEntity<Void> removeSkill(
            @PathVariable Long portfolioId,
            @PathVariable Long skillId
    ) {
        service.removeSkillFromPortfolio(portfolioId, skillId);
        return ResponseEntity.noContent().build();
    }
}