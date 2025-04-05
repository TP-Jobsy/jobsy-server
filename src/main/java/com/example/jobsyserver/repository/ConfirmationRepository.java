package com.example.jobsyserver.repository;

import com.example.jobsyserver.enums.ConfirmationAction;
import com.example.jobsyserver.model.Confirmation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmationRepository extends JpaRepository<Confirmation, Long> {
    Optional<Confirmation> findByUserEmailAndActionAndUsedFalse(String email, ConfirmationAction action);
}
