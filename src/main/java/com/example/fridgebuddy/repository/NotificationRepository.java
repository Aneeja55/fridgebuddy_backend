package com.example.fridgebuddy.repository;

import com.example.fridgebuddy.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    void deleteByIngredientId(Long ingredientId);
    boolean existsByIngredientIdAndUserId(Long ingredientId, Long userId);
    boolean existsByIngredientIdAndUserIdAndMessageContaining(Long ingredientId, Long userId, String keyword);

}
