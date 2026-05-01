package com.financetracker.infrastructure.adapter.in.web;

import com.financetracker.application.service.TransactionService;
import com.financetracker.domain.model.Transaction;
import com.financetracker.domain.model.TransactionType;
import com.financetracker.infrastructure.adapter.in.web.dto.request.TransactionRequest;
import com.financetracker.infrastructure.adapter.in.web.dto.response.TransactionResponse;
import com.financetracker.infrastructure.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for transaction operations.
 * Uses the authenticated user from the JWT token.
 */
@RestController
@RequestMapping("/v1/transactions")
@Tag(name = "Transactions", description = "Manage financial transactions")
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @Operation(summary = "Create a new transaction")
    public ResponseEntity<TransactionResponse> createTransaction(
            @AuthenticationPrincipal AuthenticatedUser user,
            @Valid @RequestBody TransactionRequest request) {
        Transaction transaction = transactionService.createTransaction(
                request.amount(), request.description(), request.date(),
                request.type(), request.categoryId(), user.userId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TransactionResponse.from(transaction));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a transaction by ID")
    public ResponseEntity<TransactionResponse> getTransactionById(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable Long id) {
        Transaction transaction = transactionService.getTransactionById(id, user.userId());
        return ResponseEntity.ok(TransactionResponse.from(transaction));
    }

    @GetMapping
    @Operation(summary = "Get all transactions with optional filters")
    public ResponseEntity<List<TransactionResponse>> getTransactions(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        List<Transaction> transactions = transactionService.getTransactions(
                user.userId(), type, categoryId, startDate, endDate);
        List<TransactionResponse> response = transactions.stream()
                .map(TransactionResponse::from).toList();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing transaction")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequest request) {
        Transaction updated = transactionService.updateTransaction(
                id, user.userId(), request.amount(), request.description(),
                request.date(), request.type(), request.categoryId());
        return ResponseEntity.ok(TransactionResponse.from(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a transaction by ID")
    public ResponseEntity<Void> deleteTransaction(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable Long id) {
        transactionService.deleteTransaction(id, user.userId());
        return ResponseEntity.noContent().build();
    }
}
