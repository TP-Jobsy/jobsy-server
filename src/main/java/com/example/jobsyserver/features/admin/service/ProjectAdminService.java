package com.example.jobsyserver.features.admin.service;

import com.example.jobsyserver.features.project.dto.ProjectDto;
import com.example.jobsyserver.features.project.projection.ProjectAdminListItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ProjectAdminService {
    List<ProjectDto> getByClient(Long clientId);

    List<ProjectDto> getByFreelancer(Long userId);

    ProjectDto getById(Long id);

    void delete(Long id);

    Page<ProjectAdminListItem> pageAll(Pageable pageable);

    Page<ProjectAdminListItem> search(String term, String status,
                                      LocalDateTime from, LocalDateTime to,
                                      Pageable pageable);
}
