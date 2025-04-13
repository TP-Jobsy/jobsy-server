package com.example.jobsyserver.controller;

import com.example.jobsyserver.dto.freelancer.*;
import com.example.jobsyserver.dto.response.DefaultResponse;
import com.example.jobsyserver.service.FreelancerProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile/freelancer")
@Tag(name = "Freelancer Profile", description = "Операции по управлению профилем фрилансера")
public class FreelancerProfileController {

    private final FreelancerProfileService freelancerProfileService;

    @Operation(summary = "Получить профиль фрилансера", description = "Возвращает полный профиль фрилансера для текущего аутентифицированного пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Профиль получен успешно"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет прав"),
            @ApiResponse(responseCode = "404", description = "Профиль не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<FreelancerProfileDto> getProfile() {
        return ResponseEntity.ok(freelancerProfileService.getProfile());
    }

    @Operation(summary = "Обновить базовую информацию профиля", description = "Обновляет такие поля, как страна, город и т.п")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Базовая информация обновлена успешно"),
            @ApiResponse(responseCode = "400", description = "Неверные данные"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PutMapping("/basic")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<FreelancerProfileDto> updateBasic(@RequestBody FreelancerProfileBasicDto basicDto) {
        return ResponseEntity.ok(freelancerProfileService.updateBasic(basicDto));
    }

    @Operation(summary = "Обновить контактные данные профиля", description = "Обновление контактной информации, например, ссылки для связи")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Контактные данные обновлены успешно"),
            @ApiResponse(responseCode = "400", description = "Неверные данные"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PutMapping("/contact")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<FreelancerProfileDto> updateContact(@RequestBody FreelancerProfileContactDto contactDto) {
        return ResponseEntity.ok(freelancerProfileService.updateContact(contactDto));
    }

    @Operation(summary = "Обновить информацию 'о себе'", description = "Обновляет информацию о категории, специализации, опыте и описании")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Информация 'о себе' обновлена успешно"),
            @ApiResponse(responseCode = "400", description = "Неверные данные"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PutMapping("/about")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<FreelancerProfileDto> updateAbout(@RequestBody FreelancerProfileAboutDto aboutDto) {
        return ResponseEntity.ok(freelancerProfileService.updateAbout(aboutDto));
    }

    @Operation(summary = "Удалить аккаунт фрилансера", description = "Удаляет аккаунт фрилансера. После удаления вход в систему невозможен")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Аккаунт удалён успешно"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<?> deleteAccount() {
        freelancerProfileService.deleteAccount();
        return ResponseEntity.ok(new DefaultResponse("Аккаунт успешно удален"));
    }

    @Operation(summary = "Получить профиль фрилансера по ID", description = "Возвращает публичный профиль фрилансера по заданному идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Профиль найден"),
            @ApiResponse(responseCode = "404", description = "Профиль не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FreelancerProfileDto> getFreelancerProfileById(@PathVariable Long id) {
        return ResponseEntity.ok(freelancerProfileService.getFreelancerProfileById(id));
    }
}