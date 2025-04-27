package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.common.SpecializationDto;
import com.example.jobsyserver.exception.ResourceNotFoundException;
import com.example.jobsyserver.mapper.SpecializationMapper;
import com.example.jobsyserver.model.Category;
import com.example.jobsyserver.model.Specialization;
import com.example.jobsyserver.repository.SpecializationRepository;
import com.example.jobsyserver.service.CategoryService;
import com.example.jobsyserver.service.SpecializationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SpecializationServiceImpl implements SpecializationService {

    private final SpecializationRepository specializationRepository;
    private final SpecializationMapper specializationMapper;
    private final CategoryService categoryService;

    @Override
    public List<SpecializationDto> getAllSpecializations() {
        return specializationRepository.findAll()
                .stream()
                .map(specializationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SpecializationDto> getAllByCategoryId(Long categoryId) {
        return specializationRepository.findByCategoryId(categoryId)
                .stream()
                .map(specializationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public SpecializationDto getSpecializationById(Long id) {
        Specialization specialization = specializationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Специализация", id));
        return specializationMapper.toDto(specialization);
    }

    @Override
    public SpecializationDto createSpecialization(SpecializationDto specializationDto) {
        Specialization specialization = specializationMapper.toEntity(specializationDto);
        Long categoryId = specializationDto.getCategoryId();
        if (categoryId != null) {
            Category category = categoryService.findCategoryById(categoryId);
            specialization.setCategory(category);
        }
        Specialization savedSpecialization = specializationRepository.save(specialization);
        return specializationMapper.toDto(savedSpecialization);
    }

    @Override
    public SpecializationDto updateSpecialization(Long id, SpecializationDto specializationDto) {
        Specialization existingSpecialization = specializationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Специализация", id));

        existingSpecialization.setName(specializationDto.getName());

        Long categoryId = specializationDto.getCategoryId();
        if (categoryId != null) {
            Category category = categoryService.findCategoryById(categoryId);
            existingSpecialization.setCategory(category);
        }

        Specialization updatedSpecialization = specializationRepository.save(existingSpecialization);
        return specializationMapper.toDto(updatedSpecialization);
    }

    @Override
    public void deleteSpecializationById(Long id) {
        if (!specializationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Специализация" + id);
        }
        specializationRepository.deleteById(id);
    }
}