package com.example.jobsyserver.mapper;

import com.example.jobsyserver.dto.common.SkillDto;
import com.example.jobsyserver.dto.freelancer.FreelancerProfileDto;
import com.example.jobsyserver.model.FreelancerProfile;
import com.example.jobsyserver.model.FreelancerSkill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.context.annotation.Primary;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Primary
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface FreelancerProfileMapper {

    @Mappings({
            @Mapping(source = "country", target = "basic.country"),
            @Mapping(source = "city", target = "basic.city"),
            @Mapping(source = "contactLink", target = "contact.contactLink"),
            @Mapping(source = "categoryId", target = "about.categoryId"),
            @Mapping(source = "specializationId", target = "about.specializationId"),
            @Mapping(source = "experienceLevel", target = "about.experienceLevel"),
            @Mapping(source = "aboutMe", target = "about.aboutMe"),
            @Mapping(source = "user.firstName", target = "basic.firstName"),
            @Mapping(source = "user.lastName", target = "basic.lastName"),
            @Mapping(source = "user.email", target = "basic.email"),
            @Mapping(source = "user.phone", target = "basic.phone"),
            @Mapping(source = "user", target = "user"),
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "createdAt", target = "createdAt"),
            @Mapping(source = "updatedAt", target = "updatedAt"),
            @Mapping(source = "freelancerSkills", target = "about.skills", qualifiedByName = "mapFreelancerSkills"),
            @Mapping(source = "avatarUrl", target = "avatarUrl")

    })
    FreelancerProfileDto toDto(FreelancerProfile profile);

    List<FreelancerProfileDto> toDtoList(List<FreelancerProfile> profiles);

    @Named("mapFreelancerSkills")
    default List<SkillDto> mapFreelancerSkills(List<FreelancerSkill> freelancerSkills) {
        if (freelancerSkills == null) {
            return Collections.emptyList();
        }
        return freelancerSkills.stream()
                .map(fs -> {
                    SkillDto dto = new SkillDto();
                    dto.setId(fs.getSkill().getId());
                    dto.setName(fs.getSkill().getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}