package com.financetracker.infrastructure.adapter.out.persistence.mapper;

import com.financetracker.domain.model.Money;
import com.financetracker.domain.model.Transaction;
import com.financetracker.infrastructure.adapter.out.persistence.entity.TransactionJpaEntity;
import org.springframework.stereotype.Component;

/**
 * Maps between Transaction domain model and TransactionJpaEntity.
 */
@Component
public class TransactionMapper {

    public Transaction toDomain(TransactionJpaEntity entity) {
        Transaction transaction = new Transaction(
                entity.getId(),
                Money.of(entity.getAmount()),
                entity.getDescription(),
                entity.getDate(),
                entity.getType(),
                entity.getCategoryId(),
                entity.getUserId()
        );
        transaction.setCreatedAt(entity.getCreatedAt());
        transaction.setUpdatedAt(entity.getUpdatedAt());
        return transaction;
    }

    public TransactionJpaEntity toEntity(Transaction domain) {
        TransactionJpaEntity entity = new TransactionJpaEntity();
        entity.setId(domain.getId());
        entity.setAmount(domain.getAmount().amount());
        entity.setDescription(domain.getDescription());
        entity.setDate(domain.getDate());
        entity.setType(domain.getType());
        entity.setCategoryId(domain.getCategoryId());
        entity.setUserId(domain.getUserId());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }
}
