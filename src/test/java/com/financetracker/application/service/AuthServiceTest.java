package com.financetracker.application.service;

import com.financetracker.domain.exception.DuplicateResourceException;
import com.financetracker.domain.exception.ResourceNotFoundException;
import com.financetracker.domain.model.User;
import com.financetracker.domain.model.UserRole;
import com.financetracker.domain.port.out.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    private AuthService authService;

    private final AuthService.PasswordEncoder passwordEncoder = new AuthService.PasswordEncoder() {
        @Override
        public String encode(String rawPassword) {
            return "encoded_" + rawPassword;
        }

        @Override
        public boolean matches(String rawPassword, String encodedPassword) {
            return encodedPassword.equals("encoded_" + rawPassword);
        }
    };

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, passwordEncoder);
    }

    @Nested
    @DisplayName("register")
    class Register {

        @Test
        @DisplayName("should register a new user successfully")
        void shouldRegister() {
            when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
                User user = invocation.getArgument(0);
                user.setId(1L);
                return user;
            });

            User result = authService.register("John", "Doe", "john@example.com", "password123");

            assertNotNull(result);
            assertEquals("john@example.com", result.getEmail());
            assertEquals("encoded_password123", result.getPassword());
            assertEquals(UserRole.USER, result.getRole());
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("should throw when email already exists")
        void shouldThrowOnDuplicateEmail() {
            when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

            assertThrows(DuplicateResourceException.class,
                    () -> authService.register("John", "Doe", "john@example.com", "password123"));

            verify(userRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("authenticate")
    class Authenticate {

        @Test
        @DisplayName("should authenticate with valid credentials")
        void shouldAuthenticate() {
            User user = new User(1L, "John", "Doe", "john@example.com",
                    "encoded_password123", UserRole.USER);
            when(userRepository.findByEmail("john@example.com"))
                    .thenReturn(Optional.of(user));

            User result = authService.authenticate("john@example.com", "password123");

            assertEquals(1L, result.getId());
            assertEquals("john@example.com", result.getEmail());
        }

        @Test
        @DisplayName("should throw when user not found")
        void shouldThrowWhenUserNotFound() {
            when(userRepository.findByEmail("unknown@example.com"))
                    .thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> authService.authenticate("unknown@example.com", "password123"));
        }

        @Test
        @DisplayName("should throw when password is wrong")
        void shouldThrowOnWrongPassword() {
            User user = new User(1L, "John", "Doe", "john@example.com",
                    "encoded_password123", UserRole.USER);
            when(userRepository.findByEmail("john@example.com"))
                    .thenReturn(Optional.of(user));

            assertThrows(IllegalArgumentException.class,
                    () -> authService.authenticate("john@example.com", "wrong_password"));
        }
    }
}
