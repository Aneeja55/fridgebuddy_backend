package com.example.fridgebuddy.controller;

import com.example.fridgebuddy.model.User;
import com.example.fridgebuddy.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserRepository repo;

    public UserController(UserRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody User user) {
        User saved = repo.save(user);
        saved.setPassword(null);
        return ResponseEntity.ok(saved);
    }
}
