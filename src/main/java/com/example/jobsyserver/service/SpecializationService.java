package com.example.jobsyserver.service;

import com.example.jobsyserver.dto.common.SpecializationDto;

import java.util.List;

public interface SpecializationService {
    List<SpecializationDto> getAllSpecializations();
    List<SpecializationDto> getAllByCategoryId(Long categoryId);
    SpecializationDto getSpecializationById(Long id);
    SpecializationDto createSpecialization(SpecializationDto specializationDto);
    SpecializationDto updateSpecialization(Long id, SpecializationDto specializationDto);
    void deleteSpecializationById(Long id);
}