package com.financetracker.infrastructure.adapter.out.persistence.mapper;

import com.financetracker.domain.model.Category;
import com.financetracker.infrastructure.adapter.out.persistence.entity.CategoryJpaEntity;
import org.springframework.stereotype.Component;

/**
 * Maps between Category domain model and CategoryJpaEntity.
 * This mapper is the bridge between the domain and persistence layers.
 */
@Component
public class CategoryMapper {

    public Category toDomain(CategoryJpaEntity entity) {
        Category category = new Category(
                entity.getId(),
                entity.getName(),
                entity.getType(),
                entity.getIcon(),
                entity.getColor()
        );
        category.setCreatedAt(entity.getCreatedAt());
        category.setUpdatedAt(entity.getUpdatedAt());
        return category;
    }

    public CategoryJpaEntity toEntity(Category domain) {
        CategoryJpaEntity entity = new CategoryJpaEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setType(domain.getType());
        entity.setIcon(domain.getIcon());
        entity.setColor(domain.getColor());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }
}
