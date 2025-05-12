package com.example.jobsyserver.features.project.mapper;

import com.example.jobsyserver.features.project.dto.ProjectApplicationDto;
import com.example.jobsyserver.features.project.dto.ProjectApplicationRequestDto;
import com.example.jobsyserver.features.project.model.ProjectApplication;
import org.mapstruct.*;
import org.springframework.context.annotation.Primary;

@Primary
@Mapper(componentModel = "spring")
public interface ProjectApplicationMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "project", ignore = true),
            @Mapping(target = "freelancer", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "applicationType", ignore = true)
    })
    ProjectApplication toEntity(ProjectApplicationRequestDto dto);

    @Mappings({
            @Mapping(source = "project.id",       target = "projectId"),
            @Mapping(source = "freelancer.id",    target = "freelancerId"),
            @Mapping(source = "applicationType",  target = "applicationType"),
            @Mapping(source = "status",           target = "status"),
            @Mapping(source = "id",               target = "id"),
            @Mapping(source = "createdAt",        target = "createdAt")
    })
    ProjectApplicationDto toDto(ProjectApplication projectApplication);
}
