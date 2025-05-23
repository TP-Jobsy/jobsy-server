package com.example.jobsyserver.features.freelancer.controller;

import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileDto;
import com.example.jobsyserver.features.freelancer.projection.FreelancerListItem;
import com.example.jobsyserver.features.freelancer.service.FreelancerProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/freelancers")
@Tag(name = "freelancer", description = "Операции для работы с фрилансерами")
public class FreelancerController {

    private final FreelancerProfileService freelancerProfileService;

    @Operation(summary = "Просмотр списка фрилансеров", description = "Возвращает список фрилансеров с базовой информацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список фрилансеров получен успешно"),
            @ApiResponse(responseCode = "404", description = "Фрилансеры не найдены"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping
    public ResponseEntity<Page<FreelancerListItem>> getAllFreelancers(
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<FreelancerListItem> page = freelancerProfileService.listFreelancers(pageable);
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "Получить детальную информацию о фрилансере", description = "Возвращает детальную информацию о фрилансере по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о фрилансере получена успешно"),
            @ApiResponse(responseCode = "404", description = "Фрилансер не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FreelancerProfileDto> getFreelancerById(@PathVariable Long id) {
        FreelancerProfileDto profile = freelancerProfileService.getFreelancerProfileById(id);
        if (profile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(profile);
    }
}