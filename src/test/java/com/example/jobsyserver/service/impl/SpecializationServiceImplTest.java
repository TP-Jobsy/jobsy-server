package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.common.SpecializationDto;
import com.example.jobsyserver.exception.SpecializationNotFoundException;
import com.example.jobsyserver.mapper.SpecializationMapper;
import com.example.jobsyserver.model.Category;
import com.example.jobsyserver.model.Specialization;
import com.example.jobsyserver.repository.SpecializationRepository;
import com.example.jobsyserver.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecializationServiceImplTest {

    @Mock
    private SpecializationRepository specializationRepository;

    @Mock
    private SpecializationMapper specializationMapper;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private SpecializationServiceImpl specializationService;

    @Test
    void testGetAllSpecializations() {
        Specialization spec1 = new Specialization(1L, new Category(1L, "IT"), "Frontend");
        Specialization spec2 = new Specialization(2L, new Category(1L, "IT"), "Backend");
        SpecializationDto dto1 = new SpecializationDto();
        dto1.setId(1L);
        dto1.setName("Frontend");
        SpecializationDto dto2 = new SpecializationDto();
        dto2.setId(2L);
        dto2.setName("Backend");
        when(specializationRepository.findAll()).thenReturn(java.util.Arrays.asList(spec1, spec2));
        when(specializationMapper.toDto(spec1)).thenReturn(dto1);
        when(specializationMapper.toDto(spec2)).thenReturn(dto2);
        var result = specializationService.getAllSpecializations();
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(specializationRepository, times(1)).findAll();
    }

    @Test
    void testGetSpecializationById_found() {
        Specialization spec = new Specialization(1L, new Category(1L, "IT"), "Frontend");
        SpecializationDto dto = new SpecializationDto();
        dto.setId(1L);
        dto.setName("Frontend");
        when(specializationRepository.findById(1L)).thenReturn(Optional.of(spec));
        when(specializationMapper.toDto(spec)).thenReturn(dto);
        var result = specializationService.getSpecializationById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(specializationRepository, times(1)).findById(1L);
    }

    @Test
    void testGetSpecializationById_notFound() {
        when(specializationRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(SpecializationNotFoundException.class, () -> specializationService.getSpecializationById(1L));
        verify(specializationRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateSpecialization() {
        SpecializationDto inputDto = new SpecializationDto();
        inputDto.setName("Frontend");
        inputDto.setCategoryId(1L);
        Specialization specializationEntity = new Specialization();
        specializationEntity.setName("Frontend");
        Category category = new Category(1L, "IT");
        Specialization savedSpecialization = new Specialization(1L, category, "Frontend");
        SpecializationDto outputDto = new SpecializationDto();
        outputDto.setId(1L);
        outputDto.setName("Frontend");
        outputDto.setCategoryId(1L);
        when(specializationMapper.toEntity(inputDto)).thenReturn(specializationEntity);
        when(categoryService.findCategoryById(1L)).thenReturn(category);
        when(specializationRepository.save(specializationEntity)).thenReturn(savedSpecialization);
        when(specializationMapper.toDto(savedSpecialization)).thenReturn(outputDto);
        var result = specializationService.createSpecialization(inputDto);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Frontend", result.getName());
        assertEquals(1L, result.getCategoryId());
        verify(categoryService, times(1)).findCategoryById(1L);
        verify(specializationRepository, times(1)).save(specializationEntity);
    }

    @Test
    void testUpdateSpecialization() {
        Category oldCategory = new Category(1L, "IT");
        Specialization existingSpecialization = new Specialization(1L, oldCategory, "OldName");
        SpecializationDto updateDto = new SpecializationDto();
        updateDto.setName("NewName");
        updateDto.setCategoryId(1L);
        Category newCategory = new Category(1L, "IT");
        Specialization updatedSpecialization = new Specialization(1L, newCategory, "NewName");
        SpecializationDto updatedDto = new SpecializationDto();
        updatedDto.setId(1L);
        updatedDto.setName("NewName");
        updatedDto.setCategoryId(1L);
        when(specializationRepository.findById(1L)).thenReturn(Optional.of(existingSpecialization));
        when(categoryService.findCategoryById(1L)).thenReturn(newCategory);
        when(specializationRepository.save(existingSpecialization)).thenReturn(updatedSpecialization);
        when(specializationMapper.toDto(updatedSpecialization)).thenReturn(updatedDto);
        var result = specializationService.updateSpecialization(1L, updateDto);
        assertNotNull(result);
        assertEquals("NewName", result.getName());
        verify(specializationRepository, times(1)).findById(1L);
        verify(categoryService, times(1)).findCategoryById(1L);
        verify(specializationRepository, times(1)).save(existingSpecialization);
    }

    @Test
    void testDeleteSpecializationById_found() {
        when(specializationRepository.existsById(1L)).thenReturn(true);
        specializationService.deleteSpecializationById(1L);
        verify(specializationRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteSpecializationById_notFound() {
        when(specializationRepository.existsById(1L)).thenReturn(false);
        assertThrows(SpecializationNotFoundException.class, () -> specializationService.deleteSpecializationById(1L));
        verify(specializationRepository, times(1)).existsById(1L);
    }
}