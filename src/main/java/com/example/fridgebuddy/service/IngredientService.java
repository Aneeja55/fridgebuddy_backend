package com.example.fridgebuddy.service;

import com.example.fridgebuddy.model.Ingredient;
import com.example.fridgebuddy.model.Notification;
import com.example.fridgebuddy.model.User;
import com.example.fridgebuddy.repository.IngredientRepository;
import com.example.fridgebuddy.repository.NotificationRepository;
import com.example.fridgebuddy.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public IngredientService(IngredientRepository ingredientRepository,
                             NotificationRepository notificationRepository,
                             UserRepository userRepository) {
        this.ingredientRepository = ingredientRepository;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

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

        LocalDate today = LocalDate.now();
        if (!saved.getExpiryDate().isBefore(today) &&
                saved.getExpiryDate().isBefore(today.plusDays(3))) {
            Notification n = new Notification();
            n.setUser(saved.getUser());
            n.setIngredient(saved);
            n.setMessage(saved.getName() + " is expiring on " + saved.getExpiryDate());
            notificationRepository.save(n);
        }

        return saved;
    }

    public List<Ingredient> getByUser(Long userId) {
        return ingredientRepository.findByUserId(userId);
    }

    public void deleteIngredient(Long id) {
        ingredientRepository.deleteById(id);
    }
}
