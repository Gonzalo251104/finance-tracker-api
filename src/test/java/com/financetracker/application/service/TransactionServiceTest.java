package com.financetracker.application.service;

import com.financetracker.domain.exception.ResourceNotFoundException;
import com.financetracker.domain.model.Money;
import com.financetracker.domain.model.Transaction;
import com.financetracker.domain.model.TransactionType;
import com.financetracker.domain.port.out.CategoryRepository;
import com.financetracker.domain.port.out.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TransactionService")
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(transactionRepository, categoryRepository);
    }

    @Nested
    @DisplayName("createTransaction")
    class CreateTransaction {

        @Test
        @DisplayName("should create a transaction when category exists")
        void shouldCreate() {
            when(categoryRepository.existsById(1L)).thenReturn(true);
            when(transactionRepository.save(any(Transaction.class)))
                    .thenAnswer(invocation -> {
                        Transaction tx = invocation.getArgument(0);
                        tx.setId(1L);
                        return tx;
                    });

            Transaction result = transactionService.createTransaction(
                    new BigDecimal("50.00"), "Lunch", LocalDate.now(),
                    TransactionType.EXPENSE, 1L, 1L);

            assertNotNull(result);
            assertEquals(new BigDecimal("50.00"), result.getAmount().amount());
            assertEquals("Lunch", result.getDescription());
            verify(transactionRepository).save(any(Transaction.class));
        }

        @Test
        @DisplayName("should throw when category does not exist")
        void shouldThrowOnMissingCategory() {
            when(categoryRepository.existsById(99L)).thenReturn(false);

            assertThrows(ResourceNotFoundException.class,
                    () -> transactionService.createTransaction(
                            new BigDecimal("50.00"), "Lunch", LocalDate.now(),
                            TransactionType.EXPENSE, 99L, 1L));

            verify(transactionRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("getTransactionById")
    class GetTransactionById {

        @Test
        @DisplayName("should return transaction when found")
        void shouldReturn() {
            Transaction tx = new Transaction(1L, Money.of(50.00), "Lunch",
                    LocalDate.now(), TransactionType.EXPENSE, 1L, 1L);
            when(transactionRepository.findByIdAndUserId(1L, 1L))
                    .thenReturn(Optional.of(tx));

            Transaction result = transactionService.getTransactionById(1L, 1L);

            assertEquals("Lunch", result.getDescription());
        }

        @Test
        @DisplayName("should throw when not found")
        void shouldThrowWhenNotFound() {
            when(transactionRepository.findByIdAndUserId(99L, 1L))
                    .thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> transactionService.getTransactionById(99L, 1L));
        }
    }

    @Nested
    @DisplayName("deleteTransaction")
    class DeleteTransaction {

        @Test
        @DisplayName("should delete when transaction exists")
        void shouldDelete() {
            when(transactionRepository.existsByIdAndUserId(1L, 1L)).thenReturn(true);

            transactionService.deleteTransaction(1L, 1L);

            verify(transactionRepository).deleteByIdAndUserId(1L, 1L);
        }

        @Test
        @DisplayName("should throw when transaction not found")
        void shouldThrowWhenNotFound() {
            when(transactionRepository.existsByIdAndUserId(99L, 1L)).thenReturn(false);

            assertThrows(ResourceNotFoundException.class,
                    () -> transactionService.deleteTransaction(99L, 1L));
        }
    }
}
