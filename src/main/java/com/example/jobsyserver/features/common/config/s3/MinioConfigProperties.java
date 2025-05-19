package com.example.jobsyserver.features.common.config.s3;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Validated
@ConfigurationProperties(prefix = "s3.client", ignoreUnknownFields = false)
public record MinioConfigProperties(
        @NotBlank String endpoint,
        @NotBlank String region,
        @NotBlank String publicUrl,
        @NotBlank(message = "Не задан ключ доступа S3") String accessKey,
        @NotBlank(message = "Не задан секретный ключ S3") String secretKey,
        @NotBlank String bucket,
        @Min(value = 1, message = "maxSize должен быть > 0") long maxSize,
        @NotEmpty List<String> allowedExt
) {}