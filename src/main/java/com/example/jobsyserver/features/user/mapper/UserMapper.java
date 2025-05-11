package com.example.jobsyserver.features.user.mapper;

import com.example.jobsyserver.features.user.dto.UserDto;
import com.example.jobsyserver.features.user.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
