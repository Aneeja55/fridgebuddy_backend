package com.example.fridgebuddy.controller;

import com.example.fridgebuddy.dto.IngredientResponse;
import com.example.fridgebuddy.model.Ingredient;
import com.example.fridgebuddy.model.IngredientStatus;
import com.example.fridgebuddy.repository.IngredientRepository;
import com.example.fridgebuddy.service.IngredientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ingredients")
@CrossOrigin(origins = "http://localhost:3000")
public class IngredientController {

    private final IngredientService service;
    private final IngredientRepository repository;

    public IngredientController(IngredientService service, IngredientRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<?> addIngredient(@RequestBody Ingredient ingredient) {
        try {
            if (ingredient.getUser() == null || ingredient.getUser().getId() == null) {
                return ResponseEntity.badRequest().body("User ID is required");
            }
            Ingredient saved = service.addIngredient(ingredient);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<IngredientResponse>> getByUser(@PathVariable Long userId) {
        List<IngredientResponse> list = service.getByUser(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            Ingredient ingredient = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Ingredient not found with id: " + id));

            IngredientStatus newStatus = IngredientStatus.valueOf(status.toUpperCase());
            ingredient.setStatus(newStatus);

            Ingredient updated = repository.save(ingredient);
            return ResponseEntity.ok(mapToResponse(updated));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status value");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error updating status: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.deleteIngredient(id);
            return ResponseEntity.ok("Ingredient deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error deleting ingredient: " + e.getMessage());
        }
    }

    private IngredientResponse mapToResponse(Ingredient i) {
        return new IngredientResponse(
                i.getId(),
                i.getUser() != null ? i.getUser().getId() : null,
                i.getName(),
                i.getCategory(),
                i.getPurchaseDate(),
                i.getExpiryDate(),
                i.getStatus() != null ? i.getStatus().name() : null
        );
    }
}
