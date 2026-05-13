package com.financetracker.infrastructure.adapter.in.web.dto.response;

import com.financetracker.domain.model.User;

import java.time.LocalDateTime;

/**
 * Response DTO for user profile information.
 * Excludes sensitive data like password.
 */
public record UserProfileResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static UserProfileResponse from(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().name(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
