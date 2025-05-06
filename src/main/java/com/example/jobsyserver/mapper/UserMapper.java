package com.example.jobsyserver.mapper;

import com.example.jobsyserver.dto.user.UserDto;
import com.example.jobsyserver.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
