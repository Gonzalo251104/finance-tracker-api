package com.financetracker.infrastructure.adapter.out.persistence.repository;

import com.financetracker.domain.model.TransactionType;
import com.financetracker.infrastructure.adapter.out.persistence.entity.CategoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for categories.
 */
public interface CategoryJpaRepository extends JpaRepository<CategoryJpaEntity, Long> {

    List<CategoryJpaEntity> findByType(TransactionType type);

    boolean existsByNameAndType(String name, TransactionType type);
}
