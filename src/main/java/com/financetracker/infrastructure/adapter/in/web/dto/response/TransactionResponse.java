package com.financetracker.infrastructure.adapter.in.web.dto.response;

import com.financetracker.domain.model.Transaction;
import com.financetracker.domain.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO for transaction data.
 */
public record TransactionResponse(
        Long id,
        BigDecimal amount,
        String description,
        LocalDate date,
        TransactionType type,
        Long categoryId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static TransactionResponse from(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getAmount().amount(),
                transaction.getDescription(),
                transaction.getDate(),
                transaction.getType(),
                transaction.getCategoryId(),
                transaction.getCreatedAt(),
                transaction.getUpdatedAt()
        );
    }
}
