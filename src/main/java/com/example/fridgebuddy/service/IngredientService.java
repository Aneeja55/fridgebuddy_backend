package com.example.fridgebuddy.service;
import org.springframework.transaction.annotation.Transactional;
import com.example.fridgebuddy.model.Ingredient;
import com.example.fridgebuddy.model.Notification;
import com.example.fridgebuddy.model.User;
import com.example.fridgebuddy.repository.IngredientRepository;
import com.example.fridgebuddy.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public IngredientService(IngredientRepository ingredientRepository,
                             NotificationService notificationService,
                             UserRepository userRepository) {
        this.ingredientRepository = ingredientRepository;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    // ✅ Add new ingredient
    public Ingredient addIngredient(Ingredient ingredient) {
        if (ingredient.getUser() == null || ingredient.getUser().getId() == null) {
            throw new IllegalArgumentException("User ID is required");
        }

        User user = userRepository.findById(ingredient.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ingredient.setUser(user);

        if (ingredient.getExpiryDate().isBefore(ingredient.getPurchaseDate())) {
            throw new IllegalArgumentException("Expiry date cannot be before purchase date.");
        }

        Ingredient saved = ingredientRepository.save(ingredient);

        // ✅ Create a notification if expiring soon (within 3 days)
        LocalDate today = LocalDate.now();
        if (!saved.getExpiryDate().isBefore(today) &&
                saved.getExpiryDate().isBefore(today.plusDays(3))) {
            String msg = saved.getName() + " is expiring on " + saved.getExpiryDate();
            notificationService.createNotification(msg, user, saved);
        }

        return saved;
    }

    // ✅ Fetch ingredients by user and generate expiry notifications dynamically
    public List<Ingredient> getByUser(Long userId) {
        List<Ingredient> ingredients = ingredientRepository.findByUserId(userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Create notifications for expired items (not used yet)
        notificationService.generateExpiryNotifications(user, ingredients);
        return ingredients;
    }

    // ✅ Delete ingredient (and related notifications)
    @Transactional
    public void deleteIngredient(Long id) {
        notificationService.deleteByIngredient(id);
        ingredientRepository.deleteById(id);
    }
}
