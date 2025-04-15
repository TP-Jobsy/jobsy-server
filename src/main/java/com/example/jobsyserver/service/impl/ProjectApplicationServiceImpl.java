package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.project.ProjectApplicationDto;
import com.example.jobsyserver.dto.project.ProjectApplicationRequestDto;
import com.example.jobsyserver.enums.ProjectApplicationStatus;
import com.example.jobsyserver.mapper.ProjectApplicationMapper;
import com.example.jobsyserver.model.ProjectApplication;
import com.example.jobsyserver.repository.ProjectApplicationRepository;
import com.example.jobsyserver.repository.ProjectRepository;
import com.example.jobsyserver.repository.UserRepository;
import com.example.jobsyserver.service.ProjectApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectApplicationServiceImpl implements ProjectApplicationService {

    private final ProjectApplicationRepository repository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectApplicationMapper mapper;

    @Transactional
    public ProjectApplicationDto createApplication(ProjectApplicationRequestDto dto){
        ProjectApplication application = mapper.toEntity(dto);

        application.setProject(projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new RuntimeException("Проект не найден")));

        application.setFreelancer(userRepository.findById(dto.getFreelancerId())
                .orElseThrow(() -> new RuntimeException("Фрилансер не найден")));

        return mapper.toDto(repository.save(application));
    }

    @Transactional(readOnly = true)
    public ProjectApplicationDto getById(Long id){
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("Заявка не найдена"));
    }

    @Transactional
    public ProjectApplicationDto updateStatus(Long id, ProjectApplicationStatus newStatus){
        ProjectApplication application = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Заявка не найдена"));

        application.setStatus(newStatus);
        return mapper.toDto(repository.save(application));
    }

    @Transactional
    public void deleteApplication(Long id){
        ProjectApplication application = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Заявка не найдена"));

        repository.delete(application);
    }

}
