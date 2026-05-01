package com.financetracker.domain.exception;

/**
 * Thrown when a duplicate resource is detected.
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
