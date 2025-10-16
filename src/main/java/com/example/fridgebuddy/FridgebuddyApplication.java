package com.example.fridgebuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FridgebuddyApplication {
    public static void main(String[] args) {
        SpringApplication.run(FridgebuddyApplication.class, args);
    }
}
