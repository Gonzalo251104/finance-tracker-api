package com.financetracker.infrastructure.adapter.out.persistence.adapter;

import com.financetracker.domain.model.CategorySummary;
import com.financetracker.domain.model.PageResult;
import com.financetracker.domain.model.Transaction;
import com.financetracker.domain.model.TransactionType;
import com.financetracker.domain.port.out.TransactionRepository;
import com.financetracker.infrastructure.adapter.out.persistence.mapper.TransactionMapper;
import com.financetracker.infrastructure.adapter.out.persistence.repository.TransactionJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Persistence adapter implementing the TransactionRepository port.
 */
@Repository
public class TransactionPersistenceAdapter implements TransactionRepository {

    private final TransactionJpaRepository jpaRepository;
    private final TransactionMapper mapper;

    public TransactionPersistenceAdapter(TransactionJpaRepository jpaRepository,
                                         TransactionMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Transaction save(Transaction transaction) {
        var entity = mapper.toEntity(transaction);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Transaction> findByIdAndUserId(Long id, Long userId) {
        return jpaRepository.findByIdAndUserId(id, userId).map(mapper::toDomain);
    }

    @Override
    public List<Transaction> findByUserId(Long userId) {
        return jpaRepository.findByUserIdOrderByDateDesc(userId)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Transaction> findByUserIdAndFilters(Long userId, TransactionType type,
                                                     Long categoryId, LocalDate startDate,
                                                     LocalDate endDate) {
        return jpaRepository.findByFilters(userId, type, categoryId, startDate, endDate)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public PageResult<Transaction> findByUserIdAndFilters(Long userId, TransactionType type,
                                                           Long categoryId, LocalDate startDate,
                                                           LocalDate endDate, int page, int size,
                                                           String sortBy, String sortDirection) {
        Sort sort = Sort.by(
                "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC,
                sortBy != null ? sortBy : "date");
        var pageable = PageRequest.of(page, size, sort);
        var jpaPage = jpaRepository.findByFilters(userId, type, categoryId, startDate, endDate, pageable);
        var transactions = jpaPage.getContent().stream().map(mapper::toDomain).toList();
        return new PageResult<>(transactions, jpaPage.getNumber(), jpaPage.getSize(),
                jpaPage.getTotalElements());
    }

    @Override
    @Transactional
    public void deleteByIdAndUserId(Long id, Long userId) {
        jpaRepository.deleteByIdAndUserId(id, userId);
    }

    @Override
    public boolean existsByIdAndUserId(Long id, Long userId) {
        return jpaRepository.existsByIdAndUserId(id, userId);
    }

    @Override
    public BigDecimal sumAmountByUserIdAndTypeAndDateRange(Long userId, TransactionType type,
                                                            LocalDate startDate, LocalDate endDate) {
        return jpaRepository.sumAmountByUserIdAndType(userId, type, startDate, endDate);
    }

    @Override
    public long countByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return jpaRepository.countByUserIdAndDateRange(userId, startDate, endDate);
    }

    @Override
    public List<CategorySummary> findCategorySummaries(Long userId, TransactionType type,
                                                        LocalDate startDate, LocalDate endDate) {
        List<Object[]> raw = jpaRepository.findCategorySummaryRaw(userId, type, startDate, endDate);
        BigDecimal total = raw.stream()
                .map(r -> (BigDecimal) r[4])
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return raw.stream().map(r -> {
            BigDecimal amount = (BigDecimal) r[4];
            BigDecimal pct = total.compareTo(BigDecimal.ZERO) > 0
                    ? amount.multiply(BigDecimal.valueOf(100))
                            .divide(total, 2, java.math.RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;
            return new CategorySummary(
                    (Long) r[0], (String) r[1], (String) r[2], (String) r[3],
                    type, amount, (Long) r[5], pct);
        }).toList();
    }
}
