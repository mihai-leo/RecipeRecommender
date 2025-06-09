package com.example.reciperecommender;


import com.example.reciperecommender.dto.AddFavoriteRequest;
import com.example.reciperecommender.dto.RecipeDto;
import com.example.reciperecommender.model.Favorite;
import com.example.reciperecommender.model.Recipe;
import com.example.reciperecommender.model.User;
import com.example.reciperecommender.repository.*;
import com.example.reciperecommender.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.reciperecommender.model.*;
import com.example.reciperecommender.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @InjectMocks
    private RecipeService recipeService;

    @Mock private RecipeRepository recipeRepository;
    @Mock private IngredientRepository ingredientRepository;
    @Mock private UserRepository userRepository;
    @Mock private RecipeIngredientRepository recipeIngredientRepository;
    @Mock private FavoriteRepository favoriteRepository;

    private Recipe recipe;
    private User user;

    @BeforeEach
    void setUp() {
        recipe = Recipe.builder().id(1L).name("Pasta").instructions("Boil").imageUrl("pasta.jpg").build();
        user = User.builder().id(1L).name("Test User").build();
    }

    @Test
    void testDeleteRecipesByCreatorId_Success() {
        Long creatorId = 1L;
        Recipe mockRecipe = new Recipe();
        when(recipeRepository.findByCreatorId(creatorId)).thenReturn(List.of(mockRecipe));

        recipeService.deleteRecipesByCreatorId(creatorId);

        verify(recipeRepository).deleteAll(List.of(mockRecipe));
    }

    @Test
    void testDeleteRecipesByCreatorId_NotFound() {
        when(recipeRepository.findByCreatorId(1L)).thenReturn(Collections.emptyList());

        assertThrows(RuntimeException.class, () -> recipeService.deleteRecipesByCreatorId(1L));
    }
    // --- getRecipesByCreatorId ---
    @Test
    void testGetRecipesByCreatorId() {
        Recipe recipe = Recipe.builder().id(1L).name("Test").instructions("Do it").build();
        recipe.setRecipeIngredients(new ArrayList<>());
        when(recipeRepository.findByCreatorId(1L)).thenReturn(List.of(recipe));

        List<RecipeDto> dtos = recipeService.getRecipesByCreatorId(1L);

        assertEquals(1, dtos.size());
        assertEquals("Test", dtos.get(0).getName());
    }
    // --- deleteRecipe ---
    @Test
    void testDeleteRecipe_Success() {
        Recipe recipe = Recipe.builder().id(1L).build();
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));

        recipeService.deleteRecipe(1L);

        verify(recipeRepository).delete(recipe);
    }

    @Test
    void testDeleteRecipe_NotFound() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> recipeService.deleteRecipe(1L));
    }

    // --- searchRecipesByName ---
    @Test
    void testSearchRecipesByName() {
        Recipe recipe = Recipe.builder().id(1L).name("Pizza").instructions("Cook").build();
        recipe.setRecipeIngredients(new ArrayList<>());
        when(recipeRepository.findByNameContainingIgnoreCase("pizza")).thenReturn(List.of(recipe));

        List<RecipeDto> results = recipeService.searchRecipesByName("pizza");

        assertEquals(1, results.size());
        assertEquals("Pizza", results.get(0).getName());
    }
    // --- updateRecipe ---
    @Test
    void testUpdateRecipe_Success() {
        Long id = 1L;
        Recipe existing = Recipe.builder().id(id).name("Old").build();
        when(recipeRepository.findById(id)).thenReturn(Optional.of(existing));
        when(recipeRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        RecipeDto updated = RecipeDto.builder().name("New").instructions("New instructions").imageUrl("url").build();

        Recipe result = recipeService.updateRecipe(id, updated);
        assertEquals("New", result.getName());
    }

    @Test
    void testUpdateRecipe_NotFound() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> recipeService.updateRecipe(1L, new RecipeDto()));
    }
    // -Favorites ---

    @Test
    void testAddRecipeToFavorites() {
        Long a =1L;
        Long b=1L;
        AddFavoriteRequest request = new AddFavoriteRequest();
        request.setUserId(1L);
        request.setRecipeId(1L);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        Mockito.when(favoriteRepository.existsByUserIdAndRecipeId(1L, 1L)).thenReturn(false);

        assertDoesNotThrow(() -> recipeService.addRecipeToFavorites(request));
    }

    @Test
    void testGetFavorites() {
        Favorite fav = new Favorite();
        fav.setUser(user);
        fav.setRecipe(recipe);

        Mockito.when(favoriteRepository.findByUserId(1L)).thenReturn(List.of(fav));

        List<RecipeDto> result = recipeService.getFavoriteRecipes(1L);
        assertEquals(1, result.size());
        assertEquals("Pasta", result.get(0).getName());
    }
    @Test
    void testRemoveRecipeFromFavorites_Success() {
        Long userId = 1L, recipeId = 2L;
        Favorite fav = new Favorite();
        when(favoriteRepository.findByUserIdAndRecipeId(userId, recipeId)).thenReturn(Optional.of(fav));

        recipeService.removeRecipeFromFavorites(userId, recipeId);

        verify(favoriteRepository).delete(fav);
    }

    @Test
    void testRemoveRecipeFromFavorites_NotFound() {
        when(favoriteRepository.findByUserIdAndRecipeId(1L, 2L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> recipeService.removeRecipeFromFavorites(1L, 2L));
    }
}
