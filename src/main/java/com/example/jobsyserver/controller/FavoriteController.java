package com.example.jobsyserver.controller;

import com.example.jobsyserver.dto.freelancer.FreelancerProfileDto;
import com.example.jobsyserver.dto.project.ProjectDto;
import com.example.jobsyserver.service.FavoriteService;
import com.example.jobsyserver.service.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
@Tag(name="Favorites", description="Избранное")
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final SecurityService security;

    @Operation(
            summary = "Список избранных проектов фрилансера",
            description = "Возвращает все проекты, которые текущий фрилансер добавил в избранное"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список найденных проектов"),
            @ApiResponse(responseCode = "401", description = "Неавторизованный доступ"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/projects")
    @PreAuthorize("hasRole('FREELANCER')")
    public List<ProjectDto> getMyFavoriteProjects() {
        Long frId = security.getCurrentFreelancerProfileId();
        return favoriteService.getFavoriteProjects(frId);
    }

    @Operation(summary = "Добавить проект в избранное", description = "Добавляет проект в избранное текущего фрилансера")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Проект успешно добавлен"),
            @ApiResponse(responseCode = "400", description = "Неверный ID проекта"),
            @ApiResponse(responseCode = "401", description = "Неавторизованный доступ"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/projects/{projectId}")
    @PreAuthorize("hasRole('FREELANCER')")
    @ResponseStatus(HttpStatus.CREATED)
    public void addFavoriteProject(@PathVariable Long projectId) {
        Long frId = security.getCurrentFreelancerProfileId();
        favoriteService.addProjectToFavorites(frId, projectId);
    }

    @Operation(summary = "Удалить проект из избранного", description = "Удаляет проект из избранного текущего фрилансера")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Проект успешно удалён"),
            @ApiResponse(responseCode = "400", description = "Неверный ID проекта"),
            @ApiResponse(responseCode = "401", description = "Неавторизованный доступ"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/projects/{projectId}")
    @PreAuthorize("hasRole('FREELANCER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFavoriteProject(@PathVariable Long projectId) {
        Long frId = security.getCurrentFreelancerProfileId();
        favoriteService.removeProjectFromFavorites(frId, projectId);
    }

    @Operation(summary = "Список избранных фрилансеров клиента", description = "Возвращает всех фрилансеров, которых текущий клиент добавил в избранное")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список найденных фрилансеров"),
            @ApiResponse(responseCode = "401", description = "Неавторизованный доступ"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/freelancers")
    @PreAuthorize("hasRole('CLIENT')")
    public List<FreelancerProfileDto> getMyFavoriteFreelancers() {
        Long clientId = security.getCurrentClientProfileId();
        return favoriteService.getFavoriteFreelancers(clientId);
    }

    @Operation(summary = "Добавить фрилансера в избранное", description = "Добавляет фрилансера в избранное текущего клиента")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Фрилансер успешно добавлен"),
            @ApiResponse(responseCode = "400", description = "Неверный ID фрилансера"),
            @ApiResponse(responseCode = "401", description = "Неавторизованный доступ"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/freelancers/{freelancerId}")
    @PreAuthorize("hasRole('CLIENT')")
    @ResponseStatus(HttpStatus.CREATED)
    public void addFavoriteFreelancer(@PathVariable Long freelancerId) {
        Long clientId = security.getCurrentClientProfileId();
        favoriteService.addFreelancerToFavorites(clientId, freelancerId);
    }

    @Operation(summary = "Удалить фрилансера из избранного", description = "Удаляет фрилансера из избранного текущего клиента")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Фрилансер успешно удалён"),
            @ApiResponse(responseCode = "400", description = "Неверный ID фрилансера"),
            @ApiResponse(responseCode = "401", description = "Неавторизованный доступ"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/freelancers/{freelancerId}")
    @PreAuthorize("hasRole('CLIENT')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFavoriteFreelancer(@PathVariable Long freelancerId) {
        Long clientId = security.getCurrentClientProfileId();
        favoriteService.removeFreelancerFromFavorites(clientId, freelancerId);
    }
}