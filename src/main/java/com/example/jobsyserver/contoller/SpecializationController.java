package com.example.jobsyserver.contoller;

import com.example.jobsyserver.dto.common.SpecializationDto;
import com.example.jobsyserver.service.SpecializationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories/{categoryId}/specializations")
@RequiredArgsConstructor
@Tag(name = "categories", description = "Получение списка специализаций по категориям")
public class SpecializationController {

    private final SpecializationService specializationService;

    @GetMapping
    public ResponseEntity<List<SpecializationDto>> getAllByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(specializationService.getAllByCategoryId(categoryId));
    }
}
