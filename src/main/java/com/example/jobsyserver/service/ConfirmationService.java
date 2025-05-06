package com.example.jobsyserver.service;

import com.example.jobsyserver.enums.ConfirmationAction;
import com.example.jobsyserver.model.Confirmation;
import com.example.jobsyserver.model.User;

import java.util.List;
import java.util.Optional;

public interface ConfirmationService {
    Confirmation createConfirmationFor(User user, ConfirmationAction action);
    Confirmation validateAndUse(String email, String code, ConfirmationAction action);
    List<Confirmation> deleteExpired(ConfirmationAction action);
    Optional<Confirmation> findActiveByEmail(String email, ConfirmationAction action);
}
