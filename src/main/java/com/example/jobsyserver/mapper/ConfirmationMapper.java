package com.example.jobsyserver.mapper;

import com.example.jobsyserver.dto.confirmation.ConfirmationDto;
import com.example.jobsyserver.model.Confirmation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConfirmationMapper {
    @Mapping(source = "user.id", target = "userId")
    ConfirmationDto toDto(Confirmation confirmation);

    @Mapping(target = "user", ignore = true)
    Confirmation toEntity(ConfirmationDto dto);
}