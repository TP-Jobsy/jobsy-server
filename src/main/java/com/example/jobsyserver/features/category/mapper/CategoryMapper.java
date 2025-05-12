package com.example.jobsyserver.features.category.mapper;

import com.example.jobsyserver.features.category.dto.CategoryDto;
import com.example.jobsyserver.features.category.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);
    Category toEntity(CategoryDto dto);
}