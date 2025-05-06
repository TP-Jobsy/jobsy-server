package com.example.jobsyserver.event;

import com.example.jobsyserver.model.User;

public record ConfirmationCodeResentEvent(User user, String confirmationCode) {}
