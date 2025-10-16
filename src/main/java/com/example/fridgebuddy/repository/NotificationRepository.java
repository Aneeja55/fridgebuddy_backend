package com.example.fridgebuddy.repository;

import com.example.fridgebuddy.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    boolean existsByIngredientIdAndSeenFalse(Long ingredientId);
    List<Notification> findByUserIdAndSeenFalse(Long userId);
}
