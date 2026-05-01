package com.financetracker.infrastructure.adapter.in.web.dto.response;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        UserInfo user
) {
    public AuthResponse(String accessToken, String refreshToken, UserInfo user) {
        this(accessToken, refreshToken, "Bearer", user);
    }

    public record UserInfo(
            Long id,
            String firstName,
            String lastName,
            String email,
            String role
    ) {}
}
