package com.example.reciperecommender.controller;
import com.example.reciperecommender.dto.*;
import com.example.reciperecommender.repository.UserRepository;
import com.example.reciperecommender.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/home-cook")
@RequiredArgsConstructor
public class HomeCookController {
    private final RecipeService recipeService;
    private final UserRepository userRepository;

    @PostMapping("/find-recipes")
    public ResponseEntity<List<RecipeDto>> findByIngredients(@RequestBody RecipeSearchRequest request) {
        return ResponseEntity.ok(recipeService.findRecipesByIngredients(request.getIngredientNames()));
    }


    @GetMapping("/search-recipes")
    public ResponseEntity<List<RecipeDto>> searchByName(@RequestParam("searchName") String searchName){
        return ResponseEntity.ok(recipeService.searchRecipesByName(searchName));
    }

    @PostMapping("/add-favorite")
    public ResponseEntity<?> addFavorite(@RequestBody AddFavoriteRequest request) {
        recipeService.addRecipeToFavorites(request);
        return ResponseEntity.ok("Added to favorites");
    }
    @DeleteMapping("/remove-favorite")
    public ResponseEntity<?> removeFavorite(@RequestBody AddFavoriteRequest request) {
        recipeService.removeRecipeFromFavorites(request.getUserId(), request.getRecipeId());
        return ResponseEntity.ok("Removed from favorites");
    }


    @GetMapping("/favorites/{userId}")
    public ResponseEntity<List<RecipeDto>> getFavorites(@PathVariable Long userId) {
        List<RecipeDto> favoriteRecipes = recipeService.getFavoriteRecipes(userId);
        return ResponseEntity.ok(favoriteRecipes);
    }

}
