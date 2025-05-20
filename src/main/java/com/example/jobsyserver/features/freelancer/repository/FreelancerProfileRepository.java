package com.example.jobsyserver.features.freelancer.repository;

import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.user.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FreelancerProfileRepository extends JpaRepository<FreelancerProfile, Long>, JpaSpecificationExecutor<FreelancerProfile> {
    @EntityGraph(attributePaths = {"user", "skills"})
    Optional<FreelancerProfile> findByUser(User user);
    @EntityGraph(attributePaths = {"user","skills"})
    Optional<FreelancerProfile> findByUserEmail(String email);
    @EntityGraph(attributePaths = {"user","skills"})
    Optional<FreelancerProfile> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"user","skills"})
    List<FreelancerProfile> findAll();
}