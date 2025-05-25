package com.example.jobsyserver.features.user.specification;

import com.example.jobsyserver.features.common.enums.UserRole;
import com.example.jobsyserver.features.user.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {

    public static Specification<User> emailContains(String email) {
        return (root, query, cb) ->
                email == null || email.isBlank() ? null :
                        cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }

    public static Specification<User> firstNameContains(String firstName) {
        return (root, query, cb) ->
                firstName == null || firstName.isBlank() ? null :
                        cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
    }

    public static Specification<User> lastNameContains(String lastName) {
        return (root, query, cb) ->
                lastName == null || lastName.isBlank() ? null :
                        cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
    }

    public static Specification<User> phoneContains(String phone) {
        return (root, query, cb) ->
                phone == null || phone.isBlank() ? null :
                        cb.like(root.get("phone"), "%" + phone + "%");
    }

    public static Specification<User> hasRole(UserRole role) {
        return (root, query, cb) ->
                role == null ? null : cb.equal(root.get("role"), role);
    }
}