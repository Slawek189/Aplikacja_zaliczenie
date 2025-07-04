package com.example.Aplikacja_zaliczenie.repository;

import com.example.Aplikacja_zaliczenie.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    boolean existsByUsername(String username);
}