package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.freelancer.FreelancerProfileDto;
import com.example.jobsyserver.dto.project.ProjectDto;
import com.example.jobsyserver.exception.BadRequestException;
import com.example.jobsyserver.exception.ResourceNotFoundException;
import com.example.jobsyserver.mapper.FreelancerProfileMapper;
import com.example.jobsyserver.mapper.ProjectMapper;
import com.example.jobsyserver.model.*;
import com.example.jobsyserver.repository.*;
import com.example.jobsyserver.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteProjectRepository favProjRepo;
    private final FavoriteFreelancerRepository favFrRepo;
    private final ProjectMapper projectMapper;
    private final FreelancerProfileMapper freelancerMapper;
    private final FreelancerProfileRepository freelancerProfileRepo;
    private final ClientProfileRepository clientProfileRepo;
    private final ProjectRepository projectRepo;

    @Override
    @Transactional
    public void addProjectToFavorites(Long frId, Long projectId) {
        var freelancer = freelancerProfileRepo.findById(frId)
                .orElseThrow(() -> new ResourceNotFoundException("Freelancer", frId));
        var project = projectRepo.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", projectId));
        if (favProjRepo.existsByFreelancerIdAndProjectId(frId, projectId)) {
            throw new BadRequestException("Проект уже в избранном");
        }
        FavoriteProject fp = new FavoriteProject();
        fp.setId(new FavoriteProjectId(frId, projectId));
        fp.setFreelancer(freelancer);
        fp.setProject(project);
        favProjRepo.save(fp);
    }

    @Override
    @Transactional
    public void removeProjectFromFavorites(Long frId, Long projectId) {
        favProjRepo.deleteByFreelancerIdAndProjectId(frId, projectId);
    }

    @Override
    public List<ProjectDto> getFavoriteProjects(Long frId) {
        return favProjRepo
                .findByFreelancerId(frId)
                .stream()
                .map(fp -> projectMapper.toDto(fp.getProject()))
                .toList();
    }

    @Override
    @Transactional
    public void addFreelancerToFavorites(Long clientId, Long freelancerId) {
        var client = clientProfileRepo.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client", clientId));
        var freelancer = freelancerProfileRepo.findById(freelancerId)
                .orElseThrow(() -> new ResourceNotFoundException("Freelancer", freelancerId));
        if (favFrRepo.existsByClientIdAndFreelancerId(clientId, freelancerId)) {
            throw new BadRequestException("Фрилансер уже в избранном");
        }
        FavoriteFreelancer ff = new FavoriteFreelancer();
        ff.setId(new FavoriteFreelancerId(clientId, freelancerId));
        ff.setClient(client);
        ff.setFreelancer(freelancer);
        favFrRepo.save(ff);
    }

    @Override
    @Transactional
    public void removeFreelancerFromFavorites(Long clientId, Long freelancerId) {
        favFrRepo.deleteByClientIdAndFreelancerId(clientId, freelancerId);
    }

    @Override
    public List<FreelancerProfileDto> getFavoriteFreelancers(Long clientId) {
        return favFrRepo
                .findByClientId(clientId)
                .stream()
                .map(ff -> freelancerMapper.toDto(ff.getFreelancer()))
                .toList();
    }
}