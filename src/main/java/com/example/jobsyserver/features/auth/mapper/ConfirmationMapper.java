package com.example.jobsyserver.features.auth.mapper;

import com.example.jobsyserver.features.auth.dto.confirmation.ConfirmationDto;
import com.example.jobsyserver.features.auth.model.Confirmation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConfirmationMapper {
    @Mapping(source = "user.id", target = "userId")
    ConfirmationDto toDto(Confirmation confirmation);

    @Mapping(target = "user", ignore = true)
    Confirmation toEntity(ConfirmationDto dto);
}