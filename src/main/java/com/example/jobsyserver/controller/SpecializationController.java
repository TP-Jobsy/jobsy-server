package com.example.jobsyserver.controller;

import com.example.jobsyserver.dto.common.SpecializationDto;
import com.example.jobsyserver.service.SpecializationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Specializations", description = "Операции для работы со специализациями")
public class SpecializationController {

    private final SpecializationService specializationService;

    @GetMapping("/specializations")
    public ResponseEntity<List<SpecializationDto>> getAllSpecializations() {
        List<SpecializationDto> specializations = specializationService.getAllSpecializations();
        return ResponseEntity.ok(specializations);
    }

    @GetMapping("/categories/{categoryId}/specializations")
    public ResponseEntity<List<SpecializationDto>> getSpecializationsByCategory(@PathVariable Long categoryId) {
        List<SpecializationDto> specializations = specializationService.getAllByCategoryId(categoryId);
        return ResponseEntity.ok(specializations);
    }

    @GetMapping("/specializations/{id}")
    public ResponseEntity<SpecializationDto> getSpecializationById(@PathVariable Long id) {
        SpecializationDto specialization = specializationService.getSpecializationById(id);
        return ResponseEntity.ok(specialization);
    }

    @PostMapping("/specializations")
    public ResponseEntity<SpecializationDto> createSpecialization(@RequestBody SpecializationDto specializationDto) {
        SpecializationDto createdSpecialization = specializationService.createSpecialization(specializationDto);
        return ResponseEntity.ok(createdSpecialization);
    }

    @PutMapping("/specializations/{id}")
    public ResponseEntity<SpecializationDto> updateSpecialization(@PathVariable Long id, @RequestBody SpecializationDto specializationDto) {
        SpecializationDto updatedSpecialization = specializationService.updateSpecialization(id, specializationDto);
        return ResponseEntity.ok(updatedSpecialization);
    }

    @DeleteMapping("/specializations/{id}")
    public ResponseEntity<Void> deleteSpecialization(@PathVariable Long id) {
        specializationService.deleteSpecializationById(id);
        return ResponseEntity.noContent().build();
    }
}