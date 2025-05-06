package com.example.jobsyserver.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Стандартный ответ")
public class DefaultResponse {
    @Schema(description = "Сообщение", example = "Почта успешно подтверждена")
    private String message;
}
