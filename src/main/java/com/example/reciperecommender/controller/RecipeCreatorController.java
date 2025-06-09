package com.example.reciperecommender.controller;

import com.example.reciperecommender.dto.AddRecipeRequest;
import com.example.reciperecommender.dto.AddRecipeResponse;
import com.example.reciperecommender.dto.RecipeDto;
import com.example.reciperecommender.model.Recipe;
import com.example.reciperecommender.model.User;
import com.example.reciperecommender.model.Role;
import com.example.reciperecommender.service.RecipeService;
import com.example.reciperecommender.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/creator")
@RequiredArgsConstructor
public class RecipeCreatorController {

    private final RecipeService recipeService;
    private final UserRepository userRepository;

    @PostMapping("/add-recipe")
    public ResponseEntity<?> addRecipe(@RequestBody AddRecipeRequest request) {
        User user = userRepository.findById(request.getCreatorId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.RECIPE_CREATOR) {
            return ResponseEntity.status(403).body("Access denied: Not a recipe creator");
        }
        Recipe saved = recipeService.addRecipe(request);
        AddRecipeResponse response = new AddRecipeResponse(
                saved.getId(), saved.getName(), saved.getInstructions(), saved.getImageUrl()
        );
        return ResponseEntity.ok(response);
    }
    @GetMapping("/my-recipes/{creatorId}")
    public ResponseEntity<?> getMyRecipes(@PathVariable Long creatorId) {
        List<RecipeDto> dtos = recipeService.getRecipesByCreatorId(creatorId);
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/update-recipe/{id}")
    public ResponseEntity<?> updateRecipe(@PathVariable Long id, @RequestBody RecipeDto updated) {
        try {
            Recipe updatedRecipe = recipeService.updateRecipe(id, updated);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/delete-recipe/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long id) {
        try {
            recipeService.deleteRecipe(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }




}
