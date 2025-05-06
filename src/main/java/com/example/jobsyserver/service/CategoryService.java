package com.example.jobsyserver.service;

import com.example.jobsyserver.dto.common.CategoryDto;
import com.example.jobsyserver.model.Category;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategories();
    CategoryDto getCategoryById(Long id);
    CategoryDto createCategory(CategoryDto categoryDto);
    CategoryDto updateCategory(Long id, CategoryDto categoryDto);
    void deleteCategoryById(Long id);
    Category findCategoryById(Long id);
}