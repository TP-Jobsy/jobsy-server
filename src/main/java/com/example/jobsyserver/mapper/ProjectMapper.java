package com.example.jobsyserver.mapper;

import com.example.jobsyserver.dto.common.SkillDto;
import com.example.jobsyserver.dto.project.ProjectCreateDto;
import com.example.jobsyserver.dto.project.ProjectDto;
import com.example.jobsyserver.dto.project.ProjectUpdateDto;
import com.example.jobsyserver.model.Project;
import com.example.jobsyserver.model.ProjectSkill;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
public interface ProjectMapper {

    @Mappings({
            @Mapping(source = "complexity", target = "projectComplexity"),
            @Mapping(source = "duration",   target = "projectDuration"),
            @Mapping(target = "id",                    ignore = true),
            @Mapping(target = "status",                ignore = true),
            @Mapping(target = "createdAt",             ignore = true),
            @Mapping(target = "updatedAt",             ignore = true),
            @Mapping(target = "client",                ignore = true),
            @Mapping(target = "assignedFreelancer",    ignore = true),
            @Mapping(target = "projectSkills", ignore = true),
    })
    Project toEntity(ProjectCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(source = "complexity", target = "projectComplexity"),
            @Mapping(source = "duration",   target = "projectDuration"),
            @Mapping(target = "assignedFreelancer", ignore = true),
            @Mapping(target = "client",             ignore = true),
            @Mapping(target = "createdAt",          ignore = true),
            @Mapping(target = "updatedAt",          ignore = true),
            @Mapping(target = "id",                 ignore = true),
            @Mapping(target = "status",             ignore = true),
            @Mapping(target = "projectSkills", ignore = true),
    })
    Project toEntity(ProjectUpdateDto dto, @MappingTarget Project project);

    @Mappings({
            @Mapping(source = "id",                 target = "id"),
            @Mapping(source = "title",              target = "title"),
            @Mapping(source = "description",        target = "description"),
            @Mapping(source = "projectComplexity",  target = "complexity"),
            @Mapping(source = "projectDuration",    target = "duration"),
            @Mapping(source = "paymentType",        target = "paymentType"),
            @Mapping(source = "fixedPrice",         target = "fixedPrice"),
            @Mapping(source = "status",             target = "status"),
            @Mapping(source = "createdAt",          target = "createdAt"),
            @Mapping(source = "updatedAt",          target = "updatedAt"),
            @Mapping(source = "category",           target = "category"),
            @Mapping(source = "specialization",     target = "specialization"),
            @Mapping(source = "client",             target = "client"),
            @Mapping(source = "assignedFreelancer", target = "assignedFreelancer"),
            @Mapping(source = "projectSkills", target = "skills", qualifiedByName = "mapProjectSkills"
            )
    })
    ProjectDto toDto(Project project);

    @Named("mapProjectSkills")
    default List<SkillDto> mapProjectSkills(List<ProjectSkill> projectSkills) {
        if (projectSkills == null) {
            return Collections.emptyList();
        }
        return projectSkills.stream()
                .map(fs -> {
                    SkillDto dto = new SkillDto();
                    dto.setId(fs.getSkill().getId());
                    dto.setName(fs.getSkill().getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}