package com.example.fridgebuddy.controller;

import com.example.fridgebuddy.dto.NotificationResponse;
import com.example.fridgebuddy.model.Notification;
import com.example.fridgebuddy.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "http://localhost:3000")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/generate/{userId}")
    public ResponseEntity<?> generate(@PathVariable Long userId) {
        notificationService.generateForUser(userId);
        return ResponseEntity.ok("Notifications generated for user " + userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationResponse>> getUnseen(@PathVariable Long userId) {
        List<NotificationResponse> resp = notificationService.getUnseen(userId)
                .stream().map(this::map).collect(Collectors.toList());
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/{id}/seen")
    public ResponseEntity<NotificationResponse> markSeen(@PathVariable Long id) {
        Notification n = notificationService.markSeen(id);
        return ResponseEntity.ok(map(n));
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
