package com.financetracker.domain.model;

import java.math.BigDecimal;

/**
 * Aggregated spending/income summary for a single category within a period.
 */
public record CategorySummary(
        Long categoryId,
        String categoryName,
        String categoryIcon,
        String categoryColor,
        TransactionType type,
        BigDecimal total,
        long transactionCount,
        BigDecimal percentage
) {}
