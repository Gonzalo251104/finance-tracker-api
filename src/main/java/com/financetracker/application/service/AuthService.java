package com.financetracker.application.service;

import com.financetracker.domain.exception.DuplicateResourceException;
import com.financetracker.domain.exception.ResourceNotFoundException;
import com.financetracker.domain.model.User;
import com.financetracker.domain.model.UserRole;
import com.financetracker.domain.port.out.UserRepository;

/**
 * Application service for authentication use cases.
 * Framework-agnostic — password encoding is injected via functional interface.
 */
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String firstName, String lastName, String email, String rawPassword) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException(
                    String.format("User with email '%s' already exists", email));
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(null, firstName, lastName, email, encodedPassword, UserRole.USER);
        return userRepository.save(user);
    }

    public User authenticate(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return user;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    /**
     * Functional interface to keep the application layer framework-agnostic.
     * The infrastructure layer provides the implementation (BCrypt).
     */
    public interface PasswordEncoder {
        String encode(String rawPassword);
        boolean matches(String rawPassword, String encodedPassword);
    }
}
