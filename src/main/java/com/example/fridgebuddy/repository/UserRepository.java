package com.example.fridgebuddy.repository;

import com.example.fridgebuddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
