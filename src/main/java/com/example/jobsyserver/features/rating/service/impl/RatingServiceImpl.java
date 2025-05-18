package com.example.jobsyserver.features.rating.service.impl;

import com.example.jobsyserver.features.client.model.ClientProfile;
import com.example.jobsyserver.features.client.repository.ClientProfileRepository;
import com.example.jobsyserver.features.common.enums.ProjectStatus;
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

    @Override
    @Transactional
    public void rateProject(Long projectId, Long profileId, Integer score) {
        validateScore(score);
        Project project = loadCompletedProject(projectId);
        Rating rating = buildRating(project, profileId, score);
        ratingRepo.save(rating);
        if (rating.getTargetFreelancer() != null) {
            updateFreelancerStats(rating.getTargetFreelancer(), score);
        } else {
            updateClientStats(rating.getTargetClient(), score);
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

    private Rating buildRating(Project project, Long profileId, Integer score) {
        Rating rating = new Rating();
        rating.setProject(project);
        rating.setScore(score);

        return frRepo.findById(profileId)
                .map(fr -> {
                    if (ratingRepo.existsByProjectIdAndRaterFreelancerId(project.getId(), fr.getId())) {
                        throw new BadRequestException("Вы уже выставили оценку этому клиенту");
                    }
                    rating.setRaterFreelancer(fr);
                    ClientProfile client = project.getClient();
                    if (client == null) {
                        throw new BadRequestException("У проекта нет клиента для оценки");
                    }
                    rating.setTargetClient(client);
                    return rating;
                })
                .orElseGet(() -> {
                    ClientProfile cl = clRepo.findById(profileId)
                            .orElseThrow(() -> new ResourceNotFoundException("Профиль оценщика"));
                    if (ratingRepo.existsByProjectIdAndRaterClientId(project.getId(), cl.getId())) {
                        throw new BadRequestException("Вы уже выставили оценку этому фрилансеру");
                    }
                    rating.setRaterClient(cl);

                    FreelancerProfile fp = project.getAssignedFreelancer();
                    if (fp == null) {
                        throw new BadRequestException("У проекта нет исполнителя для оценки");
                    }
                    rating.setTargetFreelancer(fp);
                    return rating;
                });
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