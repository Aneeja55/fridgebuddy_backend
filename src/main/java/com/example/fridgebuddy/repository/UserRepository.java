package com.example.fridgebuddy.repository;

import java.util.Optional;
import com.example.fridgebuddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
