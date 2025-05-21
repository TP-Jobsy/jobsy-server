package com.example.jobsyserver.features.search.controller;

import com.example.jobsyserver.features.common.config.search.SearchConfigProperties;
import com.example.jobsyserver.features.freelancer.projection.FreelancerListItem;
import com.example.jobsyserver.features.common.exception.BadRequestException;
import com.example.jobsyserver.features.project.projection.ProjectListItem;
import com.example.jobsyserver.features.search.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<Page<FreelancerListItem>> searchFreelancersForClient(
            @RequestParam(name = "skills", required = false) List<Long> skills,
            @RequestParam(name = "term",   required = false) String term,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        if (skills != null && skills.size() > searchProps.maxSkills()) {
            throw new BadRequestException(
                    String.format("Нельзя указывать более %d навыков для поиска", searchProps.maxSkills())
            );
        }
        return ResponseEntity.ok(searchService.searchFreelancers(skills, term, pageable));
    }

    @Operation(summary = "Фрилансер: поиск проектов по навыкам и/или тексту")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Результаты поиска проектов"),
            @ApiResponse(responseCode = "400", description = "Неверные параметры запроса"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PreAuthorize("hasRole('FREELANCER')")
    @GetMapping("/freelancer/search/projects")
    public ResponseEntity<Page<ProjectListItem>> searchProjectsForFreelancer(
            @RequestParam(name = "skills", required = false) List<Long> skills,
            @RequestParam(name = "term",   required = false) String   term,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        if (skills != null && skills.size() > searchProps.maxSkills()) {
            throw new BadRequestException(
                    String.format("Нельзя указывать более %d навыков для поиска", searchProps.maxSkills())
            );
        }
        return ResponseEntity.ok(searchService.searchProjects(skills, term, pageable));
    }
}