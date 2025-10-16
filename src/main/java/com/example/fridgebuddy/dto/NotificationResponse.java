package com.example.fridgebuddy.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    Long id;
    Long ingredientId;
    String message;
    LocalDateTime createdAt;
    boolean seen;
}
