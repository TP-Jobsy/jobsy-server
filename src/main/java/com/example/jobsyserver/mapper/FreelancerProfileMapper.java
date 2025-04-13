package com.example.jobsyserver.mapper;

import com.example.jobsyserver.dto.freelancer.FreelancerProfileDto;
import com.example.jobsyserver.model.FreelancerProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.context.annotation.Primary;

import java.util.List;

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
            @Mapping(source = "user", target = "user"),
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "createdAt", target = "createdAt"),
            @Mapping(source = "updatedAt", target = "updatedAt")
    })
    FreelancerProfileDto toDto(FreelancerProfile profile);

    List<FreelancerProfileDto> toDtoList(List<FreelancerProfile> profiles);
}