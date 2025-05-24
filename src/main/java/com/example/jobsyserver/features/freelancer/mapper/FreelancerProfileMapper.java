package com.example.jobsyserver.features.freelancer.mapper;

import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileDto;
import com.example.jobsyserver.features.skill.mapper.SkillMapper;
import com.example.jobsyserver.features.user.mapper.UserMapper;
import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.context.annotation.Primary;


@Primary
@Mapper(componentModel = "spring", uses = {UserMapper.class, SkillMapper.class})
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
            @Mapping(source = "skills", target = "about.skills"),
            @Mapping(source = "avatarUrl", target = "avatarUrl"),
            @Mapping(source = "category.name", target = "about.categoryName"),
            @Mapping(source = "specialization.name", target = "about.specializationName")

    })
    FreelancerProfileDto toDto(FreelancerProfile profile);
}