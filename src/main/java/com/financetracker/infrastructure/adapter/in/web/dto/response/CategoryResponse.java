package com.financetracker.infrastructure.adapter.in.web.dto.response;

import com.financetracker.domain.model.Category;
import com.financetracker.domain.model.TransactionType;

import java.time.LocalDateTime;

/**
 * Response DTO for category data.
 */
public record CategoryResponse(
        Long id,
        String name,
        TransactionType type,
        String icon,
        String color,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getType(),
                category.getIcon(),
                category.getColor(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}
