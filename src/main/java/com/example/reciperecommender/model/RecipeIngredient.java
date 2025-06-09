package com.example.reciperecommender.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "recipe_ingredients")
public class RecipeIngredient {

    @EmbeddedId
    private RecipeIngredientId id = new RecipeIngredientId();

    @ManyToOne
    @MapsId("recipeId")

    private Recipe recipe;

    @ManyToOne
    @MapsId("ingredientId")
    private Ingredient ingredient;

    private String quantity;
}
