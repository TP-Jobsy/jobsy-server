package com.example.jobsyserver.controller;

import com.example.jobsyserver.configuration.SearchConfigProperties;
import com.example.jobsyserver.dto.freelancer.FreelancerProfileDto;
import com.example.jobsyserver.dto.project.ProjectDto;
import com.example.jobsyserver.exception.BadRequestException;
import com.example.jobsyserver.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
public class SearchController {

    private final SearchConfigProperties searchProps;
    private final SearchService searchService;

    @Operation(summary = "Клиент: поиск фрилансеров по навыкам и/или тексту")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Результаты поиска фрилансеров"),
            @ApiResponse(responseCode = "400", description = "Неверные параметры запроса"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/client/search/freelancers")
    public ResponseEntity<List<FreelancerProfileDto>> searchFreelancersForClient(
            @RequestParam(name = "skills", required = false) List<Long> skills,
            @RequestParam(name = "term",   required = false) String term
    ) {
        if (skills != null && skills.size() > searchProps.maxSkills()) {
            throw new BadRequestException(
                    String.format("Нельзя указывать более %d навыков для поиска", searchProps.maxSkills())
            );
        }
        var results = searchService.searchFreelancers(skills, term);
        return ResponseEntity.ok(results);
    }

    @Operation(summary = "Фрилансер: поиск проектов по навыкам и/или тексту")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Результаты поиска проектов"),
            @ApiResponse(responseCode = "400", description = "Неверные параметры запроса"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PreAuthorize("hasRole('FREELANCER')")
    @GetMapping("/freelancer/search/projects")
    public ResponseEntity<List<ProjectDto>> searchProjectsForFreelancer(
            @RequestParam(name = "skills", required = false) List<Long> skills,
            @RequestParam(name = "term",   required = false) String term
    ) {
        if (skills != null && skills.size() > searchProps.maxSkills()) {
            throw new BadRequestException(
                    String.format("Нельзя указывать более %d навыков для поиска", searchProps.maxSkills())
            );
        }
        var results = searchService.searchProjects(skills, term);
        return ResponseEntity.ok(results);
    }
}