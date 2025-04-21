package com.example.jobsyserver.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AvatarStorageService {
    String uploadAvatar(String profileType, Long profileId, MultipartFile file) throws IOException;
}
