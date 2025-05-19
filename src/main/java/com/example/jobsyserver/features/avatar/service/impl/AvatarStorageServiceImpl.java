package com.example.jobsyserver.features.avatar.service.impl;

import com.example.jobsyserver.features.common.config.s3.MinioConfigProperties;
import com.example.jobsyserver.features.common.exception.AvatarStorageException;
import com.example.jobsyserver.features.avatar.service.AvatarStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@ConditionalOnProperty(prefix = "s3", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class AvatarStorageServiceImpl implements AvatarStorageService {

    private final S3Client s3;
    private final MinioConfigProperties props;

    @Override
    public String uploadAvatar(String profileType, Long profileId, MultipartFile file) {
        validateFile(file);

        String ext = extractExtension(file.getOriginalFilename());
        String key = buildKey(profileType, profileId, ext);

        try (var is = file.getInputStream()) {
            s3.putObject(
                    PutObjectRequest.builder()
                            .bucket(props.bucket())
                            .key(key)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromInputStream(is, file.getSize())
            );
        } catch (IOException e) {
            throw new AvatarStorageException("Не удалось сохранить аватар: " + e.getMessage());
        }

        return String.format("%s/avatars/%s", props.publicUrl(), key);
    }

    private void validateFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new AvatarStorageException("Недопустимый формат файла: " + contentType);
        }
        if (file.getSize() > props.maxSize()) {
            throw new AvatarStorageException(
                    "Слишком большой файл: " + file.getSize() / 1024 + "KB, максимальный " + props.maxSize() / 1024 + "KB");
        }
    }

    private String extractExtension(String filename) {
        String ext = Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(f.lastIndexOf('.') + 1).toLowerCase())
                .orElse("");
        if (!props.allowedExt().contains(ext)) {
            throw new AvatarStorageException("Недопустимое расширение файла: " + ext);
        }
        return ext;
    }

    private String buildKey(String profileType, Long profileId, String ext) {
        return String.format("%s/%d/%s.%s",
                profileType, profileId, UUID.randomUUID(), ext);
    }
}
