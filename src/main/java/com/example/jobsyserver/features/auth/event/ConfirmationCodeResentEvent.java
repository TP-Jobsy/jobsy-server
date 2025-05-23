package com.example.jobsyserver.features.auth.event;

import com.example.jobsyserver.features.common.enums.ConfirmationAction;
import com.example.jobsyserver.features.user.model.User;

public record ConfirmationCodeResentEvent(User user, String confirmationCode, ConfirmationAction action) {}
