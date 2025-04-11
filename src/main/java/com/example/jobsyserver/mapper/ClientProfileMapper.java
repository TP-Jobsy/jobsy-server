package com.example.jobsyserver.mapper;

import com.example.jobsyserver.dto.client.ClientProfileBasicDto;
import com.example.jobsyserver.dto.client.ClientProfileContactDto;
import com.example.jobsyserver.dto.client.ClientProfileFieldDto;
import com.example.jobsyserver.dto.client.ClientProfileDto;
import com.example.jobsyserver.dto.user.UserDto;
import com.example.jobsyserver.model.ClientProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Primary
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ClientProfileMapper {

    default ClientProfileBasicDto toBasicDto(ClientProfile profile) {
        if (profile == null) {
            return null;
        }
        ClientProfileBasicDto dto = new ClientProfileBasicDto();
        dto.setCompanyName(profile.getCompanyName());
        dto.setPosition(profile.getPosition());
        dto.setCountry(profile.getCountry());
        dto.setCity(profile.getCity());
        return dto;
    }

    default ClientProfileContactDto toContactDto(ClientProfile profile) {
        if (profile == null) {
            return null;
        }
        ClientProfileContactDto dto = new ClientProfileContactDto();
        dto.setContactLink(profile.getContactLink());
        return dto;
    }

    default ClientProfileFieldDto toFieldDto(ClientProfile profile) {
        if (profile == null) {
            return null;
        }
        ClientProfileFieldDto dto = new ClientProfileFieldDto();
        dto.setFieldDescription(profile.getFieldDescription());
        return dto;
    }

    default UserDto toUserDto(ClientProfile profile) {
        if (profile == null || profile.getUser() == null) {
            return null;
        }
        UserDto dto = new UserDto();
        dto.setFirstName(profile.getUser().getFirstName());
        dto.setLastName(profile.getUser().getLastName());
        dto.setPhone(profile.getUser().getPhone());
        dto.setEmail(profile.getUser().getEmail());
        return dto;
    }

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "createdAt", target = "createdAt"),
            @Mapping(source = "updatedAt", target = "updatedAt")
    })
    default ClientProfileDto toDto(ClientProfile profile) {
        if (profile == null) {
            return null;
        }
        ClientProfileDto dto = new ClientProfileDto();
        dto.setId(profile.getId());
        dto.setCreatedAt(profile.getCreatedAt());
        dto.setUpdatedAt(profile.getUpdatedAt());
        dto.setBasic(toBasicDto(profile));
        dto.setContact(toContactDto(profile));
        dto.setField(toFieldDto(profile));
        dto.setUser(toUserDto(profile));
        return dto;
    }

    List<ClientProfileDto> toDtoList(List<ClientProfile> profiles);

}