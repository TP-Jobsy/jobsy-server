package com.example.jobsyserver.features.client.controller;

import com.example.jobsyserver.features.client.dto.ClientProfileDto;
import com.example.jobsyserver.features.client.service.ClientProfileService;
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
@RequestMapping("/clients")
@RequiredArgsConstructor
@Tag(name = "client", description = "Операции для работы с профилями заказчиков")
public class ClientController {

    private final ClientProfileService clientProfileService;

    @Operation(summary = "Просмотр списка заказчиков", description = "Возвращает список заказчиков с базовой информацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список заказчиков получен успешно"),
            @ApiResponse(responseCode = "404", description = "Заказчики не найдены"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping
    public ResponseEntity<List<ClientProfileDto>> getAllClients() {
        List<ClientProfileDto> clients = clientProfileService.getAllClients();
        if (clients.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(clients);
    }

    @Operation(summary = "Получить детальную информацию о заказчике", description = "Возвращает детальную информацию о заказчике по его ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о заказчике получена успешно"),
            @ApiResponse(responseCode = "404", description = "Заказчик не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientProfileDto> getClientById(@PathVariable Long id) {
        ClientProfileDto dto = clientProfileService.getClientProfileById(id);
        return ResponseEntity.ok(dto);
    }
}
