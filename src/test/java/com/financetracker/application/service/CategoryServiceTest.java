package com.financetracker.application.service;

import com.financetracker.domain.exception.DuplicateResourceException;
import com.financetracker.domain.exception.ResourceNotFoundException;
import com.financetracker.domain.model.Category;
import com.financetracker.domain.model.TransactionType;
import com.financetracker.domain.port.out.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CategoryService")
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryService(categoryRepository);
    }

    @Nested
    @DisplayName("createCategory")
    class CreateCategory {

        @Test
        @DisplayName("should create a category successfully")
        void shouldCreateCategory() {
            when(categoryRepository.existsByNameAndType("Food", TransactionType.EXPENSE))
                    .thenReturn(false);
            when(categoryRepository.save(any(Category.class)))
                    .thenAnswer(invocation -> {
                        Category cat = invocation.getArgument(0);
                        cat.setId(1L);
                        return cat;
                    });

            Category result = categoryService.createCategory(
                    "Food", TransactionType.EXPENSE, "🍔", "#FF6B6B");

            assertNotNull(result);
            assertEquals("Food", result.getName());
            assertEquals(TransactionType.EXPENSE, result.getType());
            verify(categoryRepository).save(any(Category.class));
        }

        @Test
        @DisplayName("should throw when category name already exists for same type")
        void shouldThrowOnDuplicate() {
            when(categoryRepository.existsByNameAndType("Food", TransactionType.EXPENSE))
                    .thenReturn(true);

            assertThrows(DuplicateResourceException.class,
                    () -> categoryService.createCategory(
                            "Food", TransactionType.EXPENSE, "🍔", "#FF6B6B"));

            verify(categoryRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("getCategoryById")
    class GetCategoryById {

        @Test
        @DisplayName("should return category when found")
        void shouldReturnCategory() {
            Category category = new Category(1L, "Food", TransactionType.EXPENSE, "🍔", "#FF6B6B");
            when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

            Category result = categoryService.getCategoryById(1L);

            assertEquals("Food", result.getName());
        }

        @Test
        @DisplayName("should throw when category not found")
        void shouldThrowWhenNotFound() {
            when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> categoryService.getCategoryById(99L));
        }
    }

    @Nested
    @DisplayName("getAllCategories")
    class GetAllCategories {

        @Test
        @DisplayName("should return all categories")
        void shouldReturnAll() {
            List<Category> categories = List.of(
                    new Category(1L, "Food", TransactionType.EXPENSE, "🍔", "#FF6B6B"),
                    new Category(2L, "Salary", TransactionType.INCOME, "💰", "#2ECC71")
            );
            when(categoryRepository.findAll()).thenReturn(categories);

            List<Category> result = categoryService.getAllCategories();

            assertEquals(2, result.size());
        }
    }

    @Nested
    @DisplayName("deleteCategory")
    class DeleteCategory {

        @Test
        @DisplayName("should delete category when exists")
        void shouldDelete() {
            when(categoryRepository.existsById(1L)).thenReturn(true);

            categoryService.deleteCategory(1L);

            verify(categoryRepository).deleteById(1L);
        }

        @Test
        @DisplayName("should throw when category to delete not found")
        void shouldThrowWhenNotFound() {
            when(categoryRepository.existsById(99L)).thenReturn(false);

            assertThrows(ResourceNotFoundException.class,
                    () -> categoryService.deleteCategory(99L));

            verify(categoryRepository, never()).deleteById(any());
        }
    }
}
