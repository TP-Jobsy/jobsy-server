package com.example.jobsyserver.features.admin.controller;

import com.example.jobsyserver.features.admin.service.AdminService;
import com.example.jobsyserver.features.common.dto.response.DefaultResponse;
import com.example.jobsyserver.features.project.dto.ProjectDto;
import com.example.jobsyserver.features.portfolio.dto.FreelancerPortfolioDto;
import com.example.jobsyserver.features.user.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin", description = "Операции управления для админ-панели")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "Получить список фрилансеров", description = "Возвращает список всех пользователей с ролью FREELANCER")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Профиль получен успешно"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет прав"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/freelancers")
    public ResponseEntity<List<UserDto>> getAllFreelancers() {
        return ResponseEntity.ok(adminService.getAllFreelancers());
    }

    @Operation(summary = "Получить профиль фрилансеров", description = "Возвращает пользователя по id с ролью FREELANCER")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Профиль получен успешно"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет прав"),
            @ApiResponse(responseCode = "404", description = "Профиль не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/freelancers/{id}")
    public ResponseEntity<UserDto> getFreelancerById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getFreelancerById(id));
    }

    @Operation(summary = "Деактивировать фрилансера", description = "Деактивирует пользователя по id с ролью FREELANCER")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Фрилансер успешно деактивирован"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет прав"),
            @ApiResponse(responseCode = "404", description = "Профиль не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/freelancers/{id}")
    public ResponseEntity<?> deactivateFreelancer(@PathVariable Long id) {
        adminService.deactivateFreelancer(id);
        return ResponseEntity.ok(new DefaultResponse("Фрилансер успешно деактивирован"));
    }

    @Operation(summary = "Получить список заказчиков", description = "Возвращает список всех пользователей с ролью CLIENT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Профиль получен успешно"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет прав"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/clients")
    public ResponseEntity<List<UserDto>> getAllClients() {
        return ResponseEntity.ok(adminService.getAllClients());
    }

    @Operation(summary = "Получить профиль заказчика", description = "Возвращает пользователя по id с ролью CLIENT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Профиль получен успешно"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет прав"),
            @ApiResponse(responseCode = "404", description = "Профиль не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/clients/{id}")
    public ResponseEntity<UserDto> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getClientById(id));
    }

    @Operation(summary = "Деактивировать заказчика", description = "Деактивирует пользователя по id с ролью CLIENT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Фрилансер успешно деактивирован"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет прав"),
            @ApiResponse(responseCode = "404", description = "Профиль не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/clients/{id}")
    public ResponseEntity<?> deactivateClient(@PathVariable Long id) {
        adminService.deactivateClient(id);
        return ResponseEntity.ok(new DefaultResponse("Клиент успешно деактивирован"));
    }

    @Operation(summary = "Получить список проектов пользователя", description = "Возвращает список проектов по id заказчика")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Профиль получен успешно"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет прав"),
            @ApiResponse(responseCode = "404", description = "Профиль не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/clients/{id}/projects")
    public ResponseEntity<List<ProjectDto>> getClientProjects(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getClientProjects(id));
    }

    @Operation(summary = "Получить проект по id", description = "Возвращает проект по id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Проект получен успешно"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет прав"),
            @ApiResponse(responseCode = "404", description = "Проект не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/projects/{id}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getProjectById(id));
    }

    @Operation(summary = "Удалить проект", description = "Удалить проект по projectId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Проект удалён успешно"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет прав"),
            @ApiResponse(responseCode = "404", description = "Проект не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/projects/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        adminService.deleteProject(id);
        return ResponseEntity.ok(new DefaultResponse("Проект успешно удален"));
    }

    @Operation(summary = "Получить портфолио", description = "Возвращает портфолио по freelancerId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Портфолио получено успешно"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет прав"),
            @ApiResponse(responseCode = "404", description = "Фрилансер не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/freelancers/{id}/portfolio")
    public ResponseEntity<List<FreelancerPortfolioDto>> getFreelancerPortfolio(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getFreelancerPortfolio(id));
    }

    @Operation(summary = "Удалить портфолио", description = "Удаляет портфолио по portfolioId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Портфолио удалено успешно"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет прав"),
            @ApiResponse(responseCode = "404", description = "Портфолио не найдено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/freelancers/{id}/portfolio/{portfolioId}")
    public ResponseEntity<?> deletePortfolio(
            @PathVariable Long id,
            @PathVariable Long portfolioId) {
        adminService.deletePortfolio(id, portfolioId);
        return ResponseEntity.ok(new DefaultResponse("Портфолио успешно удалено"));
    }
}