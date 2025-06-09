package com.example.reciperecommender.dto;

import lombok.Data;

import java.util.List;

@Data
public class AddRecipeRequest {
    private Long creatorId;
    private String name;
    private String instructions;
    private String imageUrl;
    private List<IngredientDto> ingredients;

    @Data
    public static class IngredientDto {
        private String name;
        private String quantity;
    }
}
