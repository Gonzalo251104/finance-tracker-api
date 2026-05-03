package com.financetracker.application.service;

import com.financetracker.domain.exception.ResourceNotFoundException;
import com.financetracker.domain.model.FinancialSummary;
import com.financetracker.domain.model.Money;
import com.financetracker.domain.model.PageResult;
import com.financetracker.domain.model.Transaction;
import com.financetracker.domain.model.TransactionType;
import com.financetracker.domain.port.out.CategoryRepository;
import com.financetracker.domain.port.out.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Application service orchestrating transaction use cases.
 */
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    public Transaction createTransaction(BigDecimal amount, String description, LocalDate date,
                                         TransactionType type, Long categoryId, Long userId) {
        validateCategoryExists(categoryId);
        Transaction transaction = new Transaction(
                null, Money.of(amount), description, date, type, categoryId, userId);
        return transactionRepository.save(transaction);
    }

    public Transaction getTransactionById(Long id, Long userId) {
        return transactionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", id));
    }

    public List<Transaction> getTransactions(Long userId, TransactionType type,
                                              Long categoryId, LocalDate startDate,
                                              LocalDate endDate) {
        return transactionRepository.findByUserIdAndFilters(
                userId, type, categoryId, startDate, endDate);
    }

    public PageResult<Transaction> getTransactionsPaginated(Long userId, TransactionType type,
                                                              Long categoryId, LocalDate startDate,
                                                              LocalDate endDate, int page, int size,
                                                              String sortBy, String sortDirection) {
        return transactionRepository.findByUserIdAndFilters(
                userId, type, categoryId, startDate, endDate, page, size, sortBy, sortDirection);
    }

    public Transaction updateTransaction(Long id, Long userId, BigDecimal amount,
                                          String description, LocalDate date,
                                          TransactionType type, Long categoryId) {
        Transaction transaction = getTransactionById(id, userId);
        validateCategoryExists(categoryId);
        transaction.updateDetails(Money.of(amount), description, date, type, categoryId);
        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id, Long userId) {
        if (!transactionRepository.existsByIdAndUserId(id, userId)) {
            throw new ResourceNotFoundException("Transaction", "id", id);
        }
        transactionRepository.deleteByIdAndUserId(id, userId);
    }

    private void validateCategoryExists(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category", "id", categoryId);
        }
    }

    public FinancialSummary getFinancialSummary(Long userId, LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            startDate = LocalDate.now().withDayOfMonth(1);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        BigDecimal totalIncome = transactionRepository
                .sumAmountByUserIdAndTypeAndDateRange(userId, TransactionType.INCOME, startDate, endDate);
        BigDecimal totalExpenses = transactionRepository
                .sumAmountByUserIdAndTypeAndDateRange(userId, TransactionType.EXPENSE, startDate, endDate);
        long count = transactionRepository.countByUserIdAndDateRange(userId, startDate, endDate);

        var expenseCategories = transactionRepository
                .findCategorySummaries(userId, TransactionType.EXPENSE, startDate, endDate);
        var incomeCategories = transactionRepository
                .findCategorySummaries(userId, TransactionType.INCOME, startDate, endDate);

        return new FinancialSummary(startDate, endDate, totalIncome, totalExpenses,
                null, count, expenseCategories, incomeCategories);
    }
}
