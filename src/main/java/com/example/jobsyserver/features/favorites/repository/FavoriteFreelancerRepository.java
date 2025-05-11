package com.example.jobsyserver.features.favorites.repository;

import com.example.jobsyserver.features.favorites.model.FavoriteFreelancer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteFreelancerRepository extends JpaRepository<FavoriteFreelancer, Long> {
    List<FavoriteFreelancer> findByClientId(Long clientId);
    void deleteByClientIdAndFreelancerId(Long clientId, Long freelancerId);
    boolean existsByClientIdAndFreelancerId(Long clientId, Long freelancerId);

}
