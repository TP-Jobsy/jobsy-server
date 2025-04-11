package com.example.jobsyserver.repository;

import com.example.jobsyserver.model.ClientProfile;
import com.example.jobsyserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientProfileRepository extends JpaRepository<ClientProfile, Long> {
    Optional<ClientProfile> findByUser(User user);
}