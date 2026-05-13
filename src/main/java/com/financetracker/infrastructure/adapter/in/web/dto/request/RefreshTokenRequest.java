package com.financetracker.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for refreshing an access token.
 */
public record RefreshTokenRequest(
        @NotBlank(message = "Refresh token is required")
        String refreshToken
) {}
