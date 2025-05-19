package com.example.jobsyserver.features.freelancer.repository;

import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FreelancerProfileRepository extends JpaRepository<FreelancerProfile, Long>, JpaSpecificationExecutor<FreelancerProfile> {
    Optional<FreelancerProfile> findByUser(User user);
    Optional<FreelancerProfile> findByUserEmail(String email);
}