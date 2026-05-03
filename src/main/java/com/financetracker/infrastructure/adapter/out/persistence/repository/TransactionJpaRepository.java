package com.financetracker.infrastructure.adapter.out.persistence.repository;

import com.financetracker.domain.model.TransactionType;
import com.financetracker.infrastructure.adapter.out.persistence.entity.TransactionJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for transactions.
 */
public interface TransactionJpaRepository extends JpaRepository<TransactionJpaEntity, Long> {

    Optional<TransactionJpaEntity> findByIdAndUserId(Long id, Long userId);

    List<TransactionJpaEntity> findByUserIdOrderByDateDesc(Long userId);

    @Query("""
            SELECT t FROM TransactionJpaEntity t
            WHERE t.userId = :userId
            AND (:type IS NULL OR t.type = :type)
            AND (:categoryId IS NULL OR t.categoryId = :categoryId)
            AND (:startDate IS NULL OR t.date >= :startDate)
            AND (:endDate IS NULL OR t.date <= :endDate)
            ORDER BY t.date DESC
            """)
    List<TransactionJpaEntity> findByFilters(
            @Param("userId") Long userId,
            @Param("type") TransactionType type,
            @Param("categoryId") Long categoryId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("""
            SELECT t FROM TransactionJpaEntity t
            WHERE t.userId = :userId
            AND (:type IS NULL OR t.type = :type)
            AND (:categoryId IS NULL OR t.categoryId = :categoryId)
            AND (:startDate IS NULL OR t.date >= :startDate)
            AND (:endDate IS NULL OR t.date <= :endDate)
            """)
    Page<TransactionJpaEntity> findByFilters(
            @Param("userId") Long userId,
            @Param("type") TransactionType type,
            @Param("categoryId") Long categoryId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);

    void deleteByIdAndUserId(Long id, Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);

    @Query("""
            SELECT COALESCE(SUM(t.amount), 0) FROM TransactionJpaEntity t
            WHERE t.userId = :userId AND t.type = :type
            AND t.date >= :startDate AND t.date <= :endDate
            """)
    java.math.BigDecimal sumAmountByUserIdAndType(
            @Param("userId") Long userId,
            @Param("type") TransactionType type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("""
            SELECT COUNT(t) FROM TransactionJpaEntity t
            WHERE t.userId = :userId
            AND t.date >= :startDate AND t.date <= :endDate
            """)
    long countByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("""
            SELECT t.categoryId, c.name, c.icon, c.color,
                   SUM(t.amount), COUNT(t)
            FROM TransactionJpaEntity t
            JOIN CategoryJpaEntity c ON t.categoryId = c.id
            WHERE t.userId = :userId AND t.type = :type
            AND t.date >= :startDate AND t.date <= :endDate
            GROUP BY t.categoryId, c.name, c.icon, c.color
            ORDER BY SUM(t.amount) DESC
            """)
    List<Object[]> findCategorySummaryRaw(
            @Param("userId") Long userId,
            @Param("type") TransactionType type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
