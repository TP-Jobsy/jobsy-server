package com.example.jobsyserver.features.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Тип оплаты проекта")
public enum PaymentType {
    @Schema(description = "Почасовая оплата")
    HOURLY,

    @Schema(description = "Фиксированная стоимость")
    FIXED
}

