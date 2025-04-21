package com.example.jobsyserver.repository;

import com.example.jobsyserver.model.FreelancerProfile;
import com.example.jobsyserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FreelancerProfileRepository extends JpaRepository<FreelancerProfile, Long> {
    Optional<FreelancerProfile> findByUser(User user);
    Optional<FreelancerProfile> findByUserEmail(String email);
}