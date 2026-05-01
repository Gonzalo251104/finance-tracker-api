package com.financetracker.infrastructure.adapter.in.web.dto.request;

import com.financetracker.domain.model.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating or updating a category.
 */
public record CategoryRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 50, message = "Name must not exceed 50 characters")
        String name,

        @NotNull(message = "Type is required")
        TransactionType type,

        @Size(max = 50, message = "Icon must not exceed 50 characters")
        String icon,

        @Size(max = 7, message = "Color must not exceed 7 characters")
        String color
) {}
