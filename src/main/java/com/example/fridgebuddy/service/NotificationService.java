package com.example.fridgebuddy.service;

import com.example.fridgebuddy.model.Ingredient;
import com.example.fridgebuddy.model.Notification;
import com.example.fridgebuddy.model.User;
import com.example.fridgebuddy.repository.IngredientRepository;
import com.example.fridgebuddy.repository.NotificationRepository;
import com.example.fridgebuddy.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NotificationService {
    private final IngredientRepository ingredientRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(IngredientRepository ingredientRepository,
                               NotificationRepository notificationRepository,
                               UserRepository userRepository) {
        this.ingredientRepository = ingredientRepository;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public void generateForUser(Long userId) {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(3);
        List<Ingredient> expiring = ingredientRepository.findExpiringBetween(userId, start, end);

        for (Ingredient i : expiring) {
            if (notificationRepository.existsByIngredientIdAndSeenFalse(i.getId())) continue;
            Notification n = new Notification();
            n.setUser(i.getUser());
            n.setIngredient(i);
            n.setMessage(i.getName() + " is expiring on " + i.getExpiryDate());
            n.setSeen(false);
            notificationRepository.save(n);
        }
    }

    @Scheduled(fixedRate = 60000)
    public void scheduledGenerateForAllUsers() {
        List<User> users = userRepository.findAll();
        for (User u : users) {
            if (u.getId() != null) generateForUser(u.getId());
        }
    }

    public List<Notification> getUnseen(Long userId) {
        return notificationRepository.findByUserIdAndSeenFalse(userId);
    }

    public Notification markSeen(Long id) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        n.setSeen(true);
        return notificationRepository.save(n);
    }
}
