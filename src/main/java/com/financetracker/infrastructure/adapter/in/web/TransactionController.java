package com.financetracker.infrastructure.adapter.in.web;

import com.financetracker.application.service.TransactionService;
import com.financetracker.domain.model.FinancialSummary;
import com.financetracker.domain.model.Transaction;
import com.financetracker.domain.model.TransactionType;
import com.financetracker.infrastructure.adapter.in.web.dto.request.TransactionRequest;
import com.financetracker.infrastructure.adapter.in.web.dto.response.PageResponse;
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
    @Operation(summary = "Get all transactions with pagination, sorting, and optional filters")
    public ResponseEntity<PageResponse<TransactionResponse>> getTransactions(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        var result = transactionService.getTransactionsPaginated(
                user.userId(), type, categoryId, startDate, endDate,
                page, size, sortBy, sortDir);
        return ResponseEntity.ok(PageResponse.from(result, TransactionResponse::from));
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

    @GetMapping("/summary")
    @Operation(summary = "Get financial summary with totals and category breakdowns")
    public ResponseEntity<FinancialSummary> getFinancialSummary(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        var summary = transactionService.getFinancialSummary(
                user.userId(), startDate, endDate);
        return ResponseEntity.ok(summary);
    }
}
