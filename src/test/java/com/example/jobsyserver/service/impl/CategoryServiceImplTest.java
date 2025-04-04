package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.common.CategoryDto;
import com.example.jobsyserver.exception.CategoryNotFoundException;
import com.example.jobsyserver.mapper.CategoryMapper;
import com.example.jobsyserver.model.Category;
import com.example.jobsyserver.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void testGetAllCategories() {
        Category category1 = new Category(1L, "Web Development");
        Category category2 = new Category(2L, "Mobile Development");
        List<Category> categories = Arrays.asList(category1, category2);
        CategoryDto dto1 = new CategoryDto();
        dto1.setId(1L);
        dto1.setName("Web Development");
        CategoryDto dto2 = new CategoryDto();
        dto2.setId(2L);
        dto2.setName("Mobile Development");
        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.toDto(category1)).thenReturn(dto1);
        when(categoryMapper.toDto(category2)).thenReturn(dto2);
        List<CategoryDto> result = categoryService.getAllCategories();
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testGetCategoryById_found() {
        Category category = new Category(1L, "Web Development");
        CategoryDto dto = new CategoryDto();
        dto.setId(1L);
        dto.setName("Web Development");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(dto);
        CategoryDto result = categoryService.getCategoryById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCategoryById_notFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(1L));
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateCategory() {
        CategoryDto dto = new CategoryDto();
        dto.setName("Web Development");
        Category category = new Category();
        category.setName("Web Development");
        Category savedCategory = new Category(1L, "Web Development");
        CategoryDto savedDto = new CategoryDto();
        savedDto.setId(1L);
        savedDto.setName("Web Development");
        when(categoryMapper.toEntity(dto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(savedCategory);
        when(categoryMapper.toDto(savedCategory)).thenReturn(savedDto);
        CategoryDto result = categoryService.createCategory(dto);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testUpdateCategory() {
        Category existingCategory = new Category(1L, "Old Name");
        CategoryDto updateDto = new CategoryDto();
        updateDto.setName("New Name");
        Category updatedCategory = new Category(1L, "New Name");
        CategoryDto updatedDto = new CategoryDto();
        updatedDto.setId(1L);
        updatedDto.setName("New Name");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(existingCategory)).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(updatedDto);
        CategoryDto result = categoryService.updateCategory(1L, updateDto);
        assertNotNull(result);
        assertEquals("New Name", result.getName());
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(existingCategory);
    }

    @Test
    void testDeleteCategoryById() {
        when(categoryRepository.existsById(1L)).thenReturn(true);
        categoryService.deleteCategoryById(1L);
        verify(categoryRepository, times(1)).deleteById(1L);
    }
}
