package com.financetracker.infrastructure.adapter.in.web;

import com.financetracker.application.service.AuthService;
import com.financetracker.domain.model.User;
import com.financetracker.infrastructure.adapter.in.web.dto.request.UpdateProfileRequest;
import com.financetracker.infrastructure.adapter.in.web.dto.response.UserProfileResponse;
import com.financetracker.infrastructure.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for user profile operations.
 */
@RestController
@RequestMapping("/v1/users")
@Tag(name = "User Profile", description = "View and update user profile")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user's profile")
    public ResponseEntity<UserProfileResponse> getProfile(
            @AuthenticationPrincipal AuthenticatedUser user) {
        User profile = authService.getUserProfile(user.userId());
        return ResponseEntity.ok(UserProfileResponse.from(profile));
    }

    @PutMapping("/me")
    @Operation(summary = "Update current user's profile")
    public ResponseEntity<UserProfileResponse> updateProfile(
            @AuthenticationPrincipal AuthenticatedUser user,
            @Valid @RequestBody UpdateProfileRequest request) {
        User updated = authService.updateUserProfile(
                user.userId(), request.firstName(), request.lastName());
        return ResponseEntity.ok(UserProfileResponse.from(updated));
    }
}
