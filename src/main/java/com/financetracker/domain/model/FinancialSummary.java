package com.financetracker.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Complete financial summary for a user within a date range.
 * Provides totals, balance, and per-category breakdowns.
 */
public record FinancialSummary(
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal totalIncome,
        BigDecimal totalExpenses,
        BigDecimal netBalance,
        long transactionCount,
        List<CategorySummary> topExpenseCategories,
        List<CategorySummary> topIncomeCategories
) {
    public FinancialSummary {
        netBalance = totalIncome.subtract(totalExpenses);
    }
}
