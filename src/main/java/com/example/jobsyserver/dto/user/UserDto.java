package com.example.jobsyserver.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Schema(description = "DTO пользователя для отображения")
public class UserDto extends UserBaseDto {

    @Schema(description = "Флаг подтверждения email", example = "true")
    private Boolean isVerified;
}
