package com.example.jobsyserver.mapper;

import com.example.jobsyserver.dto.freelancer.FreelancerProfileDto;
import com.example.jobsyserver.dto.freelancer.FreelancerProfileUpdateDto;
import com.example.jobsyserver.model.FreelancerProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface FreelancerProfileMapper {

    @Mapping(source = "user", target = "user")
    FreelancerProfileDto toDto(FreelancerProfile profile);

    List<FreelancerProfileDto> toDtoList(List<FreelancerProfile> profiles);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    FreelancerProfile toEntity(FreelancerProfileUpdateDto updateDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "user", ignore = true)
    void updateEntityFromDto(FreelancerProfileUpdateDto updateDto, @MappingTarget FreelancerProfile profile);
}