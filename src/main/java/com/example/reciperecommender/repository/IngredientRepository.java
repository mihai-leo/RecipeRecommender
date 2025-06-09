package com.example.reciperecommender.repository;

import com.example.reciperecommender.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Optional<Ingredient> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
    List<Ingredient> findByNameStartingWithIgnoreCase(String prefix);

}
