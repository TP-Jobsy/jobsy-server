package com.example.jobsyserver.mapper;

import com.example.jobsyserver.dto.client.ClientProfileDto;
import com.example.jobsyserver.model.ClientProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Primary
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ClientProfileMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "createdAt", target = "createdAt"),
            @Mapping(source = "updatedAt", target = "updatedAt"),
            @Mapping(source = "companyName", target = "basic.companyName"),
            @Mapping(source = "position", target = "basic.position"),
            @Mapping(source = "country", target = "basic.country"),
            @Mapping(source = "city", target = "basic.city"),
            @Mapping(source = "user.firstName", target = "basic.firstName"),
            @Mapping(source = "user.lastName", target = "basic.lastName"),
            @Mapping(source = "user.email", target = "basic.email"),
            @Mapping(source = "user.phone", target = "basic.phone"),
            @Mapping(source = "contactLink", target = "contact.contactLink"),
            @Mapping(source = "fieldDescription", target = "field.fieldDescription"),
            @Mapping(source = "user", target = "user"),
            @Mapping(source = "avatarUrl", target = "avatarUrl")
    })
    ClientProfileDto toDto(ClientProfile profile);

    List<ClientProfileDto> toDtoList(List<ClientProfile> profiles);
}