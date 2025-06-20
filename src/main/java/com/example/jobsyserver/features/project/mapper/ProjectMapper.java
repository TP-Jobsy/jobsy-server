package com.example.jobsyserver.features.project.mapper;

import com.example.jobsyserver.features.project.dto.ProjectCreateDto;
import com.example.jobsyserver.features.project.dto.ProjectDto;
import com.example.jobsyserver.features.project.dto.ProjectUpdateDto;
import com.example.jobsyserver.features.category.mapper.CategoryMapper;
import com.example.jobsyserver.features.client.mapper.ClientProfileMapper;
import com.example.jobsyserver.features.project.model.Project;
import com.example.jobsyserver.features.freelancer.mapper.FreelancerProfileMapper;
import com.example.jobsyserver.features.skill.mapper.SkillMapper;
import com.example.jobsyserver.features.specialization.mapper.SpecializationMapper;
import org.mapstruct.*;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                CategoryMapper.class,
                SpecializationMapper.class,
                SkillMapper.class,
                ClientProfileMapper.class,
                FreelancerProfileMapper.class
        }
)
@Primary
public interface ProjectMapper {

    @Mappings({
            @Mapping(source = "complexity", target = "projectComplexity"),
            @Mapping(source = "duration", target = "projectDuration"),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "client", ignore = true),
            @Mapping(target = "assignedFreelancer", ignore = true),
            @Mapping(target = "clientCompleted", ignore = true),
            @Mapping(target = "freelancerCompleted", ignore = true),
            @Mapping(target = "skills", ignore = true),
            @Mapping(target = "aiRequests", ignore = true)
    })
    Project toEntity(ProjectCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(source = "complexity", target = "projectComplexity"),
            @Mapping(source = "duration", target = "projectDuration"),
            @Mapping(target = "assignedFreelancer", ignore = true),
            @Mapping(target = "client", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "clientCompleted", ignore = true),
            @Mapping(target = "freelancerCompleted", ignore = true),
            @Mapping(target = "skills", ignore = true),
            @Mapping(target = "aiRequests", ignore = true)
    })
    Project toEntity(ProjectUpdateDto dto, @MappingTarget Project project);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "title", target = "title"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "projectComplexity", target = "complexity"),
            @Mapping(source = "projectDuration", target = "duration"),
            @Mapping(source = "paymentType", target = "paymentType"),
            @Mapping(source = "fixedPrice", target = "fixedPrice"),
            @Mapping(source = "status", target = "status"),
            @Mapping(source = "createdAt", target = "createdAt"),
            @Mapping(source = "updatedAt", target = "updatedAt"),
            @Mapping(source = "category", target = "category"),
            @Mapping(source = "specialization", target = "specialization"),
            @Mapping(source = "client", target = "client"),
            @Mapping(source = "assignedFreelancer", target = "assignedFreelancer"),
            @Mapping(target = "clientCompleted", ignore = true),
            @Mapping(target = "freelancerCompleted", ignore = true),
            @Mapping(source = "skills", target = "skills")
    })
    ProjectDto toDto(Project project);

    List<ProjectDto> toDtoList(List<Project> projects);
}