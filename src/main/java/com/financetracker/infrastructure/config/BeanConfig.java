package com.financetracker.infrastructure.config;

import com.financetracker.application.service.AuthService;
import com.financetracker.application.service.CategoryService;
import com.financetracker.application.service.TransactionService;
import com.financetracker.domain.port.out.CategoryRepository;
import com.financetracker.domain.port.out.TransactionRepository;
import com.financetracker.domain.port.out.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Wires application services as Spring beans.
 * This is the only place where domain/application layer knows about Spring.
 * The services themselves remain framework-agnostic.
 */
@Configuration
public class BeanConfig {

    @Bean
    public CategoryService categoryService(CategoryRepository categoryRepository) {
        return new CategoryService(categoryRepository);
    }

    @Bean
    public TransactionService transactionService(TransactionRepository transactionRepository,
                                                  CategoryRepository categoryRepository) {
        return new TransactionService(transactionRepository, categoryRepository);
    }

    @Bean
    public AuthService authService(UserRepository userRepository,
                                    BCryptPasswordEncoder bCryptPasswordEncoder) {
        // Adapter: Bridge Spring's BCrypt into the domain's PasswordEncoder interface
        AuthService.PasswordEncoder passwordEncoder = new AuthService.PasswordEncoder() {
            @Override
            public String encode(String rawPassword) {
                return bCryptPasswordEncoder.encode(rawPassword);
            }

            @Override
            public boolean matches(String rawPassword, String encodedPassword) {
                return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
            }
        };
        return new AuthService(userRepository, passwordEncoder);
    }
}
