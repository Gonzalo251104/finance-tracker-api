package com.financetracker.infrastructure.config;

import com.financetracker.application.service.CategoryService;
import com.financetracker.application.service.TransactionService;
import com.financetracker.domain.port.out.CategoryRepository;
import com.financetracker.domain.port.out.TransactionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
