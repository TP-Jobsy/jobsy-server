package com.example.jobsyserver.mapper;

import com.example.jobsyserver.dto.project.ProjectApplicationDto;
import com.example.jobsyserver.dto.project.ProjectApplicationRequestDto;
import com.example.jobsyserver.model.ProjectApplication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.context.annotation.Primary;

@Primary
@Mapper(componentModel = "spring")
public interface ProjectApplicationMapper {
    @Mappings({
            @Mapping(target = "project", ignore = true),
            @Mapping(target = "freelancer", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createdAt", ignore = true)
    })
    ProjectApplication toEntity(ProjectApplicationRequestDto dto);

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "freelancer.id", target = "freelancerId")
    ProjectApplicationDto toDto(ProjectApplication projectApplication);
}
