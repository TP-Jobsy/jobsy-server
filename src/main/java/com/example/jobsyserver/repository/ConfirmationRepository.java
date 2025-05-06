package com.example.jobsyserver.repository;

import com.example.jobsyserver.enums.ConfirmationAction;
import com.example.jobsyserver.model.Confirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConfirmationRepository extends JpaRepository<Confirmation, Long> {
    List<Confirmation> findAllByActionAndUsedFalseAndExpiresAtBefore(ConfirmationAction action, LocalDateTime before);
    Optional<Confirmation> findFirstByUserEmailAndActionAndUsedFalseOrderByExpiresAtDesc(
            String email, ConfirmationAction action);
}
