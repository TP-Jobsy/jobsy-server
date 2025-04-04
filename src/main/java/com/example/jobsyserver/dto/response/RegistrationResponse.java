package com.example.jobsyserver.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Ответ при успешной регистрации пользователя")
public class RegistrationResponse {

    @Schema(description = "Идентификатор пользователя", example = "1")
    private Long userId;

    @Schema(description = "Сообщение", example = "Регистрация успешна. Проверьте свою почту для подтверждения")
    private String message;
}
