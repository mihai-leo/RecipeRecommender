package com.example.reciperecommender.controller;
import com.example.reciperecommender.dto.IngredientDto;
import com.example.reciperecommender.dto.RecipeDto;
import com.example.reciperecommender.dto.RecipeResponse;
import com.example.reciperecommender.dto.UserDto;
import com.example.reciperecommender.model.Ingredient;
import com.example.reciperecommender.model.Recipe;
import com.example.reciperecommender.model.User;
import com.example.reciperecommender.repository.IngredientRepository;
import com.example.reciperecommender.repository.RecipeRepository;
import com.example.reciperecommender.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDto(user.getId(), user.getName(), user.getEmail(), user.getRole()))
                .collect(Collectors.toList());
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDto updated) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(updated.getName());
                    user.setEmail(updated.getEmail());
                    user.setRole(updated.getRole());
                    userRepository.save(user);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // === RECIPES ===
    @GetMapping("/recipes")
    public List<RecipeResponse> getAllRecipes() {
        return recipeRepository.findAll().stream()
                .map(recipe -> new RecipeResponse(recipe.getId(), recipe.getName(), recipe.getInstructions(), recipe.getImageUrl()))
                .collect(Collectors.toList());
    }

    @PutMapping("/recipes/{id}")
    public ResponseEntity<?> updateRecipe(@PathVariable Long id, @RequestBody RecipeDto updated) {
        return recipeRepository.findById(id)
                .map(recipe -> {
                    recipe.setName(updated.getName());
                    recipe.setInstructions(updated.getInstructions());
                    recipe.setImageUrl(updated.getImageUrl());
                    recipeRepository.save(recipe);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        recipeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // === INGREDIENTS ===
    @GetMapping("/ingredients")
    public List<IngredientDto> getAllIngredients() {
        return ingredientRepository.findAll().stream()
                .map(i -> new IngredientDto(i.getId(), i.getName()))
                .collect(Collectors.toList());
    }

    @PutMapping("/ingredients/{id}")
    public ResponseEntity<?> updateIngredient(@PathVariable Long id, @RequestBody IngredientDto updated) {
        return ingredientRepository.findById(id)
                .map(ingredient -> {
                    ingredient.setName(updated.getName());
                    ingredientRepository.save(ingredient);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/ingredients/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long id) {
        ingredientRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
