package com.example.jobsyserver.features.specialization.mapper;

import com.example.jobsyserver.features.specialization.dto.SpecializationDto;
import com.example.jobsyserver.features.category.model.Category;
import com.example.jobsyserver.features.specialization.model.Specialization;
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