package com.example.jobsyserver.features.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Запрос на обновление access-токена по refresh-токену")
public class TokenRefreshRequest {
    @NotBlank
    @Schema(description = "Refresh-токен", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
    private String refreshToken;
}
