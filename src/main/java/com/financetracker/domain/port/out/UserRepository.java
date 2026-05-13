package com.financetracker.domain.port.out;

import com.financetracker.domain.model.User;

import java.util.Optional;

/**
 * Output port for user persistence.
 */
public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
