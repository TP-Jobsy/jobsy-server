package com.example.jobsyserver.mapper;

import com.example.jobsyserver.dto.common.SpecializationDto;
import com.example.jobsyserver.model.Category;
import com.example.jobsyserver.model.Specialization;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SpecializationMapper {
    @Mapping(source = "category.id", target = "categoryId")
    SpecializationDto toDto(Specialization specialization);

    @Mapping(source = "categoryId", target = "category")
    Specialization toEntity(SpecializationDto specializationDto);

    default Category map(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }
}