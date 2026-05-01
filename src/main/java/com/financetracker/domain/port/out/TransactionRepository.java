package com.financetracker.domain.port.out;

import com.financetracker.domain.model.Transaction;
import com.financetracker.domain.model.TransactionType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Output port for transaction persistence.
 */
public interface TransactionRepository {

    Transaction save(Transaction transaction);

    Optional<Transaction> findByIdAndUserId(Long id, Long userId);

    List<Transaction> findByUserId(Long userId);

    List<Transaction> findByUserIdAndFilters(Long userId, TransactionType type,
                                              Long categoryId, LocalDate startDate,
                                              LocalDate endDate);

    void deleteByIdAndUserId(Long id, Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);
}
