package com.example.jobsyserver.features.user.repository;

import com.example.jobsyserver.features.common.enums.UserRole;
import com.example.jobsyserver.features.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByRole(UserRole userRole);
    Optional<User> findByIdAndRole(Long id, UserRole role);
}
