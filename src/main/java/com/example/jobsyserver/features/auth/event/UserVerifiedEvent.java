package com.example.jobsyserver.features.auth.event;

import com.example.jobsyserver.features.user.model.User;

public record UserVerifiedEvent(User user) {
}
