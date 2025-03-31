package com.example.jobsyserver.service;

import com.example.jobsyserver.dto.common.SpecializationDto;
import com.example.jobsyserver.mapper.SpecializationMapper;
import com.example.jobsyserver.repository.SpecializationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpecializationService {

    private final SpecializationRepository specializationRepository;
    private final SpecializationMapper specializationMapper;

    public List<SpecializationDto> getAllByCategoryId(Long categoryId) {
        return specializationRepository.findByCategoryId(categoryId)
                .stream()
                .map(specializationMapper::toDto)
                .collect(Collectors.toList());
    }
}