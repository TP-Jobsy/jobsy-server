package com.example.jobsyserver.features.admin.controller;

import com.example.jobsyserver.features.admin.service.AdminService;
import com.example.jobsyserver.features.client.dto.ClientProfileDto;
import com.example.jobsyserver.features.common.dto.response.DefaultResponse;
import com.example.jobsyserver.features.common.enums.UserRole;
import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileDto;
import com.example.jobsyserver.features.portfolio.projection.PortfolioAdminListItem;
import com.example.jobsyserver.features.project.dto.ProjectDto;
import com.example.jobsyserver.features.portfolio.dto.FreelancerPortfolioDto;
import com.example.jobsyserver.features.project.projection.ProjectAdminListItem;
import com.example.jobsyserver.features.user.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @Operation(summary = "Получить список фрилансеров", description = "Возвращает список всех профилей пользователей с ролью FREELANCER")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Профиль получен успешно"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет прав"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/freelancers")
    public ResponseEntity<List<FreelancerProfileDto>> getAllFreelancers() {
        return ResponseEntity.ok(adminService.getAllFreelancers());
    }

    @Operation(summary = "Получить профиль фрилансеров", description = "Возвращает профиль пользователя по userId с ролью FREELANCER")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Профиль получен успешно"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет прав"),
            @ApiResponse(responseCode = "404", description = "Профиль не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/freelancers/{userId}")
    public ResponseEntity<FreelancerProfileDto> getFreelancerById(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.getFreelancerById(userId));
    }

    @Operation(summary = "Удалить фрилансера", description = "Удаляет профиль фрилансера по userId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Фрилансер успешно деактивирован"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет прав"),
            @ApiResponse(responseCode = "404", description = "Профиль не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/freelancers/{userId}")
    public ResponseEntity<?> deleteFreelancer(@PathVariable Long userId) {
        adminService.deactivateFreelancer(userId);
        return ResponseEntity.ok(new DefaultResponse("Фрилансер успешно удалён"));
    }

    @Operation(summary = "Получить список заказчиков", description = "Возвращает список всех профилей пользователей с ролью CLIENT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Профиль получен успешно"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет прав"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/clients")
    public ResponseEntity<List<ClientProfileDto>> getAllClients() {
        return ResponseEntity.ok(adminService.getAllClients());
    }

    @Operation(summary = "Получить профиль заказчика", description = "Возвращает профиль пользователя по id с ролью CLIENT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Профиль получен успешно"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет прав"),
            @ApiResponse(responseCode = "404", description = "Профиль не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/clients/{userId}")
    public ResponseEntity<ClientProfileDto> getClientById(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.getClientById(userId));
    }

    @Operation(summary = "Удалить заказчика", description = "Удаляет профиль пользователя по userId с ролью CLIENT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Фрилансер успешно деактивирован"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет прав"),
            @ApiResponse(responseCode = "404", description = "Профиль не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/clients/{userId}")
    public ResponseEntity<?> deleteClient(@PathVariable Long userId) {
        adminService.deleteClient(userId);
        return ResponseEntity.ok(new DefaultResponse("Заказчик успешно удалён"));
    }

    @Operation(summary = "Получить список проектов пользователя", description = "Возвращает список проектов по id заказчика")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Проекты успешно получены"),
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

    @Operation(
            summary = "Получить список всех проектов",
            description = "Возвращает список всех проектов в системе"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список проектов получен успешно"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован или не имеет прав"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })

    @GetMapping("/projects")
    public ResponseEntity<Page<ProjectAdminListItem>> getProjectsPage(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<ProjectAdminListItem> page = adminService.getAllProjectsPageable(pageable);
        return ResponseEntity.ok(page);
    }

    @Operation(
            summary = "Список портфолио (минимальная проекция) с пагинацией",
            description = "Возвращает ID, заголовок, имя/фамилию фрилансера и дату создания"
    )
    @GetMapping("/portfolios")
    public ResponseEntity<Page<PortfolioAdminListItem>> getPortfoliosPage(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Page<PortfolioAdminListItem> page = adminService.getAllPortfoliosPageable(pageable);
        return ResponseEntity.ok(page);
    }

    @Operation(
            summary = "Получить полное портфолио по id",
            description = "Возвращает все поля портфолио (включая описание, роли, ссылки и навыки)"
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Портфолио получено успешно"),
            @ApiResponse(responseCode = "401", description = "Не аутентифицирован или нет прав"),
            @ApiResponse(responseCode = "404", description = "Портфолио не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/portfolios/{portfolioId}")
    public ResponseEntity<FreelancerPortfolioDto> getPortfolioById(
            @PathVariable Long portfolioId) {
        FreelancerPortfolioDto dto = adminService.getPortfolioById(portfolioId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/projects/search")
    public ResponseEntity<Page<ProjectAdminListItem>> searchProjects(
            @RequestParam(required = false) String term,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String clientName,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(adminService.searchProjects(term, status, clientName, pageable));
    }

    @GetMapping("/portfolios/search")
    public ResponseEntity<Page<PortfolioAdminListItem>> searchPortfolios(
            @RequestParam(required = false) String term,
            @RequestParam(required = false) String freelancerName,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(adminService.searchPortfolios(term, freelancerName, pageable));
    }

    @GetMapping("/users/search")
    public ResponseEntity<Page<UserDto>> searchUsers(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) UserRole role,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(adminService.searchUsers(email, firstName, lastName, phone, role, pageable));
    }

    @Operation(
            summary = "Активировать фрилансера",
            description = "Меняет статус фрилансера на активный (isActive=true)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Фрилансер успешно активирован"),
            @ApiResponse(responseCode = "401", description = "Не аутентифицирован или нет прав"),
            @ApiResponse(responseCode = "404", description = "Фрилансер не найден"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @PutMapping("/freelancers/{userId}/activate")
    public ResponseEntity<DefaultResponse> activateFreelancer(@PathVariable Long userId) {
        adminService.activateFreelancer(userId);
        return ResponseEntity.ok(new DefaultResponse("Фрилансер успешно активирован"));
    }

    @Operation(
            summary = "Активировать заказчика",
            description = "Меняет статус заказчика на активный (isActive=true)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Заказчик успешно активирован"),
            @ApiResponse(responseCode = "401", description = "Не аутентифицирован или нет прав"),
            @ApiResponse(responseCode = "404", description = "Заказчик не найден"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @PutMapping("/clients/{userId}/activate")
    public ResponseEntity<DefaultResponse> activateClient(@PathVariable Long userId) {
        adminService.activateClient(userId);
        return ResponseEntity.ok(new DefaultResponse("Заказчик успешно активирован"));
    }

    @Operation(
            summary = "Деактивировать фрилансера",
            description = "Меняет статус фрилансера на неактивный (isActive=false)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Фрилансер успешно деактивирован"),
            @ApiResponse(responseCode = "401", description = "Не аутентифицирован или нет прав"),
            @ApiResponse(responseCode = "404", description = "Фрилансер не найден"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @PutMapping("/freelancers/{userId}/deactivate")
    public ResponseEntity<DefaultResponse> deactivateFreelancer(@PathVariable Long userId) {
        adminService.deactivateFreelancer(userId);
        return ResponseEntity.ok(new DefaultResponse("Фрилансер успешно деактивирован"));
    }

    @Operation(
            summary = "Деактивировать заказчика",
            description = "Меняет статус заказчика на неактивный (isActive=false)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Заказчик успешно деактивирован"),
            @ApiResponse(responseCode = "401", description = "Не аутентифицирован или нет прав"),
            @ApiResponse(responseCode = "404", description = "Заказчик не найден"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @PutMapping("/clients/{userId}/deactivate")
    public ResponseEntity<DefaultResponse> deactivateClient(@PathVariable Long userId) {
        adminService.deactivateClient(userId);
        return ResponseEntity.ok(new DefaultResponse("Заказчик успешно деактивирован"));
    }
}