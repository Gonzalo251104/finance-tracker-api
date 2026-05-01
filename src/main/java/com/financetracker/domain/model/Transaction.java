package com.financetracker.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Domain entity representing a financial transaction.
 * This is a pure domain object with no framework dependencies.
 */
public class Transaction {

    private Long id;
    private Money amount;
    private String description;
    private LocalDate date;
    private TransactionType type;
    private Long categoryId;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Transaction() {
    }

    public Transaction(Long id, Money amount, String description, LocalDate date,
                       TransactionType type, Long categoryId, Long userId) {
        validate(amount, description, date, type, categoryId, userId);
        this.id = id;
        this.amount = amount;
        this.description = description.trim();
        this.date = date;
        this.type = type;
        this.categoryId = categoryId;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // --- Business rules ---

    public void updateDetails(Money amount, String description, LocalDate date,
                              TransactionType type, Long categoryId) {
        validate(amount, description, date, type, categoryId, this.userId);
        this.amount = amount;
        this.description = description.trim();
        this.date = date;
        this.type = type;
        this.categoryId = categoryId;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isIncome() {
        return TransactionType.INCOME.equals(this.type);
    }

    public boolean isExpense() {
        return TransactionType.EXPENSE.equals(this.type);
    }

    private void validate(Money amount, String description, LocalDate date,
                          TransactionType type, Long categoryId, Long userId) {
        if (amount == null || !amount.isPositive()) {
            throw new IllegalArgumentException("Transaction amount must be positive");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Transaction description must not be blank");
        }
        if (description.length() > 255) {
            throw new IllegalArgumentException("Transaction description must not exceed 255 characters");
        }
        if (date == null) {
            throw new IllegalArgumentException("Transaction date must not be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Transaction type must not be null");
        }
        if (categoryId == null) {
            throw new IllegalArgumentException("Transaction category must not be null");
        }
        if (userId == null) {
            throw new IllegalArgumentException("Transaction user must not be null");
        }
    }

    // --- Getters ---

    public Long getId() { return id; }
    public Money getAmount() { return amount; }
    public String getDescription() { return description; }
    public LocalDate getDate() { return date; }
    public TransactionType getType() { return type; }
    public Long getCategoryId() { return categoryId; }
    public Long getUserId() { return userId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // --- Setters for hydration from persistence ---

    public void setId(Long id) { this.id = id; }
    public void setAmount(Money amount) { this.amount = amount; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setType(TransactionType type) { this.type = type; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
