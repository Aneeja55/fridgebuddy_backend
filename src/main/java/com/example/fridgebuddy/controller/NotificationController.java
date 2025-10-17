package com.example.fridgebuddy.controller;

import com.example.fridgebuddy.dto.NotificationResponse;
import com.example.fridgebuddy.model.Notification;
import com.example.fridgebuddy.model.User;
import com.example.fridgebuddy.service.NotificationService;
import com.example.fridgebuddy.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "http://localhost:3000")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public NotificationController(NotificationService notificationService, UserRepository userRepository) {
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    // ✅ Automatically generate expiry notifications for a given user
    @PostMapping("/generate/{userId}")
    public ResponseEntity<?> generate(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        notificationService.generateExpiryNotifications(user, user.getIngredients());
        return ResponseEntity.ok("Notifications generated for user " + userId);
    }

    // ✅ Get all notifications for a user
    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationResponse>> getByUser(@PathVariable Long userId) {
        List<NotificationResponse> resp = notificationService.getByUser(userId)
                .stream().map(this::map).collect(Collectors.toList());
        return ResponseEntity.ok(resp);
    }

    // ✅ Mark all notifications as seen
    @PutMapping("/{userId}/seen")
    public ResponseEntity<String> markAllSeen(@PathVariable Long userId) {
        notificationService.markAllSeen(userId);
        return ResponseEntity.ok("All notifications marked as seen.");
    }

    private NotificationResponse map(Notification n) {
        return new NotificationResponse(
                n.getId(),
                n.getIngredient() != null ? n.getIngredient().getId() : null,
                n.getMessage(),
                n.getCreatedAt(),
                n.isSeen()
        );
    }
}
