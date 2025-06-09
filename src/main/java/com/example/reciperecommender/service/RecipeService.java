package com.example.reciperecommender.service;

import com.example.reciperecommender.dto.*;
import com.example.reciperecommender.model.*;
import com.example.reciperecommender.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final FavoriteRepository favoriteRepository;
    private static final Logger log = LoggerFactory.getLogger(RecipeService.class);

    @Transactional
    public Recipe addRecipe(AddRecipeRequest request) {
        User creator = userRepository.findById(request.getCreatorId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Recipe recipe = Recipe.builder()
                .name(request.getName())
                .instructions(request.getInstructions())
                .imageUrl(request.getImageUrl())
                .creator(creator)
                .build();

        Recipe savedRecipe = recipeRepository.save(recipe);
        recipeRepository.flush();

        List<RecipeIngredient> recipeIngredients = new ArrayList<>();

        for (AddRecipeRequest.IngredientDto ing : request.getIngredients()) {
            Ingredient ingredient = ingredientRepository.findByNameIgnoreCase(ing.getName())
                    .orElseGet(() -> ingredientRepository.save(
                            Ingredient.builder().name(ing.getName()).build()));

            RecipeIngredient recipeIngredient = RecipeIngredient.builder()
                 .id(new RecipeIngredientId(savedRecipe.getId(), ingredient.getId()))
                    .recipe(savedRecipe)
                    .ingredient(ingredient)
                    .quantity(ing.getQuantity())
                    .build();

            recipeIngredients.add(recipeIngredient);
        }

        recipeIngredientRepository.saveAll(recipeIngredients);

        return savedRecipe;
    }
    @Transactional
    public void deleteRecipesByCreatorId(Long creatorId) {
        List<Recipe> recipes = recipeRepository.findByCreatorId(creatorId);
        if (recipes.isEmpty()) {
            throw new RuntimeException("No recipes found for this creator");
        }
        recipeRepository.deleteAll(recipes);
    }
    public List<RecipeDto> getRecipesByCreatorId(Long creatorId) {
        List<Recipe> recipes = recipeRepository.findByCreatorId(creatorId);

        return recipes.stream()
                .map(this:: convertToDto)
                .toList();
    }

    @Transactional
    public Recipe updateRecipe(Long id, RecipeDto updated) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        recipe.setName(updated.getName());
        recipe.setInstructions(updated.getInstructions());
        recipe.setImageUrl(updated.getImageUrl());


        return recipeRepository.save(recipe);
    }
    @Transactional
    public void deleteRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));


        recipeRepository.delete(recipe);
    }

    public List<RecipeDto> findRecipesByIngredients(List<String> ingredientNames) {
        Set<String> available = ingredientNames.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        List<Recipe> allRecipes = recipeRepository.findAll();

        List<Recipe> matchingRecipes = allRecipes.stream()
                .filter(recipe -> recipe.getRecipeIngredients().stream()
                        .allMatch(ri -> available.contains(ri.getIngredient().getName().toLowerCase())))
                .collect(Collectors.toList());

        if (!matchingRecipes.isEmpty()) {
            return matchingRecipes.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        }

        return allRecipes.stream()
                .sorted(Comparator.comparingInt(recipe -> {
                    long missingCount = recipe.getRecipeIngredients().stream()
                            .map(ri -> ri.getIngredient().getName().toLowerCase())
                            .filter(ing -> !available.contains(ing))
                            .count();
                    return (int) missingCount;
                }))
                .limit(5)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    private RecipeDto convertToDto(Recipe recipe) {
        List<RecipeDto.IngredientDto> ingredientDtos = recipe.getRecipeIngredients().stream()
                .map(ri -> RecipeDto.IngredientDto.builder()
                        .name(ri.getIngredient().getName())
                        .quantity(ri.getQuantity())
                        .build())
                .toList();

        return RecipeDto.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .instructions(recipe.getInstructions())
                .imageUrl(recipe.getImageUrl())
                .ingredients(ingredientDtos)
                .build();
    }


    public List<RecipeDto> searchRecipesByName(String keyword) {
        return recipeRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(this::convertToDto)
                .toList();
    }

    public void addRecipeToFavorites(AddFavoriteRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Recipe recipe = recipeRepository.findById(request.getRecipeId())
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        boolean exists = favoriteRepository.existsByUserIdAndRecipeId(user.getId(), recipe.getId());
        if (exists) {
            throw new RuntimeException("Recipe is already in favorites");
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setRecipe(recipe);

        favoriteRepository.save(favorite);
    }


    public List<RecipeDto> getFavoriteRecipes(Long userId) {
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);
        return favorites.stream()
                .map(fav -> {
                    Recipe recipe = fav.getRecipe();
                    return RecipeDto.builder()
                            .id(recipe.getId())
                            .name(recipe.getName())
                            .instructions(recipe.getInstructions())
                            .imageUrl(recipe.getImageUrl())
                            .build();
                })
                .toList();
    }
    @Transactional
    public void removeRecipeFromFavorites(Long userId, Long recipeId) {
        Favorite favorite = favoriteRepository.findByUserIdAndRecipeId(userId, recipeId)
                .orElseThrow(() -> new RuntimeException("Favorite not found"));

        favoriteRepository.delete(favorite);
    }




}
