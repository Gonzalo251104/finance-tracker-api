package com.financetracker.infrastructure.adapter.in.web.dto.request;

import com.financetracker.domain.model.TransactionType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request DTO for creating or updating a transaction.
 */
public record TransactionRequest(
        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
        BigDecimal amount,

        @NotBlank(message = "Description is required")
        @Size(max = 255, message = "Description must not exceed 255 characters")
        String description,

        @NotNull(message = "Date is required")
        LocalDate date,

        @NotNull(message = "Type is required")
        TransactionType type,

        @NotNull(message = "Category ID is required")
        Long categoryId
) {}
