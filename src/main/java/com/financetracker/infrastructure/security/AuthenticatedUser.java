package com.financetracker.infrastructure.security;

/**
 * Represents the authenticated user extracted from the JWT token.
 * Stored as the principal in the SecurityContext.
 */
public record AuthenticatedUser(Long userId, String email) {
}
