package com.financetracker.domain.model;

import java.time.LocalDateTime;

/**
 * Domain entity representing a transaction category.
 * This is a pure domain object with no framework dependencies.
 */
public class Category {

    private Long id;
    private String name;
    private TransactionType type;
    private String icon;
    private String color;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Category() {
    }

    public Category(Long id, String name, TransactionType type, String icon, String color) {
        setName(name);
        this.id = id;
        this.type = type;
        this.icon = icon;
        this.color = color;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // --- Business rules ---

    public void updateDetails(String name, String icon, String color) {
        setName(name);
        this.icon = icon;
        this.color = color;
        this.updatedAt = LocalDateTime.now();
    }

    private void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Category name must not be blank");
        }
        if (name.length() > 50) {
            throw new IllegalArgumentException("Category name must not exceed 50 characters");
        }
        this.name = name.trim();
    }

    // --- Getters ---

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TransactionType getType() {
        return type;
    }

    public String getIcon() {
        return icon;
    }

    public String getColor() {
        return color;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // --- Setters for hydration from persistence ---

    public void setId(Long id) {
        this.id = id;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
