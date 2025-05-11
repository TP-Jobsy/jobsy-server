package com.example.jobsyserver.features.common.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Стандартный ответ об ошибке")
public class ErrorResponse {

    @Schema(description = "Код состояния ошибки", example = "404")
    private int status;

    @Schema(description = "Сообщение об ошибке", example = "Ресурс не найден")
    private String message;
}
