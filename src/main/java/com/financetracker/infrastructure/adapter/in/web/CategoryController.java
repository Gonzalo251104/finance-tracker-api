package com.financetracker.infrastructure.adapter.in.web;

import com.financetracker.application.service.CategoryService;
import com.financetracker.domain.model.Category;
import com.financetracker.domain.model.TransactionType;
import com.financetracker.infrastructure.adapter.in.web.dto.request.CategoryRequest;
import com.financetracker.infrastructure.adapter.in.web.dto.response.CategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for category operations.
 * This is a driving adapter in hexagonal architecture.
 */
@RestController
@RequestMapping("/v1/categories")
@Tag(name = "Categories", description = "Manage transaction categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @Operation(summary = "Create a new category")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        Category category = categoryService.createCategory(
                request.name(), request.type(), request.icon(), request.color());
        return ResponseEntity.status(HttpStatus.CREATED).body(CategoryResponse.from(category));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a category by ID")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(CategoryResponse.from(category));
    }

    @GetMapping
    @Operation(summary = "Get all categories, optionally filtered by type")
    public ResponseEntity<List<CategoryResponse>> getCategories(
            @RequestParam(required = false) TransactionType type) {
        List<Category> categories = (type != null)
                ? categoryService.getCategoriesByType(type)
                : categoryService.getAllCategories();
        List<CategoryResponse> response = categories.stream()
                .map(CategoryResponse::from).toList();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing category")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        Category updated = categoryService.updateCategory(
                id, request.name(), request.icon(), request.color());
        return ResponseEntity.ok(CategoryResponse.from(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category by ID")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
