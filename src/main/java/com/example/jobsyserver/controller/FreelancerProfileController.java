package com.example.jobsyserver.controller;

import com.example.jobsyserver.dto.freelancer.FreelancerProfileDto;
import com.example.jobsyserver.dto.freelancer.FreelancerProfileUpdateDto;
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
@Tag(name = "Freelancer Profile", description = "Операции управления профилем фрилансера")
public class FreelancerProfileController {

    private final FreelancerProfileService freelancerProfileService;

    @Operation(
            summary = "Получить профиль фрилансера",
            description = "Возвращает данные профиля фрилансера. Доступно только для пользователей с ролью фрилансер"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Профиль фрилансера получен успешно"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет прав"),
            @ApiResponse(responseCode = "404", description = "Профиль не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<FreelancerProfileDto> getProfile() {
        FreelancerProfileDto profile = freelancerProfileService.getProfile();
        return ResponseEntity.ok(profile);
    }

    @Operation(
            summary = "Обновить профиль фрилансера",
            description = "Позволяет обновить профиль фрилансера. Обновляются данные профиля и общие данные пользователя, если они были переданы. Доступно только для пользователей с ролью фрилансер"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Профиль фрилансера обновлён успешно"),
            @ApiResponse(responseCode = "400", description = "Неверные данные для обновления профиля"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет прав"),
            @ApiResponse(responseCode = "404", description = "Профиль не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PutMapping
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<FreelancerProfileDto> updateProfile(@RequestBody FreelancerProfileUpdateDto updateDto) {
        FreelancerProfileDto updatedProfile = freelancerProfileService.updateProfile(updateDto);
        return ResponseEntity.ok(updatedProfile);
    }

    @Operation(
            summary = "Удалить аккаунт фрилансера",
            description = "Удаляет аккаунт фрилансера, делая его неактивным. После этого пользователь не сможет войти в систему. Доступно только для пользователей с ролью фрилансер"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Аккаунт фрилансера успешно деактивирован"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет прав"),
            @ApiResponse(responseCode = "404", description = "Профиль не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<DefaultResponse> deleteAccount() {
        freelancerProfileService.deleteAccount();
        return ResponseEntity.ok(new DefaultResponse("Аккаунт успешно удален"));
    }
}