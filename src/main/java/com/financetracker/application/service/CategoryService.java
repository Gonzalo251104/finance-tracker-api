package com.financetracker.application.service;

import com.financetracker.domain.exception.DuplicateResourceException;
import com.financetracker.domain.exception.ResourceNotFoundException;
import com.financetracker.domain.model.Category;
import com.financetracker.domain.model.TransactionType;
import com.financetracker.domain.port.out.CategoryRepository;

import java.util.List;

/**
 * Application service orchestrating category use cases.
 * This layer coordinates domain objects and repository ports.
 */
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(String name, TransactionType type, String icon, String color) {
        if (categoryRepository.existsByNameAndType(name, type)) {
            throw new DuplicateResourceException(
                    String.format("Category '%s' already exists for type %s", name, type));
        }
        Category category = new Category(null, name, type, icon, color);
        return categoryRepository.save(category);
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getCategoriesByType(TransactionType type) {
        return categoryRepository.findByType(type);
    }

    public Category updateCategory(Long id, String name, String icon, String color) {
        Category category = getCategoryById(id);
        category.updateDetails(name, icon, color);
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category", "id", id);
        }
        categoryRepository.deleteById(id);
    }
}
