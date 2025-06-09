package com.example.reciperecommender.repository;

import com.example.reciperecommender.model.Recipe;
import com.example.reciperecommender.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByCreator(User creator);
    List<Recipe> findByNameContainingIgnoreCase(String keyword);

    List<Recipe> findByCreatorId(Long creatorId);
}
