package com.example.jobsyserver.controller;

import com.example.jobsyserver.dto.portfolio.FreelancerPortfolioCreateDto;
import com.example.jobsyserver.dto.portfolio.FreelancerPortfolioDto;
import com.example.jobsyserver.dto.portfolio.FreelancerPortfolioUpdateDto;
import com.example.jobsyserver.service.FreelancerPortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile/freelancer/portfolio")
@RequiredArgsConstructor
@Validated
@Tag(name = "freelancer", description = "Панель фрилансера: портфолио")
@PreAuthorize("hasRole('FREELANCER')")
public class FreelancerPortfolioController {

    private final FreelancerPortfolioService service;

    @Operation(summary = "Получить портфолио фрилансера")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список элементов портфолио успешно получен"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет роли FREELANCER"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping
    public ResponseEntity<List<FreelancerPortfolioDto>> getPortfolio() {
        return ResponseEntity.ok(service.getMyPortfolio());
    }

    @Operation(summary = "Добавить новый элемент портфолио")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Элемент портфолио успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверные данные в запросе"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет роли FREELANCER"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping
    public ResponseEntity<FreelancerPortfolioDto> create(
            @Valid @RequestBody FreelancerPortfolioCreateDto dto
    ) {
        FreelancerPortfolioDto created = service.createPortfolio(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Обновить элемент портфолио")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Элемент портфолио успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Неверные данные в запросе"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет роли FREELANCER"),
            @ApiResponse(responseCode = "404", description = "Элемент портфолио не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PutMapping("/{id}")
    public ResponseEntity<FreelancerPortfolioDto> update(
            @PathVariable Long id,
            @Valid @RequestBody FreelancerPortfolioUpdateDto dto
    ) {
        return ResponseEntity.ok(service.updatePortfolio(id, dto));
    }

    @Operation(summary = "Удалить элемент портфолио")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Элемент портфолио успешно удалён"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет роли FREELANCER"),
            @ApiResponse(responseCode = "404", description = "Элемент портфолио не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deletePortfolio(id);
        return ResponseEntity.noContent().build();
    }
}