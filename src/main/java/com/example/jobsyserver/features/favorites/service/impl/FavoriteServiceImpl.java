package com.example.jobsyserver.features.favorites.service.impl;

import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileDto;
import com.example.jobsyserver.features.freelancer.repository.FreelancerProfileRepository;
import com.example.jobsyserver.features.project.dto.ProjectDto;
import com.example.jobsyserver.features.client.repository.ClientProfileRepository;
import com.example.jobsyserver.features.common.exception.BadRequestException;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.favorites.model.FavoriteFreelancer;
import com.example.jobsyserver.features.favorites.model.FavoriteFreelancerId;
import com.example.jobsyserver.features.favorites.model.FavoriteProject;
import com.example.jobsyserver.features.favorites.model.FavoriteProjectId;
import com.example.jobsyserver.features.favorites.repository.FavoriteFreelancerRepository;
import com.example.jobsyserver.features.favorites.repository.FavoriteProjectRepository;
import com.example.jobsyserver.features.project.repository.ProjectRepository;
import com.example.jobsyserver.features.freelancer.mapper.FreelancerProfileMapper;
import com.example.jobsyserver.features.project.mapper.ProjectMapper;
import com.example.jobsyserver.features.favorites.service.FavoriteService;
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