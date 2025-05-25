package com.example.jobsyserver.features.user.repository;

import com.example.jobsyserver.features.common.enums.UserRole;
import com.example.jobsyserver.features.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRole(UserRole userRole);

    Optional<User> findByIdAndRole(Long id, UserRole role);

    Optional<User> findByEmailAndRole(String email, UserRole role);

    @Query("""
                SELECT u FROM User u
                WHERE (:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))
                  AND (:firstName IS NULL OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')))
                  AND (:lastName IS NULL OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :lastName, '%')))
                  AND (:phone IS NULL OR u.phone LIKE CONCAT('%', :phone, '%'))
                  AND (:role IS NULL OR u.role = :role)
            """)
    Page<User> searchUsersAdmin(
            @Param("email") String email,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("phone") String phone,
            @Param("role") UserRole role,
            Pageable pageable
    );
}
