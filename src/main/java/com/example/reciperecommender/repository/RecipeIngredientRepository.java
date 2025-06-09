package com.example.reciperecommender.repository;

import com.example.reciperecommender.model.RecipeIngredient;
import com.example.reciperecommender.model.RecipeIngredientId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, RecipeIngredientId> {
    List<RecipeIngredient> findByRecipeId(Long recipeId);
}
