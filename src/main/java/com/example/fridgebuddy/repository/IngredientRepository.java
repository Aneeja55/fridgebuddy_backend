package com.example.fridgebuddy.repository;

import com.example.fridgebuddy.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    @Query("SELECT i FROM Ingredient i WHERE i.user.id = :userId AND i.expiryDate BETWEEN :start AND :end")
    List<Ingredient> findExpiringBetween(@Param("userId") Long userId,
                                         @Param("start") LocalDate start,
                                         @Param("end") LocalDate end);

    List<Ingredient> findByUserId(Long userId);
}
