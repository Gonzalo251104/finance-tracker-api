package com.financetracker.infrastructure.adapter.in.web;

import com.financetracker.application.service.TransactionService;
import com.financetracker.domain.model.Transaction;
import com.financetracker.domain.model.TransactionType;
import com.financetracker.infrastructure.adapter.in.web.dto.request.TransactionRequest;
import com.financetracker.infrastructure.adapter.in.web.dto.response.TransactionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for transaction operations.
 * NOTE: userId is hardcoded to 1L for now. Will be replaced with
 * authenticated user extraction once Spring Security + JWT is configured.
 */
@RestController
@RequestMapping("/v1/transactions")
@Tag(name = "Transactions", description = "Manage financial transactions")
public class TransactionController {

    private static final Long TEMP_USER_ID = 1L;

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @Operation(summary = "Create a new transaction")
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody TransactionRequest request) {
        Transaction transaction = transactionService.createTransaction(
                request.amount(), request.description(), request.date(),
                request.type(), request.categoryId(), TEMP_USER_ID);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TransactionResponse.from(transaction));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a transaction by ID")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long id) {
        Transaction transaction = transactionService.getTransactionById(id, TEMP_USER_ID);
        return ResponseEntity.ok(TransactionResponse.from(transaction));
    }

    @GetMapping
    @Operation(summary = "Get all transactions with optional filters")
    public ResponseEntity<List<TransactionResponse>> getTransactions(
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        List<Transaction> transactions = transactionService.getTransactions(
                TEMP_USER_ID, type, categoryId, startDate, endDate);
        List<TransactionResponse> response = transactions.stream()
                .map(TransactionResponse::from).toList();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing transaction")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @PathVariable Long id, @Valid @RequestBody TransactionRequest request) {
        Transaction updated = transactionService.updateTransaction(
                id, TEMP_USER_ID, request.amount(), request.description(),
                request.date(), request.type(), request.categoryId());
        return ResponseEntity.ok(TransactionResponse.from(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a transaction by ID")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id, TEMP_USER_ID);
        return ResponseEntity.noContent().build();
    }
}
