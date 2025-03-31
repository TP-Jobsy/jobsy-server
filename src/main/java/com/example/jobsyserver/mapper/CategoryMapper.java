package com.example.jobsyserver.mapper;

import com.example.jobsyserver.dto.common.CategoryDto;
import com.example.jobsyserver.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);
}