package com.financetracker.infrastructure.adapter.out.persistence.adapter;

import com.financetracker.domain.model.Category;
import com.financetracker.domain.model.TransactionType;
import com.financetracker.domain.port.out.CategoryRepository;
import com.financetracker.infrastructure.adapter.out.persistence.entity.CategoryJpaEntity;
import com.financetracker.infrastructure.adapter.out.persistence.mapper.CategoryMapper;
import com.financetracker.infrastructure.adapter.out.persistence.repository.CategoryJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Persistence adapter implementing the CategoryRepository port.
 * This is the bridge between the domain and Spring Data JPA.
 */
@Repository
public class CategoryPersistenceAdapter implements CategoryRepository {

    private final CategoryJpaRepository jpaRepository;
    private final CategoryMapper mapper;

    public CategoryPersistenceAdapter(CategoryJpaRepository jpaRepository, CategoryMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Category save(Category category) {
        CategoryJpaEntity entity = mapper.toEntity(category);
        CategoryJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Category> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Category> findByType(TransactionType type) {
        return jpaRepository.findByType(type).stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existsByNameAndType(String name, TransactionType type) {
        return jpaRepository.existsByNameAndType(name, type);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
}
