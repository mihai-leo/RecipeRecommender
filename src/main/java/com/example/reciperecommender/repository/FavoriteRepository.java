package com.example.reciperecommender.repository;

import com.example.reciperecommender.model.Favorite;
import com.example.reciperecommender.model.FavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {
    List<Favorite> findByUserId(Long userId);
    boolean existsByUserIdAndRecipeId(Long userId, Long recipeId);
    void deleteByUserIdAndRecipeId(Long userId, Long recipeId);
    Optional<Favorite> findByUserIdAndRecipeId(Long userId, Long recipeId);
}
