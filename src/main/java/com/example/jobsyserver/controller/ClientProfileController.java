package com.example.jobsyserver.controller;

import com.example.jobsyserver.dto.client.ClientProfileBasicDto;
import com.example.jobsyserver.dto.client.ClientProfileContactDto;
import com.example.jobsyserver.dto.client.ClientProfileFieldDto;
import com.example.jobsyserver.dto.client.ClientProfileDto;
import com.example.jobsyserver.dto.response.DefaultResponse;
import com.example.jobsyserver.service.ClientProfileService;
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

@RestController
@RequestMapping("/profile/client")
@RequiredArgsConstructor
@Validated
@Tag(name = "client", description = "Операции управления профилем заказчика")
public class ClientProfileController {

    private final ClientProfileService clientProfileService;

    @Operation(summary = "Получить профиль заказчика", description = "Возвращает данные профиля заказчика текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Профиль получен успешно"),
            @ApiResponse(responseCode = "404", description = "Профиль не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ClientProfileDto> getProfile() {
        ClientProfileDto dto = clientProfileService.getProfile();
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Обновить основные данные профиля заказчика", description = "Обновляет основные данные")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Основные данные обновлены успешно"),
            @ApiResponse(responseCode = "400", description = "Ошибка обновления основных данных"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PutMapping("/basic")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ClientProfileDto> updateBasic(@Valid @RequestBody ClientProfileBasicDto basicDto) {
        ClientProfileDto dto = clientProfileService.updateBasic(basicDto);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Обновить контактные данные профиля заказчика", description = "Обновляет контактные данные")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Контактные данные обновлены успешно"),
            @ApiResponse(responseCode = "400", description = "Ошибка обновления контактных данных"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PutMapping("/contact")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ClientProfileDto> updateContact(@RequestBody ClientProfileContactDto contactDto) {
        ClientProfileDto dto = clientProfileService.updateContact(contactDto);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Обновить данные о сфере деятельности", description = "Обновляет информацию о сфере деятельности")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные о сфере деятельности обновлены успешно"),
            @ApiResponse(responseCode = "400", description = "Ошибка обновления данных о сфере деятельности"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")

    })
    @PutMapping("/field")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ClientProfileDto> updateField(@RequestBody ClientProfileFieldDto fieldDto) {
        ClientProfileDto dto = clientProfileService.updateField(fieldDto);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Удалить аккаунт заказчика", description = "Удаляет аккаунт заказчика текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Аккаунт успешно удален"),
            @ApiResponse(responseCode = "404", description = "Профиль не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<DefaultResponse> deleteAccount() {
        clientProfileService.deleteAccount();
        return ResponseEntity.ok(new DefaultResponse("Аккаунт успешно удален"));
    }
}
