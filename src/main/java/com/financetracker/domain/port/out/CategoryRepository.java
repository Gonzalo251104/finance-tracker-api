package com.financetracker.domain.port.out;

import com.financetracker.domain.model.Category;
import com.financetracker.domain.model.TransactionType;

import java.util.List;
import java.util.Optional;

/**
 * Output port for category persistence.
 * The domain defines WHAT it needs; the infrastructure decides HOW to do it.
 */
public interface CategoryRepository {

    Category save(Category category);

    Optional<Category> findById(Long id);

    List<Category> findAll();

    List<Category> findByType(TransactionType type);

    boolean existsByNameAndType(String name, TransactionType type);

    void deleteById(Long id);

    boolean existsById(Long id);
}
