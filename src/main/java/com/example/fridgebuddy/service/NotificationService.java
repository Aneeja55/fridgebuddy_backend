package com.example.fridgebuddy.service;

import com.example.fridgebuddy.model.Notification;
import com.example.fridgebuddy.model.User;
import com.example.fridgebuddy.model.Ingredient;
import com.example.fridgebuddy.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    // ✅ Create a notification
    public void createNotification(String message, User user, Ingredient ingredient) {
        Notification n = new Notification(message, user, ingredient);
        n.setCreatedAt(LocalDateTime.now());
        repository.save(n);
    }

    // ✅ Get notifications for a specific user
    public List<Notification> getByUser(Long userId) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    // ✅ Mark all notifications as read
    public void markAllSeen(Long userId) {
        List<Notification> notifications = repository.findByUserIdOrderByCreatedAtDesc(userId);
        for (Notification n : notifications) {
            n.setSeen(true);
        }
        repository.saveAll(notifications);
    }

    // ✅ Delete notification when ingredient is deleted (optional)
    public void deleteByIngredient(Long ingredientId) {
        repository.deleteByIngredientId(ingredientId);
    }

    // ✅ Automatically generate expiry notifications
    public void generateExpiryNotifications(User user, List<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            // Skip if already used
            if (ingredient.getStatus() != null && ingredient.getStatus().name().equalsIgnoreCase("USED")) continue;

            // Check if expired
            if (ingredient.getExpiryDate() != null && ingredient.getExpiryDate().isBefore(LocalDate.now())) {
                // Check if already notified
                boolean exists = repository.existsByIngredientIdAndUserId(ingredient.getId(), user.getId());
                if (!exists) {
                    String msg = "⚠️ Your ingredient '" + ingredient.getName() + "' has expired!";
                    createNotification(msg, user, ingredient);
                }
            }
        }
    }
}
