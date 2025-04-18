package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.project.ProjectApplicationDto;
import com.example.jobsyserver.dto.project.ProjectApplicationRequestDto;
import com.example.jobsyserver.enums.ProjectApplicationStatus;
import com.example.jobsyserver.exception.ProjectApplicationNotFoundException;
import com.example.jobsyserver.exception.ProjectNotFoundException;
import com.example.jobsyserver.exception.UserNotFoundException;
import com.example.jobsyserver.mapper.ProjectApplicationMapper;
import com.example.jobsyserver.model.ProjectApplication;
import com.example.jobsyserver.repository.FreelancerProfileRepository;
import com.example.jobsyserver.repository.ProjectApplicationRepository;
import com.example.jobsyserver.repository.ProjectRepository;
import com.example.jobsyserver.service.ProjectApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectApplicationServiceImpl implements ProjectApplicationService {

    private final ProjectApplicationRepository repository;
    private final ProjectRepository projectRepository;
    private final FreelancerProfileRepository freelancerProfileRepository;
    private final ProjectApplicationMapper mapper;

    @Transactional
    public ProjectApplicationDto createApplication(ProjectApplicationRequestDto dto){
        ProjectApplication application = mapper.toEntity(dto);

        application.setProject(projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException("Проект не найден")));

        application.setFreelancer(freelancerProfileRepository.findById(dto.getFreelancerId())
                .orElseThrow(() -> new UserNotFoundException("Фрилансер не найден")));

        return mapper.toDto(repository.save(application));
    }

    @Transactional(readOnly = true)
    public ProjectApplicationDto getById(Long id){
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ProjectApplicationNotFoundException("Заявка не найдена"));
    }

    @Transactional
    public ProjectApplicationDto updateStatus(Long id, ProjectApplicationStatus newStatus){
        ProjectApplication application = repository.findById(id)
                .orElseThrow(() -> new ProjectApplicationNotFoundException("Заявка не найдена"));

        application.setStatus(newStatus);
        return mapper.toDto(repository.save(application));
    }

    @Transactional
    public void deleteApplication(Long id){
        ProjectApplication application = repository.findById(id)
                .orElseThrow(() -> new ProjectApplicationNotFoundException("Заявка не найдена"));

        repository.delete(application);
    }

}
