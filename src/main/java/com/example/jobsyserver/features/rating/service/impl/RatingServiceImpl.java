package com.example.jobsyserver.features.rating.service.impl;

import com.example.jobsyserver.features.auth.service.SecurityService;
import com.example.jobsyserver.features.client.model.ClientProfile;
import com.example.jobsyserver.features.client.repository.ClientProfileRepository;
import com.example.jobsyserver.features.common.enums.ProjectStatus;
import com.example.jobsyserver.features.common.enums.UserRole;
import com.example.jobsyserver.features.common.exception.BadRequestException;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.freelancer.repository.FreelancerProfileRepository;
import com.example.jobsyserver.features.project.model.Project;
import com.example.jobsyserver.features.project.repository.ProjectRepository;
import com.example.jobsyserver.features.rating.dto.RatingResponseDto;
import com.example.jobsyserver.features.rating.mapper.RatingMapper;
import com.example.jobsyserver.features.rating.model.Rating;
import com.example.jobsyserver.features.rating.repository.RatingRepository;
import com.example.jobsyserver.features.rating.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepo;
    private final ProjectRepository projectRepo;
    private final FreelancerProfileRepository frRepo;
    private final ClientProfileRepository clRepo;
    private final RatingMapper ratingMapper;
    private final SecurityService security;

    @Override
    @Transactional
    public void rateProject(Long projectId, Long profileId, Integer score) {
        validateScore(score);
        var project = loadCompletedProject(projectId);
        var role = security.getCurrentUser().getRole();
        if (role == UserRole.FREELANCER) {
            rateByFreelancer(project, profileId, score);
        } else {
            rateByClient(project, profileId, score);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<RatingResponseDto> getRatings(Long projectId) {
        return ratingMapper.toDtoList(ratingRepo.findByProjectId(projectId));
    }

    private void validateScore(Integer score) {
        if (score == null || score < 1 || score > 5) {
            throw new BadRequestException("Оценка должна быть от 1 до 5");
        }
    }

    private Project loadCompletedProject(Long projectId) {
        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Проект"));
        if (project.getStatus() != ProjectStatus.COMPLETED) {
            throw new BadRequestException("Проект ещё не завершён");
        }
        return project;
    }

    private void rateByFreelancer(Project project, Long freelancerId, int score) {
        if (ratingRepo.existsByProjectIdAndRaterFreelancerId(project.getId(), freelancerId)) {
            throw new BadRequestException("Вы уже выставили оценку этому клиенту");
        }
        var fr = frRepo.findById(freelancerId)
                .orElseThrow(() -> new ResourceNotFoundException("Профиль фрилансера"));
        var client = project.getClient();
        if (client == null) {
            throw new BadRequestException("У проекта нет клиента для оценки");
        }
        var rating = new Rating();
        rating.setProject(project);
        rating.setRaterFreelancer(fr);
        rating.setTargetClient(client);
        rating.setScore(score);
        ratingRepo.save(rating);
        updateClientStats(client, score);
    }

    private void rateByClient(Project project, Long clientId, int score) {
        if (ratingRepo.existsByProjectIdAndRaterClientId(project.getId(), clientId)) {
            throw new BadRequestException("Вы уже выставили оценку этому фрилансеру");
        }
        var cl = clRepo.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Профиль клиента"));
        var fp = project.getAssignedFreelancer();
        if (fp == null) {
            throw new BadRequestException("У проекта нет исполнителя для оценки");
        }
        var rating = new Rating();
        rating.setProject(project);
        rating.setRaterClient(cl);
        rating.setTargetFreelancer(fp);
        rating.setScore(score);
        ratingRepo.save(rating);
        updateFreelancerStats(fp, score);
    }

    private void updateFreelancerStats(FreelancerProfile fp, int score) {
        int cnt = fp.getRatingCount();
        double avg = fp.getAverageRating();
        int newCnt = cnt + 1;
        double newAvg = (avg * cnt + score) / newCnt;

        fp.setRatingCount(newCnt);
        fp.setAverageRating(newAvg);
        frRepo.save(fp);
    }

    private void updateClientStats(ClientProfile cp, int score) {
        int cnt = cp.getRatingCount();
        double avg = cp.getAverageRating();
        int newCnt = cnt + 1;
        double newAvg = (avg * cnt + score) / newCnt;

        cp.setRatingCount(newCnt);
        cp.setAverageRating(newAvg);
        clRepo.save(cp);
    }
}