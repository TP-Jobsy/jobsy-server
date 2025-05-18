package com.example.jobsyserver.features.project.controller;

import com.example.jobsyserver.features.project.dto.ProjectDto;
import com.example.jobsyserver.features.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectCompletionController {

    private final ProjectService projectService;

    @PreAuthorize("hasRole('CLIENT')")
    @PatchMapping("/{projectId}/complete/client")
    public ResponseEntity<ProjectDto> completeByClient(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.completeByClient(projectId));
    }

    @PreAuthorize("hasRole('FREELANCER')")
    @PatchMapping("/{projectId}/complete/freelancer")
    public ResponseEntity<ProjectDto> completeByFreelancer(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.completeByFreelancer(projectId));
    }
}
